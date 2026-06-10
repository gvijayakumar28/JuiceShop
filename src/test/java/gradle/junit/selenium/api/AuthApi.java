package gradle.junit.selenium.api;

import gradle.junit.selenium.constants.Endpoints;
import gradle.junit.selenium.model.LoginRequest;
import gradle.junit.selenium.utils.ApiClient;
import gradle.junit.selenium.utils.ResponseValidator;
import io.restassured.response.Response;

/**
 * Handles authentication API calls.
 * Receives an ApiClient configured with a plain (unauthenticated) spec — login does not need a token.
 */
public class AuthApi {

    private final ApiClient client;

    public AuthApi(ApiClient client) {
        this.client = client;
    }

    public String login(String email, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        Response response = client.post(Endpoints.LOGIN, loginRequest);

        ResponseValidator validator = new ResponseValidator(response);
        validator.checkStatusCode(200);
        validator.checkBodyIsNotEmpty();

        return validator.extractString("authentication.token");
    }
}
