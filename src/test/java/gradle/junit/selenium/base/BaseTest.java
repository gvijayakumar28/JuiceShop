package gradle.junit.selenium.base;

import gradle.junit.selenium.driver.BrowserFactory;
import gradle.junit.selenium.driver.DriverFactory;
import gradle.junit.selenium.driver.DriverManager;
import gradle.junit.selenium.model.Customer;
import gradle.junit.selenium.utils.ConfigReader;
import gradle.junit.selenium.utils.ScreenshotOnFailureListener;
import gradle.junit.selenium.utils.TokenManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

/**
 * Base class for all test classes.
 *
 * Responsibilities:
 *   - Start and stop the browser
 *   - Login once and set up RequestSpecifications for API calls
 *   - Expose customer, baseSpec, and authSpec to all test classes
 *
 * TestNG notes:
 *   - @BeforeClass/@AfterClass run once per class and are non-static by default
 *   - @Listeners registers the screenshot-on-failure listener for all subclasses
 */
@Listeners(ScreenshotOnFailureListener.class)
public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    // Jenkins sets APP_URL env var; local runs use config.properties fallback
    protected static final String BASE_URL = System.getenv().getOrDefault(
            "APP_URL", ConfigReader.get("APP_URL", "http://localhost:3000"));

    // Available to all test classes that extend BaseTest
    protected Customer customer;
    protected RequestSpecification baseSpec;   // unauthenticated — for calls that don't need a token
    protected RequestSpecification authSpec;   // authenticated  — token already baked in

    @BeforeClass(alwaysRun = true)
    public void setup() {
        log.info("========== TEST SUITE STARTING: {} ==========", getClass().getSimpleName());

        setupBrowser();
        setupCustomer();
        setupApiSpecs();
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        DriverManager.quitDriver();
        log.info("========== TEST SUITE FINISHED: {} ==========", getClass().getSimpleName());
    }

    // -------------------------------------------------------
    // Private setup methods
    // -------------------------------------------------------

    private void setupBrowser() {
        String browser = ConfigReader.get("browser", "chrome");
        log.info("Browser: {} | Execution: {}", browser, ConfigReader.get("execution", "local"));

        BrowserFactory factory = DriverFactory.getFactory(browser);
        WebDriver driver = factory.createDriver();
        DriverManager.setDriver(driver);
    }

    private void setupCustomer() {
        // Read credentials from config.properties — not hardcoded in tests
        customer = new Customer.Builder()
                .setEmail(ConfigReader.get("user.email"))
                .setPassword(ConfigReader.get("user.password"))
                .setSecurityAnswer(ConfigReader.get("user.security.answer"))
                .build();
    }

    private void setupApiSpecs() {
        // Base spec — no token, used for unauthenticated calls like login
        baseSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();

        // Login once — TokenManager caches token and auto-refreshes if expired
        String token = TokenManager.getToken(customer.getEmail(), customer.getPassword());
        customer.saveToken(token);

        // Auth spec — token baked in, used for all authenticated calls
        authSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .addFilter(new AllureRestAssured())
                .build();

        log.info("API specs configured — token obtained and ready");
    }
}
