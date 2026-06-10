package gradle.junit.selenium.model;

/**
 * POJO for the post review API request body.
 * Uses the JavaBean style — no-arg constructor with getters and setters.
 * RestAssured serializes this to JSON using the getters.
 */
public class ReviewRequest {

    private String message;
    private String author;

    public ReviewRequest() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
