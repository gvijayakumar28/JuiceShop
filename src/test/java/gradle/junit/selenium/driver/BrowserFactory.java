package gradle.junit.selenium.driver;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;

/**
 * Contract that every browser implementation must follow.
 * Each browser (Chrome, Firefox) provides its own version of these two methods.
 */
public interface BrowserFactory {

    /** Creates and returns a ready-to-use WebDriver instance. */
    WebDriver createDriver();

    /** Builds and returns browser-specific options (headless, incognito, etc). */
    MutableCapabilities buildOptions();
}
