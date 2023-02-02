package selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderingRidePage {

    private WebDriver driver;

    @FindBy(css = "button[type='submit']")
    WebElement getRideButton;

    @FindBy(name = "start")
    WebElement startLocationInput;

    @FindBy(id = "start-error")
    WebElement startLocationInputError;

    @FindBy(id = "email-error")
    WebElement emailInputError;

    @FindBy(name = "end")
    WebElement endLocationInput;

    @FindBy(css = ".mat-option-text")
    List<WebElement> lisOption;

    @FindBy(css = ".cdk-overlay-pane")//cdk-overlay-pane mat-autocomplete-panel-above
    WebElement option;

    @FindBy(css = "mat-radio-button[value='cheapest']")
    WebElement radioOptionCheapest;

    @FindBy(css = "mat-radio-button[value='travelTime']")
    WebElement radioOptionTravelTime;

    @FindBy(css = "mat-checkbox[formcontrolname='babyOption']")
    WebElement checkboxBaby;

    @FindBy(css = "mat-checkbox[formcontrolname='foodOption']")
    WebElement checkboxFood;

    @FindBy(css = "mat-checkbox[formcontrolname='petOption']")
    WebElement checkboxPet;

    @FindBy(css = "mat-checkbox[formcontrolname='baggageOption']")
    WebElement checkboxBaggageOption;

    @FindBy(css = ".intermediate-stations-container")
    WebElement intermediateStationContainer;

    @FindBy(css = ".other-users-container")
    WebElement otherUsersContainer;

    @FindBy(name = "first intermediate")
    WebElement firstIntermediateInput;

    @FindBy(name = "second intermediate")
    WebElement secondIntermediateInput;

    @FindBy(name = "third intermediate")
    WebElement thirdIntermediateInput;

    @FindBy(css = "input[formControlName='userEmailFriendsFirst']")
    WebElement firstUserEmail;

    @FindBy(css = "input[formControlName='userEmailFriendsSecond']")
    WebElement secondUserEmail;

    @FindBy(css = "input[formControlName='userEmailFriendsThird']")
    WebElement thirdUserEmail;

    @FindBy(name = "explanation-users")
    WebElement explanationUserText;

    @FindBy(name = "explanation-locations")
    WebElement explanationStationText;

    @FindBy(css = ".mat-simple-snack-bar-content")
    WebElement snackBarError;

    @FindBy(tagName = "simple-snack-bar")
    WebElement snackBar;

    @FindBy(tagName = "snack-bar-container")
    WebElement snackBarContainer;

    @FindBy(name = "third intermediate-button")
    WebElement buttonMinusThird;

    @FindBy(name = "second intermediate-button")
    WebElement buttonMinusSecond;

    @FindBy(css = ".mat-simple-snackbar-action")
    WebElement buttonSnackBar;

    @FindBy(css = ".message-title")
    WebElement messageNewRide;

    @FindBy(css = ".price-message")
    WebElement messagePrice;

    @FindBy(css = ".submit-button-pay")
    WebElement buttonPayNotification;

    @FindBy(css = ".mat-simple-snack-bar-content")
    WebElement snackBarMessage;

    public OrderingRidePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isOpened() {
        return (new WebDriverWait(driver, Duration.ofMinutes(5)))
                .until(ExpectedConditions.textToBePresentInElement(getRideButton, "Get a ride"));
    }

    public WebElement getOrderingRidePage() {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(getRideButton));
        return getRideButton;
    }

    public void fillStartPlace(String location) {
        startLocationInput.clear();
        startLocationInput.sendKeys(location);
    }

    public void fillEndPlace(String location) {
        endLocationInput.clear();
        endLocationInput.sendKeys(location);
    }

    //assert da je velicina liste veca od nula
    public void clickLocation() {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(option));
        if (lisOption.size() > 0) {
            lisOption.get(0).click();
        }
    }

    public void clickOnGetARide() {
        getRideButton.click();
    }

    public void clickOnAllCheckbox() {
        checkboxBaby.click();
        checkboxFood.click();
        checkboxPet.click();
        checkboxBaggageOption.click();
    }

    public void selectCheapestOption() {
        radioOptionCheapest.click();
    }

    public void selectShortestTime() {
        radioOptionTravelTime.click();
    }

    public void clickOnIntermediateStationContainer() {
        intermediateStationContainer.click();
    }

    public void clickOnOtherUsersContainer() {
        otherUsersContainer.click();
    }

    public void fillAllIntermediateStation(String first, String second, String third) {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(explanationStationText));
        fillFirstIntermediateStation(first);
        clickLocation();
        fillSecondIntermediateStation(second);
        clickLocation();
        fillThirdIntermediateStation(third);
        clickLocation();
    }

    public void fillFirstIntermediateStation(String location) {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(firstIntermediateInput));
        firstIntermediateInput.clear();
        firstIntermediateInput.sendKeys(location);
    }

    public void fillSecondIntermediateStation(String location) {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(secondIntermediateInput));
        secondIntermediateInput.clear();
        secondIntermediateInput.sendKeys(location);
    }

    public void fillThirdIntermediateStation(String location) {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(thirdIntermediateInput));
        thirdIntermediateInput.clear();
        thirdIntermediateInput.sendKeys(location);
    }

    public void fillAllUsers(String first, String second, String third) {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(explanationUserText));
        fillFirstUserEmail(first);
        fillSecondUserEmail(second);
        fillThirdUserEmail(third);
    }

    public void fillFirstUserEmail(String location) {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(firstUserEmail));
        firstUserEmail.clear();
        firstUserEmail.sendKeys(location);
    }

    public void fillSecondUserEmail(String location) {
        secondUserEmail.clear();
        secondUserEmail.sendKeys(location);
    }

    public void fillThirdUserEmail(String location) {
        thirdUserEmail.clear();
        thirdUserEmail.sendKeys(location);
    }

    public String getErrorMessageLocation() {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(startLocationInputError));
        return startLocationInputError.getText();
    }

    public String getErrorMessageUserEmail() {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(emailInputError));
        return emailInputError.getText();
    }

    public String getSnackBarError() {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(snackBar));
        return snackBarError.getText();
    }

    public void clickOnButtonMinusThird() {
        buttonMinusThird.click();
    }

    public void clickOnButtonMinusSecond() {
        buttonMinusSecond.click();
    }

    public void clickOnErrorMessage() {
        buttonSnackBar.click();
    }

    public String getMessageNewRide() {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(snackBarContainer));
        return messageNewRide.getText();
    }

    public String getPrice() {
        return messagePrice.getText();
    }

    public void clickOnButtonPayNotification() {
        buttonPayNotification.click();
    }

    public String getMessage() {
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(snackBarContainer));
        return snackBarMessage.getText();
    }

}
