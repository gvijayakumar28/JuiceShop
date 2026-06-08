package gradle.junit.selenium.base;

import gradle.junit.selenium.utils.DriverFactory;
import gradle.junit.selenium.utils.ScreenshotOnFailureExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all test classes.
 * Driver is created ONCE before all tests in the class and closed ONCE after all tests finish.
 * ScreenshotOnFailureExtension automatically takes a screenshot whenever a test fails.
 *
 * @TestInstance(PER_CLASS) allows @BeforeAll and @AfterAll to be non-static,
 * which is required for ThreadLocal-based DriverFactory to work correctly.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ScreenshotOnFailureExtension.class)
public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeAll
    void initDriver() {
        log.info("========== TEST SUITE STARTING: {} ==========", getClass().getSimpleName());
        DriverFactory.initDriver();
    }

    @AfterAll
    void quitDriver() {
        DriverFactory.quitDriver();
        log.info("========== TEST SUITE FINISHED: {} ==========", getClass().getSimpleName());
    }
}
