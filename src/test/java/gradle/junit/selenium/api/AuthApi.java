package gradle.junit.selenium.api;

import gradle.junit.selenium.constants.Endpoints;
import gradle.junit.selenium.model.LoginRequest;
import gradle.junit.selenium.utils.ApiClient;
import gradle.junit.selenium.utils.ResponseValidator;
import io.restassured.response.Response;

public class AuthApi {

    private final ApiClient client = new ApiClient();

    public String login(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        Response response = client.post(Endpoints.LOGIN, loginRequest);

        ResponseValidator validator = new ResponseValidator(response);
        validator.checkStatusCode(200);
        validator.checkBodyIsNotEmpty();

        return validator.extractString("authentication.token");
    }
}
