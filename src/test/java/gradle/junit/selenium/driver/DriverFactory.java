package gradle.junit.selenium.driver;

public class DriverFactory {

    // Private constructor — this class should never be instantiated
    private DriverFactory() {}

    /**
     * Returns the BrowserFactory for the given browser name.
     * Example: DriverFactory.getFactory("chrome") returns a ChromeFactory
     */
    public static BrowserFactory getFactory(String browser) {
        String browserName = browser.toLowerCase().trim();

        switch (browserName) {
            case "chrome":
                return new ChromeFactory();

            case "firefox":
                return new FirefoxFactory();

            default:
                throw new IllegalArgumentException(String.format("Unsupported browser: '%s'. Supported values: chrome, firefox", browser));
        }
    }
}
