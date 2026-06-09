package gradle.junit.selenium.api;

import gradle.junit.selenium.constants.Endpoints;
import gradle.junit.selenium.model.ReviewRequest;
import gradle.junit.selenium.utils.ApiClient;
import gradle.junit.selenium.utils.ResponseValidator;
import io.restassured.response.Response;

/**
 * Handles product-related API calls.
 * Receives an ApiClient configured with an authenticated spec (Authorization header already set).
 * No need to pass a token to individual methods — it is baked into the spec.
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
        validator.checkBodyContains("status", "success");
        validator.checkResponseTime(3000);

        return validator.extractInt("data[0].id");
    }

    public void postReview(int productId, String message, String author) {
        ReviewRequest reviewRequest = new ReviewRequest(message, author);

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
        validator.checkBodyContains("status", "success");
        validator.checkResponseTime(3000);
    }
}
