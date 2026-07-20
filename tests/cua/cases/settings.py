"""Taro CUA test: Oracle Settings dialog.

Verifies the settings entry point (gear icon) opens the configuration dialog
where the secure gateway URL and optional keys are shown. Low-risk smoke that
also yields a clean settings screenshot for the store listing.
"""
from a_test import TestCase, run_case

case = TestCase(
    name="settings",
    package="com.aistudio.taro.xkyzv",
    instruction=(
        "The Taro tarot app is on its Home screen. "
        "Your task: open the settings. "
        "Steps: "
        "1. Tap the settings gear icon (top of the Home screen). "
        "2. A settings dialog titled 'Oracle Settings' (or similar) should appear with configuration fields "
        "such as an API key, a gateway URL, and a Google client ID. "
        "Report TEST_PASSED once the settings dialog with configuration fields is visible. "
        "Only report TEST_FAILED if the settings never open or the app crashes."
    ),
    successCriteria=[
        "A settings dialog/screen is shown with configuration fields (gateway URL / API key)",
        "No crash occurs",
    ],
    failureCriteria=[
        "The settings dialog never appears",
        "The app crashes when opening settings",
    ],
    maxSteps=14,
)

if __name__ == "__main__":
    import os
    result = run_case(case, output_dir=os.environ.get("OUT", "/tmp/taro-cua/output/settings"))
    print(f"Verdict: {result['verdict']} -- {result.get('reason', '')}")
    assert result["verdict"] == "pass", f"Test failed: {result.get('reason')}"
