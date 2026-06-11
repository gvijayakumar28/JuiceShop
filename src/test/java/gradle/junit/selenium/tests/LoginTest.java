package gradle.junit.selenium.tests;

import gradle.junit.selenium.base.BaseTest;
import gradle.junit.selenium.pages.LoginPage;
import gradle.junit.selenium.pages.ProductListPage;
import gradle.junit.selenium.utils.ConfigReader;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(priority = 1, groups = {"smoke"}, description = "Login via UI")
    @Story("Verify valid Login Scenario")
    @Description("Login to JuiceShop with valid credentials")
    public void verifyValidLoginTest(){
        LoginPage loginPage = new LoginPage();
        loginPage.open(BASE_URL);
        loginPage.dismissPopups();
        ProductListPage productListPage = loginPage.loginAs(customer.getEmail(), customer.getPassword());
        Assert.assertNotNull(productListPage, "User should be login successful");
        LoginPage loginPage1 = productListPage.logout();
        Assert.assertNotNull(loginPage1, "Login page should be displayed");

    }

    @Test(priority = 2, groups = {"smoke"}, description = "Login via UI")
    @Story("Verify Invalid Login Scenario")
    @Description("Login to JuiceShop with invalid credentials")
    public void verifyInvalidLoginTest(){
        LoginPage loginPage = new LoginPage();
        loginPage.open(BASE_URL);
        loginPage.dismissPopups();
        loginPage.enterCredentials(customer.getEmail(), "test");
        Assert.assertTrue(loginPage.isLoginErrorMessageDisplayed(), "Invalid email or password message should be displayed");
    }
}
