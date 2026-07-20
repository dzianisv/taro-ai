"""Taro CUA test: live camera scan of a physical card -> structured reading.

PRD flow: the wedge feature. User points the camera at a physical tarot card and
gets a structured reading (card name, orientation, meaning, advice, lucky elements).
On the emulator the camera shows a virtual scene; the proxy returns 401 without
auth so the app renders its offline structured reading. The flow (camera ->
capture -> structured result screen) is what we verify and screenshot.
"""
from a_test import TestCase, run_case

case = TestCase(
    name="scan_card",
    package="com.aistudio.taro.xkyzv",
    instruction=(
        "The Taro tarot app is on its Home screen. "
        "Your task: use the live camera scanner to scan a physical tarot card and get a reading. "
        "Steps: "
        "1. On the Home screen, tap the 'Scan Physical Card' option (opens the live camera scanner). "
        "2. If Android asks for camera permission, tap 'While using the app' or 'Allow'. "
        "3. The camera preview should appear with a framing overlay. Tap the capture / 'reveal' button to scan the card. "
        "4. Wait for analysis; a loading animation may play. IMPORTANT: after tapping the capture/reveal button, WAIT at least 8 seconds for the analysis to finish and the result screen to appear — do NOT report done while the camera preview is still showing. Issue a wait action (e.g. wait 8 seconds) and re-check the screen. "
        "5. A structured reading should appear (a new screen titled 'Your Reading' with fields such as a card name, an orientation like UPRIGHT/REVERSED, a summary, a general interpretation, and advice). "
        "Report TEST_PASSED once the camera opened AND, after capture, a card reading screen with a card name and interpretation text is shown. "
        "Only report TEST_FAILED if the app crashes, the camera never opens, or no reading is produced after capture AND waiting."
    ),
    successCriteria=[
        "The live camera scanner opened with a preview",
        "After capture, a card reading (card name / meaning / advice, or interpretation text) is displayed",
        "No crash dialog is visible",
    ],
    failureCriteria=[
        "The app crashes when opening the camera or after capture",
        "The camera scanner never opens",
        "No reading is produced after capturing the card",
    ],
    maxSteps=28,
)

if __name__ == "__main__":
    import os
    result = run_case(case, output_dir=os.environ.get("OUT", "/tmp/taro-cua/output/scan_card"))
    print(f"Verdict: {result['verdict']} -- {result.get('reason', '')}")
    assert result["verdict"] == "pass", f"Test failed: {result.get('reason')}"
