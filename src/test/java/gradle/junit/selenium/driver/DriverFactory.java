package gradle.junit.selenium.driver;

/**
 * Selects and returns the correct BrowserFactory based on the browser name.
 *
 * Responsibilities:
 *   - Read browser name
 *   - Return the matching BrowserFactory implementation
 *
 * NOT responsible for:
 *   - Creating WebDrivers directly
 *   - Managing ThreadLocal
 *   - Configuring browser options (that is each factory's job)
 */
public class DriverFactory {

    // Private constructor — this class should never be instantiated
    private DriverFactory() {}

    /**
     * Returns the BrowserFactory for the given browser name.
     * Example: DriverFactory.getFactory("chrome") returns a ChromeFactory
     */
    public static BrowserFactory getFactory(String browser) {
        return switch (browser.toLowerCase().trim()) {
            case "chrome"  -> new ChromeFactory();
            case "firefox" -> new FirefoxFactory();
            default -> throw new IllegalArgumentException(
                "Unsupported browser: '" + browser + "'. Supported values: chrome, firefox"
            );
        };
    }
}
