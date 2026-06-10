package gradle.junit.selenium.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * POJO for the product reviews API response body.
 *
 * JSON shape:
 * {
 *   "status": "success",
 *   "data": [ { "message": "...", "author": "..." }, ... ]
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewsResponse {

    private String status;
    private List<Review> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Review> getData() {
        return data;
    }

    public void setData(List<Review> data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Review {

        private String message;
        private String author;

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
}
