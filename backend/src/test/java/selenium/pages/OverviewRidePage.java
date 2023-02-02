package selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OverviewRidePage {

    private WebDriver driver;

    @FindBy(css = ".submit-button-payment")
    WebElement paymentButton;

    @FindBy(css = ".minute")
    WebElement minuteText;

    @FindBy(css = ".driver-name")
    WebElement driverText;

    @FindBy(css = ".price")
    WebElement priceText;


    public OverviewRidePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getMinute() {
        return minuteText.getText();
    }

    public String getDriver() {
        return driverText.getText();
    }

    public String getPrice() {
        return priceText.getText();
    }

    public boolean isOpened(){
        return (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(paymentButton, "Payment"));
    }

    public void clickOnPaymentButton(){
        paymentButton.click();
    }
}
