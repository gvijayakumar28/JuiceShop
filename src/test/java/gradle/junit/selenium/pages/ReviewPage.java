package gradle.junit.selenium.pages;

import gradle.junit.selenium.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class ReviewPage extends BasePage {

    private final By reviewTextArea = By.xpath("//textarea[@placeholder='What did you like or dislike?']");
    private final By submitReviewButton = By.xpath("//button[@aria-label='Send the review']");
    private final By expandReviewsPanel = By.xpath("//mat-expansion-panel[@aria-label='Expand for Reviews']");

    public ReviewPage() {
        super();
    }

    @Step("Submit review: {reviewText}")
    public ReviewPage submitReview(String reviewText) {
        type(reviewTextArea, reviewText);
        click(submitReviewButton);
        return this;
    }

    @Step("Expand reviews section")
    public ReviewPage expandReviews() {
        click(expandReviewsPanel);
        return this;
    }

    @Step("Check review is visible: {reviewText}")
    public boolean isReviewVisible(String reviewText) {
        By reviewLocator = By.xpath("//*[contains(text(),'" + reviewText + "')]");
        return isDisplayed(reviewLocator);
    }
}
