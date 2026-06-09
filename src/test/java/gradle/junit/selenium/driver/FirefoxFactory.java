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

    /**
     * Returns FirefoxOptions — used by the interface and internally by createDriver().
     * Keeping this as a private typed method avoids unsafe casting.
     */
    private FirefoxOptions buildFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();

        // Run without a visible browser window — needed in CI/Docker
        boolean headless = ConfigReader.getBoolean("headless")
                || "true".equalsIgnoreCase(System.getenv("CI"));
        if (headless) {
            options.addArguments("-headless");
        }

        // Open in private mode — starts with a clean browser state
        if (ConfigReader.getBoolean("incognito")) {
            options.addArguments("-private");
        }

        // Required flags when running inside a Docker container
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        return options;
    }

    /** Satisfies the BrowserFactory interface — delegates to the typed method. */
    @Override
    public MutableCapabilities buildOptions() {
        return buildFirefoxOptions();
    }

    @Override
    public WebDriver createDriver() {
        // Uses the typed method directly — no unsafe cast needed
        FirefoxOptions options = buildFirefoxOptions();
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
