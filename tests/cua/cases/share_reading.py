"""Taro CUA test: viral share loop.

PRD growth lever: every reading exports a 9:16 card (name + streak + referral link)
via the Android share sheet. This is the #1 feature for virality, so we verify the
share entry point produces a shareable card / opens the system share chooser.
"""
from a_test import TestCase, run_case

case = TestCase(
    name="share_reading",
    package="com.aistudio.taro.xkyzv",
    instruction=(
        "The Taro tarot app is on its Home screen. "
        "Your task: get a reading, then share it. "
        "Steps: "
        "1. Tap 'Daily Draw' on the Home screen to generate a reading. "
        "2. Wait for the reading screen to appear (a card + interpretation). Let animations settle. "
        "3. On the reading screen, find and tap the share action (a gold button labelled 'Share this reading' near the bottom). "
        "4. After tapping share, WAIT ~3 seconds for the result. The app renders a shareable tarot card image and then opens the Android system share sheet (a bottom sheet / chooser listing apps like Messages, Gmail, Bluetooth, Copy). Issue a wait action and re-check the screen — do NOT report done in the same step you tapped share. "
        "5. Confirm the Android share sheet / chooser appeared (or a shareable card image is shown). "
        "Report TEST_PASSED once, after tapping share and waiting, the Android system share sheet (app chooser) OR a shareable card image is visible. "
        "Only report TEST_FAILED if there is no share action anywhere on the reading screen, or the app crashes when sharing."
    ),
    successCriteria=[
        "A reading was generated and displayed",
        "After tapping the 'Share this reading' button and waiting, the Android system share sheet (app chooser) is visible, OR a rendered shareable tarot card image is shown",
        "No crash occurs during sharing",
    ],
    failureCriteria=[
        "No share button/icon exists on the reading screen",
        "The app crashes when attempting to share",
    ],
    maxSteps=26,
)

if __name__ == "__main__":
    import os
    result = run_case(case, output_dir=os.environ.get("OUT", "/tmp/taro-cua/output/share_reading"))
    print(f"Verdict: {result['verdict']} -- {result.get('reason', '')}")
    assert result["verdict"] == "pass", f"Test failed: {result.get('reason')}"
