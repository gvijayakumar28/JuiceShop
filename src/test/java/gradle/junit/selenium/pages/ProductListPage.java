package gradle.junit.selenium.pages;

import gradle.junit.selenium.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class ProductListPage extends BasePage {

    private final By firstProductCard = By.cssSelector("mat-grid-tile:first-child mat-card");

    public ProductListPage() {
        super();
    }

    @Step("Open first product from the list")
    public ReviewPage openFirstProduct() {
        click(firstProductCard);
        return new ReviewPage();
    }
}
