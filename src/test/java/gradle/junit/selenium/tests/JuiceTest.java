package gradle.junit.selenium.tests;

import gradle.junit.selenium.api.ProductApi;
import gradle.junit.selenium.base.BaseTest;
import gradle.junit.selenium.pages.LoginPage;
import gradle.junit.selenium.pages.ProductListPage;
import gradle.junit.selenium.pages.ReviewPage;
import gradle.junit.selenium.utils.ApiClient;
import gradle.junit.selenium.utils.ConfigReader;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Epic("JuiceShop Application")
@Feature("Product Reviews")
public class JuiceTest extends BaseTest {

    @Test(groups = {"ui", "smoke"}, description = "Login and post product review via UI")
    @Story("Post review via browser")
    @Description("Login to JuiceShop, open a product, post a review and verify it is visible on screen")
    public void loginAndPostProductReviewViaUi() {
        String reviewText = "Great product, highly recommended!";

        // Step 1 - Open login page and dismiss popups
        LoginPage loginPage = new LoginPage();
        loginPage.open(BASE_URL);
        loginPage.dismissPopups();

        // Step 2 - Login and land on product list page
        ProductListPage productListPage = loginPage.loginAs(customer.getEmail(), customer.getPassword());

        // Step 3 - Open the first product
        ReviewPage reviewPage = productListPage.selectSearchProduct("Strawberry Juice");

        // Step 4 - Submit a review
        reviewPage.submitReview(reviewText);

        // Step 5 - Expand the reviews section
        reviewPage.expandReviews();

        // Step 6 - Assert the review is visible
        boolean isReviewPosted = reviewPage.isReviewVisible(reviewText);
        assertTrue(isReviewPosted, "Review should be visible after posting");
    }

    @Test(groups = {"ui", "smoke"}, description = "Search product review via UI")
    @Story("Search Product via browser")
    @Description("Search product")
    public void searchProductiaUi() {
        String reviewText = "Great product, highly recommended!";

        // Step 1 - Open login page and dismiss popups
        LoginPage loginPage = new LoginPage();
        loginPage.open(BASE_URL);
        loginPage.dismissPopups();

        // Step 2 - Login and land on product list page
        ProductListPage productListPage = loginPage.loginAs(customer.getEmail(), customer.getPassword());

        // Step 3 - Search product
        productListPage.searchProduct("Strawberry Juice");
        boolean isProductDisplayed = productListPage.isProductDisplayed("Strawberry Juice");
        Assert.assertTrue(isProductDisplayed, "Product should be displayed");
    }

    @Test(groups = {"api", "smoke"}, description = "Login and post product review via API")
    @Story("Post review via API")
    @Description("Login via API, search for a product, post a review and verify it is saved in the database")
    public void loginAndPostProductReviewViaApi() {
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
