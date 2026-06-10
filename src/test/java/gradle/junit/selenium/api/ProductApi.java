package gradle.junit.selenium.api;

import gradle.junit.selenium.constants.Endpoints;
import gradle.junit.selenium.model.ProductSearchResponse;
import gradle.junit.selenium.model.ReviewRequest;
import gradle.junit.selenium.model.ReviewsResponse;
import gradle.junit.selenium.utils.ApiClient;
import gradle.junit.selenium.utils.ResponseValidator;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Handles product-related API calls.
 * Receives an ApiClient configured with an authenticated spec (Authorization header already set).
 * Uses POJO classes for both request bodies and response validation.
 */
public class ProductApi {

    private final ApiClient client;

    public ProductApi(ApiClient client) {
        this.client = client;
    }

    public int getFirstProductId(String searchQuery) {
        Response response = client.get(Endpoints.PRODUCT_SEARCH + "?q=" + searchQuery);

        ResponseValidator validator = new ResponseValidator(response);
        validator.checkStatusCode(200);
        validator.checkResponseTime(3000);

        // Deserialize JSON into typed POJO and validate through getters
        ProductSearchResponse searchResponse = response.as(ProductSearchResponse.class);
        assertEquals("success", searchResponse.getStatus(), "Search response status should be success");
        assertFalse(searchResponse.getData().isEmpty(), "No products found for query: " + searchQuery);

        return searchResponse.getData().get(0).getId();
    }

    public void postReview(int productId, String message, String author) {
        // Request body as POJO
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setMessage(message);
        reviewRequest.setAuthor(author);

        // Token is already in the spec — no need to pass it here
        Response response = client.put(Endpoints.productReviews(productId), reviewRequest);

        ResponseValidator validator = new ResponseValidator(response);
        validator.checkStatusCode(201);
        validator.checkBodyIsNotEmpty();
    }

    public void verifyReviewExists(int productId, String authorEmail) {
        Response response = client.get(Endpoints.productReviews(productId));

        ResponseValidator validator = new ResponseValidator(response);
        validator.checkStatusCode(200);
        validator.checkResponseTime(3000);

        // Deserialize JSON into typed POJO
        ReviewsResponse reviewsResponse = response.as(ReviewsResponse.class);
        assertEquals("success", reviewsResponse.getStatus(), "Reviews response status should be success");

        // Check at least one review belongs to our author — typed access, no JsonPath strings
        boolean reviewFound = reviewsResponse.getData().stream()
                .anyMatch(review -> authorEmail.equals(review.getAuthor()));

        assertTrue(reviewFound, "No review found for author: " + authorEmail);
    }
}
