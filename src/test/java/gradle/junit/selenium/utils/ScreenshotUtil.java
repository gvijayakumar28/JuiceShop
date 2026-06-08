package gradle.junit.selenium.utils;

import gradle.junit.selenium.constants.TestOutputPaths;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtil.class);

    // Takes a screenshot, saves it as a PNG file and attaches it to Allure report
    public static void takeScreenshot(String screenshotName) {
        WebDriver driver = DriverFactory.getDriver();

        if (driver == null) {
            log.warn("Cannot take screenshot — browser is not running");
            return;
        }

        // Capture the screenshot as raw bytes
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        byte[] screenshotBytes = takesScreenshot.getScreenshotAs(OutputType.BYTES);

        // Save PNG file to test-output/screenshots/ folder
        saveToFile(screenshotName, screenshotBytes);

        // Also attach to Allure report so it appears inside the test
        Allure.addAttachment(
                screenshotName,
                "image/png",
                new ByteArrayInputStream(screenshotBytes),
                "png"
        );
    }

    private static void saveToFile(String screenshotName, byte[] screenshotBytes) {
        try {
            // Create the screenshots folder if it does not exist
            Path screenshotsFolder = Paths.get(TestOutputPaths.SCREENSHOTS);
            Files.createDirectories(screenshotsFolder);

            // Build filename: testName_2026-06-08_22-15-01.png
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = screenshotName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";

            // Write the PNG file
            Path filePath = screenshotsFolder.resolve(fileName);
            Files.write(filePath, screenshotBytes);

            log.info("Screenshot saved: {}", filePath);

        } catch (IOException e) {
            log.error("Failed to save screenshot to file: {}", e.getMessage());
        }
    }
}
