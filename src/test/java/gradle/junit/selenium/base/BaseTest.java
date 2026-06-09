package gradle.junit.selenium.base;

import gradle.junit.selenium.driver.BrowserFactory;
import gradle.junit.selenium.driver.DriverFactory;
import gradle.junit.selenium.driver.DriverManager;
import gradle.junit.selenium.utils.ConfigReader;
import gradle.junit.selenium.utils.ScreenshotOnFailureExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all test classes.
 * Sets up and tears down the WebDriver once per test class using @BeforeAll / @AfterAll.
 * ScreenshotOnFailureExtension automatically captures screenshots on test failure.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ScreenshotOnFailureExtension.class)
public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeAll
    void initDriver() {
        log.info("========== TEST SUITE STARTING: {} ==========", getClass().getSimpleName());

        // Read browser from config.properties (default: chrome)
        String browser = ConfigReader.get("browser", "chrome");
        log.info("Browser: {} | Execution: {}", browser, ConfigReader.get("execution", "local"));

        // Get the correct factory for the browser, then create the driver
        BrowserFactory factory = DriverFactory.getFactory(browser);
        WebDriver driver = factory.createDriver();

        // Store driver in DriverManager so all page objects can access it
        DriverManager.setDriver(driver);
    }

    @AfterAll
    void quitDriver() {
        DriverManager.quitDriver();
        log.info("========== TEST SUITE FINISHED: {} ==========", getClass().getSimpleName());
    }
}
