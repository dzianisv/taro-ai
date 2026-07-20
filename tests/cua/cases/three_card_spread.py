"""Taro CUA test: 3-Card spread (Past / Present / Future).

PRD flow: user draws a classic 3-card spread and receives an interpretation
covering Past, Present, and Future. Works offline via the local interpreter.
"""
from a_test import TestCase, run_case

case = TestCase(
    name="three_card_spread",
    package="com.aistudio.taro.xkyzv",
    instruction=(
        "The Taro tarot app is on its Home screen. "
        "Your task: draw a 3-card tarot spread. "
        "Steps: "
        "1. On the Home screen, find and tap the three-card spread option "
        "(labelled with 'Past, Present, and Future' or similar — it draws three cards). "
        "2. Wait for the cards to deal in; a staggered card animation may play — let it finish. "
        "3. A reading screen should show the three drawn card names and an interpretation organised into Past, Present, and Future sections. The reading is text-based; card graphics are NOT required — three card names plus past/present/future interpretation text is sufficient. "
        "Wait a couple of seconds after tapping for animated content to settle. "
        "Report TEST_PASSED once a three-part (past/present/future) reading with card names and interpretation text is visible. "
        "Only report TEST_FAILED if the app crashes or no reading text ever appears."
    ),
    successCriteria=[
        "A reading screen shows three card names and an interpretation covering past, present, and future",
        "No crash dialog or error toast is visible",
    ],
    failureCriteria=[
        "The app crashes or shows an ANR dialog",
        "No reading or interpretation appears after selecting the 3-card spread",
    ],
    maxSteps=22,
)

if __name__ == "__main__":
    import os
    result = run_case(case, output_dir=os.environ.get("OUT", "/tmp/taro-cua/output/three_card_spread"))
    print(f"Verdict: {result['verdict']} -- {result.get('reason', '')}")
    assert result["verdict"] == "pass", f"Test failed: {result.get('reason')}"
