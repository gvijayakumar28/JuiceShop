package gradle.junit.selenium.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * POJO for the product search API response body.
 *
 * JSON shape:
 * {
 *   "status": "success",
 *   "data": [ { "id": 1, "name": "Apple Juice", ... }, ... ]
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSearchResponse {

    private String status;
    private List<Product> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Product {

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
