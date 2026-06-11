package gradle.junit.selenium.pages;

import gradle.junit.selenium.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    private static final String LOGIN_PATH = "/#/login";

    private final By cookieDismissButton = By.xpath("//a[contains(@class,'cc-dismiss')]");
    private final By welcomeBannerCloseButton = By.xpath("//button[@aria-label='Close Welcome Banner']");
    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("loginButton");
    private final By loginError =By.xpath("//div[@class='error ng-star-inserted']");

    public LoginPage() {
        super();
    }

    @Step("Open login page")
    public LoginPage open(String baseUrl) {
        navigateTo(baseUrl + LOGIN_PATH);
        return this;
    }

    @Step("Dismiss popups")
    public LoginPage dismissPopups() {
        // Popups only appear on the FIRST page load of a browser session.
        // Use a short 2s wait here — the default 10s wait would waste ~20s
        // in every later test when the popups never reappear.
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            shortWait.until(ExpectedConditions.elementToBeClickable(cookieDismissButton)).click();
        } catch (Exception e) {
            // Cookie popup not present, continue
        }
        try {
            shortWait.until(ExpectedConditions.elementToBeClickable(welcomeBannerCloseButton)).click();
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
        return new ProductListPage();
    }

    @Step("Login with email: {email} and {password}")
    public void enterCredentials(String email, String password) {
        type(emailField, email);
        type(passwordField, password);
        click(loginButton);
    }

    public boolean isLoginErrorMessageDisplayed(){
        return isDisplayed(loginError);
    }
}
