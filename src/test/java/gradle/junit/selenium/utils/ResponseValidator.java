package gradle.junit.selenium.utils;

import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseValidator {

    private final Response response;

    public ResponseValidator(Response response) {
        this.response = response;
    }

    // Check the status code is what we expect
    // Example: validator.checkStatusCode(200)
    public void checkStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode,
                "Status code mismatch. Expected: " + expectedStatusCode + ", Got: " + actualStatusCode);
    }

    // Check a specific field in the JSON response body
    // Example: validator.checkBodyContains("status", "success")
    public void checkBodyContains(String fieldPath, String expectedValue) {
        String actualValue = response.jsonPath().getString(fieldPath);
        assertEquals(expectedValue, actualValue,
                "Field '" + fieldPath + "' mismatch. Expected: " + expectedValue + ", Got: " + actualValue);
    }

    // Check the response body is not blank/empty
    public void checkBodyIsNotEmpty() {
        String body = response.getBody().asString();
        assertFalse(body.isEmpty(), "Response body was empty");
    }

    // Check the API responded within a time limit
    // Example: validator.checkResponseTime(3000) means must reply within 3 seconds
    public void checkResponseTime(long maxMilliseconds) {
        long actualTime = response.getTime();
        assertTrue(actualTime < maxMilliseconds,
                "Response was too slow. Expected under: " + maxMilliseconds + "ms, Got: " + actualTime + "ms");
    }

    // Pull out a String value from the response body
    // Example: validator.extractString("authentication.token")
    public String extractString(String fieldPath) {
        return response.jsonPath().getString(fieldPath);
    }

    // Pull out a number value from the response body
    // Example: validator.extractInt("data[0].id")
    public int extractInt(String fieldPath) {
        return response.jsonPath().getInt(fieldPath);
    }
}
