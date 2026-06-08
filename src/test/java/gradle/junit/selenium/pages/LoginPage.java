package gradle.junit.selenium.pages;

import gradle.junit.selenium.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private static final String LOGIN_PATH = "/#/login";

    private final By cookieDismissButton = By.xpath("//a[contains(@class,'cc-dismiss')]");
    private final By welcomeBannerCloseButton = By.xpath("//button[@aria-label='Close Welcome Banner']");
    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("loginButton");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open login page")
    public LoginPage open(String baseUrl) {
        navigateTo(baseUrl + LOGIN_PATH);
        return this;
    }

    @Step("Dismiss popups")
    public LoginPage dismissPopups() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cookieDismissButton)).click();
        } catch (Exception e) {
            // Cookie popup not present, continue
        }
        try {
            wait.until(ExpectedConditions.elementToBeClickable(welcomeBannerCloseButton)).click();
        } catch (Exception e) {
            // Welcome banner not present, continue
        }
        return this;
    }

    @Step("Login with email: {email}")
    public ProductListPage loginAs(String email, String password) {
        type(emailField, email);
        type(passwordField, password);
        click(loginButton);
        waitForUrlToContain("/#/search");
        return new ProductListPage(driver);
    }
}
