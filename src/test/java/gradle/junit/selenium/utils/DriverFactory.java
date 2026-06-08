package gradle.junit.selenium.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe WebDriver factory using ThreadLocal.
 * Each thread (parallel test) gets its own isolated browser instance.
 */
public class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    // ThreadLocal gives each thread its own private WebDriver — no sharing, no conflicts
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static void initDriver() {
        log.info("Starting Chrome browser");
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Run headless when inside CI/Docker (no display available)
        if (Boolean.parseBoolean(System.getenv().getOrDefault("CI", "false"))) {
            log.info("CI environment detected — running Chrome in headless mode");
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driverThread.set(driver);
        log.info("Chrome browser started successfully");
    }

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            log.info("Closing Chrome browser");
            driver.quit();
            driverThread.remove(); // prevent memory leak after thread finishes
            log.info("Chrome browser closed");
        }
    }
}
