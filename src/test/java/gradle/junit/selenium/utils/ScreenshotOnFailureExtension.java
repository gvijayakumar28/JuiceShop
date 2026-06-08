package gradle.junit.selenium.utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

// TestWatcher listens to every test — pass, fail, skip, abort
// We only care about failures — take screenshot when a test fails
public class ScreenshotOnFailureExtension implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        // Get the test method name — e.g. "loginAndPostProductReviewViaUi"
        String testName = context.getDisplayName();

        // Take screenshot and attach to Allure report
        ScreenshotUtil.takeScreenshot("FAILED - " + testName);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        // Test passed — no screenshot needed
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        // Test was skipped — no screenshot needed
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        // Test was aborted — take screenshot as well
        String testName = context.getDisplayName();
        ScreenshotUtil.takeScreenshot("ABORTED - " + testName);
    }
}
