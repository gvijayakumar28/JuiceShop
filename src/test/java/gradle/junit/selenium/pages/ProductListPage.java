package gradle.junit.selenium.pages;

import gradle.junit.selenium.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductListPage extends BasePage {

    private final By firstProductCard = By.cssSelector("mat-grid-tile:first-child mat-card");

    public ProductListPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open first product from the list")
    public ReviewPage openFirstProduct() {
        click(firstProductCard);
        return new ReviewPage(driver);
    }
}
