package gradle.junit.selenium.constants;

public class Endpoints {

    // Auth endpoints
    public static final String LOGIN = "/rest/user/login";

    // Product endpoints
    public static final String PRODUCT_SEARCH = "/rest/products/search";
    public static final String PRODUCT_REVIEWS = "/rest/products/%d/reviews";

    // Helper method to build the product reviews endpoint with actual product id
    // Example: Endpoints.productReviews(1) returns "/rest/products/1/reviews"
    public static String productReviews(int productId) {
        return String.format(PRODUCT_REVIEWS, productId);
    }
}
