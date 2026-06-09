package gradle.junit.selenium.base;

import gradle.junit.selenium.driver.BrowserFactory;
import gradle.junit.selenium.driver.DriverFactory;
import gradle.junit.selenium.driver.DriverManager;
import gradle.junit.selenium.model.Customer;
import gradle.junit.selenium.utils.ConfigReader;
import gradle.junit.selenium.utils.ScreenshotOnFailureExtension;
import gradle.junit.selenium.utils.TokenManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all test classes.
 *
 * Responsibilities:
 *   - Start and stop the browser (WebDriver lifecycle)
 *   - Login once and set up RequestSpecifications for API calls
 *   - Expose customer, baseSpec, and authSpec to all test classes
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(ScreenshotOnFailureExtension.class)
public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    private static final String BASE_URL = System.getenv().getOrDefault("APP_URL", "http://localhost:3000");

    // Available to all test classes that extend BaseTest
    protected Customer customer;
    protected RequestSpecification baseSpec;   // unauthenticated — for calls that don't need a token
    protected RequestSpecification authSpec;   // authenticated  — token already baked in

    @BeforeAll
    void setup() {
        log.info("========== TEST SUITE STARTING: {} ==========", getClass().getSimpleName());

        setupBrowser();
        setupCustomer();
        setupApiSpecs();
    }

    @AfterAll
    void teardown() {
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
