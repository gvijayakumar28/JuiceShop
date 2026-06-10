package gradle.junit.selenium.model;

/**
 * POJO for the login API request body.
 * Uses the JavaBean style — no-arg constructor with getters and setters.
 * RestAssured serializes this to JSON using the getters.
 */
public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
