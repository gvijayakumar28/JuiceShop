package gradle.junit.selenium.pages;

import gradle.junit.selenium.base.BasePage;
import org.openqa.selenium.By;

public class BasketPage extends BasePage {

    private static final String BASKET_PATH = "/#/basket";

    private final By basketItems = By.cssSelector("mat-row");
    private final By checkoutButton = By.xpath("//button[@routerlink='/address/select']");

    public BasketPage() {
        super();
    }

    public BasketPage open(String baseUrl) {
        navigateTo(baseUrl + BASKET_PATH);
        return this;
    }

    public boolean hasItems() {
        return isDisplayed(basketItems);
    }
}
