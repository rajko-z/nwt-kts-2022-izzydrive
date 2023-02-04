package selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CurrentDrivingPassengerPage {
    //provera driver i vreme
    private WebDriver driver;

    @FindBy(tagName = "h1")
    WebElement title;

    @FindBy(css = ".driver")
    WebElement driverName;

    @FindBy(css = ".minute")
    WebElement time;

    public CurrentDrivingPassengerPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isOpened() {
        return (new WebDriverWait(driver, Duration.ofMinutes(20)))
                .until(ExpectedConditions.textToBePresentInElement(title, "Current driving"));
    }

    public String getDriverName() {
        return driverName.getText();
    }

    public Integer getTime() {
        return Integer.valueOf(time.getText());
    }
}
