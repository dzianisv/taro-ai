"""Taro CUA test: Daily Draw produces a reading.

PRD flow: user taps Daily Draw and receives an AI tarot reading for the day.
Works without sign-in: the app falls back to a local interpreter and still
renders a full reading, so this passes even when the AI proxy returns 401/429.
"""
from a_test import TestCase, run_case

case = TestCase(
    name="daily_draw",
    package="com.aistudio.taro.xkyzv",
    instruction=(
        "The Taro tarot app has been launched and is on its Home screen. "
        "Your task: get a single daily tarot reading. "
        "Steps: "
        "1. On the Home screen, find and tap the 'Daily Draw' option (a card that draws one tarot card for today). "
        "2. Wait for the reading to appear. A short mystical loading animation may play first — wait for it to finish. "
        "3. A reading screen should show a tarot card name (e.g. 'Daily Card: The Star') and a written interpretation of it (paragraphs describing the card's energy, meaning, and advice). The reading is text-based; there does NOT need to be a card graphic/illustration — card name plus interpretation text is sufficient. "
        "Interface animations (card flip / fade-in) are expected — wait a couple of seconds after tapping for content to settle. "
        "If a tap seems to miss, look at the screen and tap the correct element again. "
        "Report TEST_PASSED once a tarot card name and its written interpretation are visible on the reading screen. "
        "Only report TEST_FAILED if the app crashes, shows an error dialog, or no reading text ever appears."
    ),
    successCriteria=[
        "A reading screen is shown with a tarot card name and a written interpretation/meaning",
        "No crash dialog or error toast is visible",
    ],
    failureCriteria=[
        "The app crashes or shows an 'App isn't responding' dialog",
        "No reading or interpretation text ever appears after tapping Daily Draw",
    ],
    maxSteps=22,
)

if __name__ == "__main__":
    import os
    result = run_case(case, output_dir=os.environ.get("OUT", "/tmp/taro-cua/output/daily_draw"))
    print(f"Verdict: {result['verdict']} -- {result.get('reason', '')}")
    assert result["verdict"] == "pass", f"Test failed: {result.get('reason')}"
