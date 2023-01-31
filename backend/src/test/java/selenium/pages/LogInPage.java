package selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LogInPage {

    private static String PAGE_URL="https://localhost:4200/anon";

    private WebDriver driver;

    //sign_in_button
    @FindBy(css = ".sign_in_button")
    WebElement buttonSignIn;

    //formControlName="email"
    @FindBy(css = "input[formControlName='email']")
    WebElement emailInput;

    @FindBy(css = "input[formControlName='password']")
    WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    WebElement logInButton;

    public LogInPage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);
        PageFactory.initElements(driver, this);
    }

    public void clickOnSignInButton(){
        buttonSignIn.click();
    }

    public void fillEmailAndPassword(String email, String password){
        emailInput.clear();
        emailInput.sendKeys(email);
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickLogInButton(){
        logInButton.click();
    }
}
