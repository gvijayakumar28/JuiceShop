package gradle.junit.selenium.pages;

import gradle.junit.selenium.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductListPage extends BasePage {

    private final By firstProductCard = By.cssSelector("mat-grid-tile:first-child mat-card");
    private final By accountNavBarTab = By.id("navbarAccount");
    private final By accountNavBarLogout = By.id("navbarLogoutButton");
    private final By searchButton = By.id("searchQuery");
    private final By searchInput = By.id("mat-input-0");
    private final By searchList = By.xpath("//mat-grid-tile");
    private final By nextPage = By.xpath("//button[@aria-label='Next page']");



    public ProductListPage() {
        super();
    }

    @Step("Open first product from the list")
    public ReviewPage openFirstProduct() {
        click(firstProductCard);
        return new ReviewPage();
    }

    @Step("Open first product from the list")
    public LoginPage logout() {
        click(accountNavBarTab);
        click(accountNavBarLogout);
        waitForUrlToContain("/#/login");
        return new LoginPage();
    }

    @Step("Search product {productName}")
    public void searchProduct(String productName){
        click(searchButton);
        type(searchInput, productName);
        pressEnterKey();
    }

    @Step("Verify product in search list {productName}")
    public boolean isProductDisplayed(String productName){
        WebElement element = pagination(searchList, nextPage, productName);
        return element.isDisplayed();
    }

    @Step("Verify product in search list {productName}")
    public ReviewPage selectSearchProduct(String productName){
        pagination(searchList, nextPage, productName).click();
        return new ReviewPage();
    }

}
