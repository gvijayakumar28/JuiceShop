package gradle.junit.selenium.utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

/**
 * Common HTTP client with reusable methods for GET, POST, PUT, PATCH, DELETE.
 * All API classes use this instead of writing raw RestAssured calls each time.
 */
public class ApiClient {

    private static final Logger log = LoggerFactory.getLogger(ApiClient.class);

    // Read APP_URL from environment — uses host.docker.internal in CI, localhost locally
    private static final String BASE_URL = System.getenv().getOrDefault("APP_URL", "http://localhost:3000");
    private static final String CONTENT_TYPE = "application/json";

    // AllureRestAssured filter — logs every request and response into the Allure report
    private static final AllureRestAssured ALLURE_FILTER = new AllureRestAssured();

    // -------------------------------------------------------
    // GET — fetch data, no request body needed
    // Example: client.get("/rest/products/search?q=apple")
    // -------------------------------------------------------
    public Response get(String endpoint) {
        log.info("GET {}", BASE_URL + endpoint);
        Response response = given()
                .filter(ALLURE_FILTER)
                .header("Content-Type", CONTENT_TYPE)
                .when()
                .get(BASE_URL + endpoint)
                .andReturn();
        log.info("GET {} -> status {}", endpoint, response.getStatusCode());
        return response;
    }

    // -------------------------------------------------------
    // POST — send a POJO body (RestAssured auto-converts to JSON)
    // Example: client.post("/rest/user/login", new LoginRequest(email, password))
    // -------------------------------------------------------
    public Response post(String endpoint, Object body) {
        log.info("POST {}", BASE_URL + endpoint);
        Response response = given()
                .filter(ALLURE_FILTER)
                .header("Content-Type", CONTENT_TYPE)
                .body(body)
                .when()
                .post(BASE_URL + endpoint)
                .andReturn();
        log.info("POST {} -> status {}", endpoint, response.getStatusCode());
        return response;
    }

    // -------------------------------------------------------
    // PUT — send a POJO body with auth token
    // Example: client.put("/rest/products/1/reviews", new ReviewRequest(...), token)
    // -------------------------------------------------------
    public Response put(String endpoint, Object body, String token) {
        log.info("PUT {}", BASE_URL + endpoint);
        Response response = given()
                .filter(ALLURE_FILTER)
                .header("Content-Type", CONTENT_TYPE)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .put(BASE_URL + endpoint)
                .andReturn();
        log.info("PUT {} -> status {}", endpoint, response.getStatusCode());
        return response;
    }

    // -------------------------------------------------------
    // PATCH — send a POJO body with auth token
    // Example: client.patch("/rest/products/1", new UpdateRequest(...), token)
    // -------------------------------------------------------
    public Response patch(String endpoint, Object body, String token) {
        log.info("PATCH {}", BASE_URL + endpoint);
        Response response = given()
                .filter(ALLURE_FILTER)
                .header("Content-Type", CONTENT_TYPE)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .patch(BASE_URL + endpoint)
                .andReturn();
        log.info("PATCH {} -> status {}", endpoint, response.getStatusCode());
        return response;
    }

    // -------------------------------------------------------
    // DELETE — remove a resource (auth required)
    // Example: client.delete("/rest/products/1", token)
    // -------------------------------------------------------
    public Response delete(String endpoint, String token) {
        log.info("DELETE {}", BASE_URL + endpoint);
        Response response = given()
                .filter(ALLURE_FILTER)
                .header("Content-Type", CONTENT_TYPE)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(BASE_URL + endpoint)
                .andReturn();
        log.info("DELETE {} -> status {}", endpoint, response.getStatusCode());
        return response;
    }
}
