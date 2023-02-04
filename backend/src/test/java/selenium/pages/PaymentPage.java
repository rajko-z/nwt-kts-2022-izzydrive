package selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PaymentPage {

    private WebDriver driver;

    @FindBy(css = ".submit-button-approve-payment")
    WebElement paymentButton;

    @FindBy(css = ".submit-button-cancel-payment")
    WebElement cancelButton;

    @FindBy(css = ".text.title")
    WebElement titleElement;

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickOnPayButton() {
        paymentButton.click();
    }

    public void clickOnCancelButton() {
        cancelButton.click();
    }

    public boolean isOpened() {
        return (new WebDriverWait(driver, Duration.ofSeconds(20)))
                .until(ExpectedConditions.textToBePresentInElement(titleElement, "To pay:"));
    }


}
