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

    @Override
    public MutableCapabilities buildOptions() {
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

    @Override
    public WebDriver createDriver() {
        ChromeOptions options = (ChromeOptions) buildOptions();
        String execution = ConfigReader.get("execution", "local");

        if ("remote".equalsIgnoreCase(execution)) {
            // Connect to Selenium Grid / BrowserStack / LambdaTest
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
