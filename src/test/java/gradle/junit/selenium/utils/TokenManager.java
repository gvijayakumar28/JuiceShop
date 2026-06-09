package gradle.junit.selenium.utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manages the authentication token lifecycle.
 *
 * - Logs in once and caches the token.
 * - Automatically checks JWT expiry before each use.
 * - Re-logs in silently if the token is expired or missing.
 *
 * This means tests never worry about token management — they just call getToken().
 */
public class TokenManager {

    private static final Logger log = LoggerFactory.getLogger(TokenManager.class);

    // Cached token and its expiry time (epoch seconds from JWT payload)
    private static String cachedToken;
    private static long expiryEpochSeconds = 0;

    // Private constructor — this is a utility class, never instantiate it
    private TokenManager() {}

    /**
     * Returns a valid token.
     * If no token exists or the token is about to expire, it logs in again automatically.
     */
    public static String getToken(String email, String password) {
        if (cachedToken == null || isExpired()) {
            log.info("Token missing or expired — logging in to get a fresh token");
            refresh(email, password);
        } else {
            log.info("Reusing existing token (still valid)");
        }
        return cachedToken;
    }

    // -------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------

    private static boolean isExpired() {
        long now = System.currentTimeMillis() / 1000;
        // Treat as expired 60 seconds before actual expiry — safety buffer
        boolean expired = now >= expiryEpochSeconds - 60;
        if (expired) {
            log.info("Token is expired or about to expire — will refresh");
        }
        return expired;
    }

    private static void refresh(String email, String password) {
        // Build a plain spec (no auth header) just for the login call
        String baseUrl = System.getenv().getOrDefault("APP_URL", "http://localhost:3000");

        RequestSpecification plainSpec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();

        // Login and get fresh token
        ApiClient client = new ApiClient(plainSpec);
        gradle.junit.selenium.api.AuthApi authApi = new gradle.junit.selenium.api.AuthApi(client);
        cachedToken = authApi.login(email, password);

        // Parse expiry from JWT so we know when to refresh next time
        expiryEpochSeconds = parseExpiry(cachedToken);
        log.info("New token obtained — valid until epoch {}", expiryEpochSeconds);
    }

    /**
     * Reads the expiry time from the JWT token payload.
     * JWT format: header.payload.signature  — payload is base64-encoded JSON.
     * Example payload: {"exp": 1748998212, "iat": 1748994612, ...}
     */
    private static long parseExpiry(String jwtToken) {
        try {
            String[] parts = jwtToken.split("\\.");
            if (parts.length < 2) return 0;

            // Decode the middle section (payload) from base64
            byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
            String payload = new String(decoded);

            // Extract the "exp" field using regex — no JSON library needed
            Matcher matcher = Pattern.compile("\"exp\":(\\d+)").matcher(payload);
            if (matcher.find()) {
                return Long.parseLong(matcher.group(1));
            }

        } catch (Exception e) {
            log.warn("Could not parse token expiry — token will be treated as expired each time: {}", e.getMessage());
        }
        return 0;
    }
}
