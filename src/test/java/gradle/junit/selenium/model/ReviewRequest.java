package gradle.junit.selenium.model;

public class ReviewRequest {

    private String message;
    private String author;

    public ReviewRequest(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
