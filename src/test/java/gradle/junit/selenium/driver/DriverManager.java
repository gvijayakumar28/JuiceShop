package gradle.junit.selenium.driver;

import org.openqa.selenium.WebDriver;

/**
 * Manages WebDriver instances using ThreadLocal.
 * Each thread (parallel test) gets its own private WebDriver — no sharing between threads.
 *
 * Responsibilities:
 *   - setDriver()  — store a WebDriver for the current thread
 *   - getDriver()  — retrieve the WebDriver for the current thread
 *   - quitDriver() — close the browser and remove from ThreadLocal
 */
public class DriverManager {

    // ThreadLocal gives each thread its own isolated WebDriver instance
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    // Private constructor — never instantiate this class
    private DriverManager() {}

    /** Stores the WebDriver for the current thread. */
    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    /** Returns the WebDriver for the current thread. */
    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    /** Closes the browser and removes the driver from ThreadLocal to prevent memory leaks. */
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
