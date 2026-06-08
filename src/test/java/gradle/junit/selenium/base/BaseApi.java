package gradle.junit.selenium.base;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseApi {

    protected static final String BASE_URL = "http://localhost:3000";

    protected RequestSpecification baseRequest() {
        return given()
                .header("Content-Type", "application/json");
    }

    protected RequestSpecification authenticatedRequest(String token) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token);
    }
}
