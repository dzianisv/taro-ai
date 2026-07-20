# Taro CUA end-to-end tests

Computer-use-agent (CUA) tests that drive a **real build of the app on an Android
emulator** with a vision LLM, covering every PRD user flow. Each case records
screenshots + an MP4 + a GIF (reused for Play Store media).

Built on the shared [`a-test`](https://github.com/dzianisv/a-test) harness
(vision planner + accessibility-tree tap grounding).

## Cases (PRD flow coverage)

| Case | PRD flow |
|---|---|
| `daily_draw` | Tap Daily Draw → single-card reading |
| `three_card_spread` | 3-card Past/Present/Future spread |
| `scan_card` | Live camera scan → structured reading |
| `share_reading` | Viral share loop → Android share sheet |
| `history` | Reading history persists + reopens |
| `settings` | Oracle Settings dialog |

All flows work in **guest mode** (no sign-in): the app falls back to a local
interpreter when the AI proxy is unauthenticated, so the suite passes without
Firebase Auth or Gemini billing.

## Run

```bash
# 1. Build + install the app
export ANDROID_HOME=~/Android/Sdk ANDROID_SDK_ROOT=~/Android/Sdk
./gradlew :app:assembleDebug
adb -s emulator-5554 install -r app/build/outputs/apk/debug/app-debug.apk

# 2. Provide a vision model (Azure OpenAI shown; a-test also supports OpenAI)
export AZURE_OPENAI_API_KEY=... AZURE_OPENAI_ENDPOINT=... AZURE_OPENAI_MODEL=gpt-4o

# 3. Run the suite (or a single case)
ANDROID_SERIAL=emulator-5554 tests/cua/run.sh
ANDROID_SERIAL=emulator-5554 tests/cua/run.sh daily_draw
```

Output (per case) lands in `tests/cua/output/<case>/`: `result.json` (verdict +
judge reason), `<case>.mp4`, `demo.gif`, and `step-*.png`.

## Notes

- **Animations must be off on the device.** Infinite Compose animations (cosmic
  starfield, CTA pulse) keep `uiautomator` from reaching idle, which empties the
  a11y tree and breaks tap grounding. `run.sh` sets the three animation scales to
  `0` automatically.
- **Grounding.** Defaults to `--grounding a11y` (resolves the planner's element
  description to exact `uiautomator` bounds — pixel-perfect taps, no API key).
  Set `GROUNDING=holo` (needs `HAI_API_KEY`) or `GROUNDING=none` (blind taps).
- **a-test pin.** `run.sh` installs `a-test` at a pinned commit
  (`ATEST_REF`). Bump it after dzianisv/a-test#19 merges to `main`.
