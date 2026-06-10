package gradle.junit.selenium.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO for the login API response body.
 * RestAssured deserializes the JSON response into this object using response.as(LoginResponse.class).
 *
 * JSON shape:
 * {
 *   "authentication": { "token": "...", "bid": 1, "umail": "..." }
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)  // ignore JSON fields we don't model
public class LoginResponse {

    private Authentication authentication;

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Authentication {

        private String token;
        private String umail;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUmail() {
            return umail;
        }

        public void setUmail(String umail) {
            this.umail = umail;
        }
    }
}
