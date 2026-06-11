package gradle.junit.selenium.base;

import gradle.junit.selenium.driver.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

/**
 * Abstract base class for all Page Objects.
 * Automatically obtains the WebDriver from DriverManager — no need to pass it in the constructor.
 * All page classes extend this and inherit the shared Selenium helper methods.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected List<WebElement> getElementList(By locator){
        return driver.findElements(locator);
    }

    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForElementVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForPageLoad(int timeoutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(webDriver ->
                        ((JavascriptExecutor) webDriver)
                                .executeScript("return document.readyState")
                                .equals("complete"));
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void pressEnterKey(){
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).perform();
    }

    protected String getText(By locator) {
        return waitForVisible(locator).getText();
    }

    protected String getText(WebElement element) {
        return waitForElementVisible(element).getText();
    }

    protected void navigateTo(String url) {
        driver.get(url);
    }

    protected WebElement pagination(By locator, By nextPage,  String target){
            do{
                List<WebElement> elements = driver.findElements(locator);
                for(WebElement element : elements){
                    if(getText(element).contains(target)){
                        return element;
                    }
                }
                if (isDisplayed(nextPage)) {
                    click(nextPage);
                    waitForPageLoad(20);
                } else {
                    break;
                }
            }while (true);

            return null;
    }

    protected void waitForUrlToContain(String urlFragment) {
        wait.until(ExpectedConditions.urlContains(urlFragment));
    }
}
