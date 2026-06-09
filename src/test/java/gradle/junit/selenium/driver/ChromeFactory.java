package gradle.junit.selenium.driver;

import gradle.junit.selenium.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Creates a Chrome WebDriver for local or remote execution.
 */
public class ChromeFactory implements BrowserFactory {

    /**
     * Returns ChromeOptions — used by the interface and internally by createDriver().
     * Keeping this as a private typed method avoids unsafe casting.
     */
    private ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // Run without a visible browser window — needed in CI/Docker
        boolean headless = ConfigReader.getBoolean("headless")
                || "true".equalsIgnoreCase(System.getenv("CI"));
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        // Open in private/incognito mode — starts with a clean browser state
        if (ConfigReader.getBoolean("incognito")) {
            options.addArguments("--incognito");
        }

        // Required flags when running inside a Docker container
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        return options;
    }

    /** Satisfies the BrowserFactory interface — delegates to the typed method. */
    @Override
    public MutableCapabilities buildOptions() {
        return buildChromeOptions();
    }

    @Override
    public WebDriver createDriver() {
        // Uses the typed method directly — no unsafe cast needed
        ChromeOptions options = buildChromeOptions();
        String execution = ConfigReader.get("execution", "local");

        if ("remote".equalsIgnoreCase(execution)) {
            String gridUrl = ConfigReader.get("grid.url");
            try {
                return new RemoteWebDriver(new URL(gridUrl), options);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid grid URL: " + gridUrl, e);
            }
        }

        // Local execution — WebDriverManager downloads ChromeDriver automatically
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }
}
