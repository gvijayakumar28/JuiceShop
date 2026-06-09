package gradle.junit.selenium.utils;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

/**
 * Common HTTP client wrapping RestAssured.
 * Accepts a pre-configured RequestSpecification (base URL, headers, auth token, filters).
 * All API classes use this instead of writing raw RestAssured calls each time.
 *
 * Token management is handled externally — this client just executes HTTP calls.
 */
public class ApiClient {

    private static final Logger log = LoggerFactory.getLogger(ApiClient.class);

    // The spec carries base URL, content type, Authorization header, and Allure filter
    private final RequestSpecification spec;

    public ApiClient(RequestSpecification spec) {
        this.spec = spec;
    }

    // -------------------------------------------------------
    // GET — fetch data, no request body needed
    // -------------------------------------------------------
    public Response get(String endpoint) {
        log.info("GET {}", endpoint);
        Response response = given()
                .spec(spec)
                .when()
                .get(endpoint)
                .andReturn();
        log.info("GET {} -> status {}", endpoint, response.getStatusCode());
        return response;
    }

    // -------------------------------------------------------
    // POST — send a body (POJO auto-converted to JSON by RestAssured)
    // -------------------------------------------------------
    public Response post(String endpoint, Object body) {
        log.info("POST {}", endpoint);
        Response response = given()
                .spec(spec)
                .body(body)
                .when()
                .post(endpoint)
                .andReturn();
        log.info("POST {} -> status {}", endpoint, response.getStatusCode());
        return response;
    }

    // -------------------------------------------------------
    // PUT — update a resource with a body
    // -------------------------------------------------------
    public Response put(String endpoint, Object body) {
        log.info("PUT {}", endpoint);
        Response response = given()
                .spec(spec)
                .body(body)
                .when()
                .put(endpoint)
                .andReturn();
        log.info("PUT {} -> status {}", endpoint, response.getStatusCode());
        return response;
    }

    // -------------------------------------------------------
    // PATCH — partial update with a body
    // -------------------------------------------------------
    public Response patch(String endpoint, Object body) {
        log.info("PATCH {}", endpoint);
        Response response = given()
                .spec(spec)
                .body(body)
                .when()
                .patch(endpoint)
                .andReturn();
        log.info("PATCH {} -> status {}", endpoint, response.getStatusCode());
        return response;
    }

    // -------------------------------------------------------
    // DELETE — remove a resource
    // -------------------------------------------------------
    public Response delete(String endpoint) {
        log.info("DELETE {}", endpoint);
        Response response = given()
                .spec(spec)
                .when()
                .delete(endpoint)
                .andReturn();
        log.info("DELETE {} -> status {}", endpoint, response.getStatusCode());
        return response;
    }
}
