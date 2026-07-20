"""Taro CUA test: reading history persistence.

PRD flow: the switching cost is memory — every reading is saved and re-openable.
Verifies a past reading is listed under Reading History and can be reopened.
"""
from a_test import TestCase, run_case

case = TestCase(
    name="history",
    package="com.aistudio.taro.xkyzv",
    instruction=(
        "The Taro tarot app is on its Home screen. "
        "Your task: verify reading history works. "
        "Steps: "
        "1. First create a reading: tap 'Daily Draw' and wait for the reading to appear. "
        "2. Go back to the Home screen (use the system Back gesture/button or an in-app back arrow). "
        "3. Find the 'Reading History' section on the Home screen. It should list at least one past reading "
        "(showing a reading type like 'Daily' and/or the drawn card). "
        "4. Tap the most recent history entry to reopen it. "
        "5. The saved reading opens and displays its interpretation again — this reopened reading is the proof that history works; END the test here, on the reopened reading screen. "
        "Report TEST_PASSED once you have reopened a saved reading FROM the history list and its interpretation text is displayed. "
        "Only report TEST_FAILED if no history entry ever appears after creating a reading, or the app crashes."
    ),
    successCriteria=[
        "A previously saved reading was reopened from the Reading History list and its interpretation text is now displayed on a reading screen",
        "No crash dialog is visible",
    ],
    failureCriteria=[
        "No history entry appears after a reading was created",
        "The app crashes when opening a history entry",
    ],
    maxSteps=26,
)

if __name__ == "__main__":
    import os
    result = run_case(case, output_dir=os.environ.get("OUT", "/tmp/taro-cua/output/history"))
    print(f"Verdict: {result['verdict']} -- {result.get('reason', '')}")
    assert result["verdict"] == "pass", f"Test failed: {result.get('reason')}"
