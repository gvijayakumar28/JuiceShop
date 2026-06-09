package gradle.junit.selenium.driver;

import gradle.junit.selenium.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Creates a Firefox WebDriver for local or remote execution.
 */
public class FirefoxFactory implements BrowserFactory {

    @Override
    public MutableCapabilities buildOptions() {
        FirefoxOptions options = new FirefoxOptions();

        // Run without a visible browser window — needed in CI/Docker
        boolean headless = ConfigReader.getBoolean("headless")
                || "true".equalsIgnoreCase(System.getenv("CI"));
        if (headless) {
            options.addArguments("-headless");
        }

        // Open in private mode
        if (ConfigReader.getBoolean("incognito")) {
            options.addArguments("-private");
        }

        return options;
    }

    @Override
    public WebDriver createDriver() {
        FirefoxOptions options = (FirefoxOptions) buildOptions();
        String execution = ConfigReader.get("execution", "local");

        if ("remote".equalsIgnoreCase(execution)) {
            String gridUrl = ConfigReader.get("grid.url");
            try {
                return new RemoteWebDriver(new URL(gridUrl), options);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid grid URL: " + gridUrl, e);
            }
        }

        // Local execution — WebDriverManager downloads GeckoDriver automatically
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(options);
    }
}
