#!/usr/bin/env bash
# End-to-end CUA (computer-use agent) tests for Taro.
#
# Drives a real build of the app on an Android emulator with a vision LLM,
# covering every PRD user flow, and captures screenshots + video + GIF per case
# (reused for Play Store media). Uses the shared a-test harness.
#
# Requirements:
#   - An Android emulator/device attached (set ANDROID_SERIAL, e.g. emulator-5554)
#   - The app installed:  ./gradlew :app:assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk
#   - A vision model. This runner uses Azure OpenAI via env:
#       AZURE_OPENAI_API_KEY, AZURE_OPENAI_ENDPOINT, AZURE_OPENAI_MODEL (or set MODEL)
#     (a-test also accepts OPENAI_API_KEY / other backends -- see a-test docs.)
#
# Usage:
#   ANDROID_SERIAL=emulator-5554 tests/cua/run.sh                 # all cases
#   ANDROID_SERIAL=emulator-5554 tests/cua/run.sh daily_draw      # one case
set -uo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CASES_DIR="$HERE/cases"
OUT_ROOT="${OUT_ROOT:-$HERE/output}"
MODEL="${MODEL:-${AZURE_OPENAI_MODEL:-gpt-4o}}"
GROUNDING="${GROUNDING:-a11y}"

# a-test is pinned to the commit that adds a11y tap grounding + resilient
# recording teardown (dzianisv/a-test#19). Bump after it merges to main.
ATEST_REF="${ATEST_REF:-2b3c6ee4206b18d845fd961276ee179d855b2ffc}"
VENV="${VENV:-/tmp/a-test-venv}"

export ANDROID_SERIAL="${ANDROID_SERIAL:-emulator-5554}"

# --- ensure a-test is installed (pinned) ---
if [ ! -d "$VENV" ]; then
  python3 -m venv "$VENV"
  # shellcheck disable=SC1091
  . "$VENV/bin/activate"
  pip install -q --upgrade pip
  pip install -q "a-test @ git+https://github.com/dzianisv/a-test.git@${ATEST_REF}"
else
  # shellcheck disable=SC1091
  . "$VENV/bin/activate"
fi

# --- device prep ---
# Infinite Compose animations (cosmic starfield, CTA pulse) keep uiautomator
# from ever reaching idle, breaking a11y grounding + XML. Freeze them.
for s in window_animation_scale transition_animation_scale animator_duration_scale; do
  adb -s "$ANDROID_SERIAL" shell settings put global "$s" 0 2>/dev/null || true
done

CASES=(daily_draw three_card_spread scan_card share_reading history settings)
[ "$#" -gt 0 ] && CASES=("$@")

PKG="com.aistudio.taro.xkyzv"
mkdir -p "$OUT_ROOT"
declare -A RESULT
fail=0
for c in "${CASES[@]}"; do
  echo "==================== CASE: $c ===================="
  out="$OUT_ROOT/$c"
  mkdir -p "$out"
  # Cold-start each case from Home for determinism.
  adb -s "$ANDROID_SERIAL" shell am force-stop "$PKG" 2>/dev/null || true
  sleep 1.5
  python -m a_test run --target android --case "$CASES_DIR/$c.py" \
    --model "$MODEL" --include-xml --grounding "$GROUNDING" --output-dir "$out" 2>&1 | tee "$out/run.log"
  verdict=$(python - "$out/result.json" <<'PY'
import json,sys
try: print(json.load(open(sys.argv[1])).get("verdict","ERROR"))
except Exception: print("ERROR")
PY
)
  RESULT[$c]=$verdict
  [ "$verdict" = "pass" ] || fail=1
  echo "---- $c => $verdict ----"
done

echo ""
echo "==================== SUMMARY ===================="
for c in "${CASES[@]}"; do printf "%-20s %s\n" "$c" "${RESULT[$c]:-?}"; done
exit $fail
