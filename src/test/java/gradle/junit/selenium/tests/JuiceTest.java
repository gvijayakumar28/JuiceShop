package gradle.junit.selenium.tests;

import gradle.junit.selenium.api.AuthApi;
import gradle.junit.selenium.api.ProductApi;
import gradle.junit.selenium.base.BaseTest;
import gradle.junit.selenium.model.Customer;
import gradle.junit.selenium.pages.LoginPage;
import gradle.junit.selenium.pages.ProductListPage;
import gradle.junit.selenium.pages.ReviewPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("JuiceShop Application")
@Feature("Product Reviews")
class JuiceTest extends BaseTest {

    // Read APP_URL from environment — uses host.docker.internal in CI, localhost locally
    private static final String BASE_URL = System.getenv().getOrDefault("APP_URL", "http://localhost:3000");

    Customer customer;

    @BeforeAll
    void setupCustomer() {
        customer = new Customer.Builder()
                .setEmail("gvijayakumarganesan92@gmail.com")
                .setPassword("Automation@26")
                .setSecurityAnswer("605601")
                .build();
    }

    @Test
    @Tag("ui")
    @Tag("smoke")
    @DisplayName("Login and post product review via UI")
    @Story("Post review via browser")
    @Description("Login to JuiceShop, open a product, post a review and verify it is visible on screen")
    void loginAndPostProductReviewViaUi() {
        String reviewText = "Great product, highly recommended!";

        // Step 1 - Open login page and dismiss popups
        LoginPage loginPage = new LoginPage();
        loginPage.open(BASE_URL);
        loginPage.dismissPopups();

        // Step 2 - Login and land on product list page
        ProductListPage productListPage = loginPage.loginAs(customer.getEmail(), customer.getPassword());

        // Step 3 - Open the first product
        ReviewPage reviewPage = productListPage.openFirstProduct();

        // Step 4 - Submit a review
        reviewPage.submitReview(reviewText);

        // Step 5 - Expand the reviews section
        reviewPage.expandReviews();

        // Step 6 - Assert the review is visible
        boolean isReviewPosted = reviewPage.isReviewVisible(reviewText);
        assertTrue(isReviewPosted, "Review should be visible after posting");
    }

    @Test
    @Tag("api")
    @Tag("smoke")
    @DisplayName("Login and post product review via API")
    @Story("Post review via API")
    @Description("Login via API, search for a product, post a review and verify it is saved in the database")
    void loginAndPostProductReviewViaApi() {
        AuthApi authApi = new AuthApi();
        ProductApi productApi = new ProductApi();

        // Step 1 - Login and get token
        String token = authApi.login(customer.getEmail(), customer.getPassword());
        customer.saveToken(token);

        // Step 2 - Get a product id to post a review on
        int productId = productApi.getFirstProductId("apple");

        // Step 3 - Post the review
        productApi.postReview(productId, "This is a great product!", customer.getEmail(), customer.getToken());

        // Step 4 - Verify the review was saved
        productApi.verifyReviewExists(productId, customer.getEmail());
    }
}
