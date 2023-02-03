package selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ResetPasswordPage {

    private static String PAGE_URL="https://localhost:4200/anon/reset-password/e1ab839e-eec3-4d90-b12c-8f929bcd3617";

    private WebDriver driver;

    @FindBy(xpath = "//button[contains(@class, 'submit-button')]")
    WebElement saveButton;

    @FindBy(xpath = "//mat-form-field//input[contains(@formcontrolname, 'newPassword')]")
    WebElement newPasswordInput;
    @FindBy(xpath = "//mat-form-field//input[contains(@formcontrolname, 'repeatedPassword')]")
    WebElement repeatedNewPasswordInput;

    public ResetPasswordPage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);
        PageFactory.initElements(driver, this);
    }


    public boolean isOpened(){
        return (new WebDriverWait(driver, Duration.ofMinutes(5))
                .until(ExpectedConditions.textToBePresentInElement(saveButton, "Save")));
    }
    public void fillNewPassword(String password){
        newPasswordInput.clear();
        newPasswordInput.sendKeys(password);
    }

    public void fillRepeatedPassword(String password){
        repeatedNewPasswordInput.clear();
        repeatedNewPasswordInput.sendKeys(password);
    }

    public void clickResetPassword(){
        saveButton.click();
    }


}
