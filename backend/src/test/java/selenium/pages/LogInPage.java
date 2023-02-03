package selenium.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LogInPage {

    private static String PAGE_URL="https://localhost:4200/anon";

    private WebDriver driver;

    @FindBy(xpath = "//p[contains(@class, 'form_tittle')]")
    WebElement pageTitle;

    //sign_in_button
    @FindBy(css = ".sign_in_button")
    WebElement buttonSignIn;

    //formControlName="email"
    @FindBy(css = "input[formControlName='email']")
    WebElement emailInput;

    @FindBy(css = "input[formControlName='password']")
    WebElement passwordInput;

    @FindBy(xpath = "//input[@formControlName='email'][contains(@class, 'ng-invalid')]")
    WebElement emailInvalidInput;

    @FindBy(css = "//input[@formControlName='password'][contains(@class, 'ng-invalid')]")
    WebElement passwordInvalidInput;

    @FindBy(xpath = "//a[contains(@class, 'sign_in_button')]")
    WebElement sigInPageLink;

    @FindBy(xpath = "//button[contains(@class, 'reset-password-link')]")
    WebElement forgotPasswordButton;

    @FindBy(css = "button[type='submit']")
    WebElement logInButton;

    @FindBy(xpath = "//button[@type='submit'][contains(@class, 'disabled_large_btn')]")
    WebElement disabledLoginButton;

    @FindBy(xpath = "//button[@type='submit'][contains(@class, 'large_button')]")
    WebElement enabledLoginButton;

     @FindBy(xpath = "//simple-snack-bar//span")
    WebElement snackbarText;

    @FindBy(xpath = "//simple-snack-bar/div/button/span")
    WebElement snackbarButton;

    //button
    @FindBy(xpath = "//button[contains(@aria-label, 'Hide password')]")
    WebElement hidePasswordButton;

    @FindBy(xpath = "//snack-bar-container")
    WebElement snackbarContainer;

    @FindBy(xpath = "//p[contains(@class, 'status-title')]")
    WebElement statusTitle;

    @FindBy(xpath = "//mat-dialog-container")
    WebElement emailDialog;

    @FindBy(xpath = "//mat-dialog-container//input[contains(@formcontrolname, 'email')]")
    WebElement emailForResetPassword;
    @FindBy(xpath = "//mat-dialog-container//input[contains(@formcontrolname, 'email')][contains(@class, 'ng-invalid')]")
    WebElement errorEmailForResetPassword;
    @FindBy(xpath = "//mat-dialog-container//button[contains(@class, 'submit-button')]")
    WebElement sendResetPasswordButton;

    @FindBy(xpath = "//mat-dialog-container//button[contains(@class, 'submit-button-disable')]")
    WebElement disabledSendResetPasswordButton;

    @FindBy(xpath = "//button[contains(text(), 'Get a ride')]")
    WebElement getRideButton;



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

    public void fillEmail(String email){
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void fillPassword(String password){
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickLogInButton(){
        logInButton.click();
    }

    public boolean isOpened() {
        return (new WebDriverWait(driver, Duration.ofMinutes(5)))
                .until(ExpectedConditions.textToBePresentInElement(pageTitle, "Welcome back"));
    }

    public WebElement isPresentInvalidEmailField(){
        return (new WebDriverWait(driver, Duration.ofMinutes(5)))
                .until(ExpectedConditions.visibilityOf(emailInvalidInput));
    }

    public WebElement isPresentInvalidPasswordField(){
        return (new WebDriverWait(driver, Duration.ofMinutes(5)))
                .until(ExpectedConditions.visibilityOf(passwordInvalidInput));
    }

    public Object isLoginButtonInvalid() {
        return (new WebDriverWait(driver, Duration.ofMinutes(5)))
                .until(ExpectedConditions.visibilityOf(disabledLoginButton));
    }

    public Object isLoginButtonValid() {
        return (new WebDriverWait(driver, Duration.ofMinutes(5)))
                .until(ExpectedConditions.visibilityOf(enabledLoginButton));
    }

    public void clickOutsidePasswordInput() {
        passwordInput.sendKeys(Keys.TAB);
    }

    public WebElement isMessagePopupOpened(){
        return (new WebDriverWait(driver, Duration.ofMinutes(5)))
                .until(ExpectedConditions.visibilityOf(snackbarContainer));
    }

    public boolean messagePopupContainsText(String text){
        return (new WebDriverWait(driver, Duration.ofMinutes(5)))
                .until(ExpectedConditions.textToBePresentInElement(snackbarText, text));
    }
    public boolean messagePopupButtonContainsText(String text){
        return (new WebDriverWait(driver, Duration.ofMinutes(5)))
                .until(ExpectedConditions.textToBePresentInElement(snackbarButton, text));
    }

    public void clickOnHidePassword(){
        this.hidePasswordButton.click();
    }

    public boolean isOpenedNextDriverPage(){
        return (new WebDriverWait(driver, Duration.ofMinutes(5))
                .until(ExpectedConditions.textToBePresentInElement(statusTitle, "Your current status")));

    }

    public WebElement isOpenedNextPassengerPage(){
        return (new WebDriverWait(driver, Duration.ofMinutes(5))
                .until(ExpectedConditions.visibilityOf(getRideButton)));

    }

    public void clickForgotPassword(){
        this.forgotPasswordButton.click();
    }

    public WebElement checkOpenEmailDialog(){
        return (new WebDriverWait(driver, Duration.ofMinutes(5)))
                .until(ExpectedConditions.visibilityOf(emailDialog));
    }

    public void fillEmailForForgotPassword(String email){
        emailForResetPassword.clear();
        emailForResetPassword.sendKeys(email);
    }

    public void clickButtonForResetPasswordRequest(){
        sendResetPasswordButton.click();
    }

    public WebElement checkInvalidEmailFormFieldForgotPassword(){
        return (new WebDriverWait(driver, Duration.ofMinutes(5)))
                .until(ExpectedConditions.visibilityOf(errorEmailForResetPassword));
    }

    public WebElement checkDisableButtonForSendResetPasswordRequest() {
        return (new WebDriverWait(driver, Duration.ofMinutes(5)))
                .until(ExpectedConditions.visibilityOf(disabledSendResetPasswordButton));
    }

}
