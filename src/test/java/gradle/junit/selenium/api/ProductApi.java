package gradle.junit.selenium.api;

import gradle.junit.selenium.constants.Endpoints;
import gradle.junit.selenium.model.ReviewRequest;
import gradle.junit.selenium.utils.ApiClient;
import gradle.junit.selenium.utils.ResponseValidator;
import io.restassured.response.Response;

public class ProductApi {

    private final ApiClient client = new ApiClient();

    public int getFirstProductId(String searchQuery) {
        Response response = client.get(Endpoints.PRODUCT_SEARCH + "?q=" + searchQuery);

        ResponseValidator validator = new ResponseValidator(response);
        validator.checkStatusCode(200);
        validator.checkBodyContains("status", "success");
        validator.checkResponseTime(3000);

        return validator.extractInt("data[0].id");
    }

    public void postReview(int productId, String message, String author, String token) {
        ReviewRequest reviewRequest = new ReviewRequest(message, author);

        Response response = client.put(Endpoints.productReviews(productId), reviewRequest, token);

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
