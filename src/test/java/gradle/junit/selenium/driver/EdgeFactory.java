package gradle.junit.selenium.driver;

import gradle.junit.selenium.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Creates an Edge WebDriver for local or remote execution.
 */
public class EdgeFactory implements BrowserFactory {

    @Override
    public MutableCapabilities buildOptions() {
        EdgeOptions options = new EdgeOptions();

        // Run without a visible browser window — needed in CI/Docker
        boolean headless = ConfigReader.getBoolean("headless")
                || "true".equalsIgnoreCase(System.getenv("CI"));
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        // Open in InPrivate mode
        if (ConfigReader.getBoolean("incognito")) {
            options.addArguments("--inprivate");
        }

        // Required flags when running inside a Docker container
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        return options;
    }

    @Override
    public WebDriver createDriver() {
        EdgeOptions options = (EdgeOptions) buildOptions();
        String execution = ConfigReader.get("execution", "local");

        if ("remote".equalsIgnoreCase(execution)) {
            String gridUrl = ConfigReader.get("grid.url");
            try {
                return new RemoteWebDriver(new URL(gridUrl), options);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid grid URL: " + gridUrl, e);
            }
        }

        // Local execution — WebDriverManager downloads EdgeDriver automatically
        WebDriverManager.edgedriver().setup();
        return new EdgeDriver(options);
    }
}
