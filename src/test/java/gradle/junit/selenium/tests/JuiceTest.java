package gradle.junit.selenium.tests;

import gradle.junit.selenium.api.AuthApi;
import gradle.junit.selenium.api.ProductApi;
import gradle.junit.selenium.base.BaseTest;
import gradle.junit.selenium.pages.LoginPage;
import gradle.junit.selenium.pages.ProductListPage;
import gradle.junit.selenium.pages.ReviewPage;
import gradle.junit.selenium.utils.ApiClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("JuiceShop Application")
@Feature("Product Reviews")
class JuiceTest extends BaseTest {

    private static final String BASE_URL = System.getenv().getOrDefault("APP_URL", "http://localhost:3000");

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
        // authSpec already has the token — no manual login needed
        ApiClient authClient = new ApiClient(authSpec);
        ProductApi productApi = new ProductApi(authClient);

        // Step 1 - Get a product id to post a review on
        int productId = productApi.getFirstProductId("apple");

        // Step 2 - Post the review
        productApi.postReview(productId, "This is a great product!", customer.getEmail());

        // Step 3 - Verify the review was saved
        productApi.verifyReviewExists(productId, customer.getEmail());
    }
}
