package gradle.junit.selenium.utils;

import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener — automatically takes a screenshot when a test fails or is skipped.
 *
 * ITestListener gives callbacks for every test event: start, success, failure, skip.
 * We only care about failures — the other methods have empty default implementations.
 *
 * Registered on BaseTest via @Listeners, so every test class inherits it automatically.
 */
public class ScreenshotOnFailureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        // Test method name — e.g. "loginAndPostProductReviewViaUi"
        String testName = result.getMethod().getMethodName();

        // Take screenshot and attach to Allure report
        ScreenshotUtil.takeScreenshot("FAILED - " + testName);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // A skipped test (e.g. dependsOnMethods failed) — capture state as well
        String testName = result.getMethod().getMethodName();
        ScreenshotUtil.takeScreenshot("SKIPPED - " + testName);
    }
}
