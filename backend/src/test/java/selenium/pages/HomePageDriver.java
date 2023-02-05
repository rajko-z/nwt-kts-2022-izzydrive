package selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePageDriver {

    private WebDriver driver;

    @FindBy(css = "#startButtonDriverPage")
    WebElement startButton;

    @FindBy(css = "#endButtonDriverPage")
    WebElement finishButton;


    @FindBy(css ="#yesClickedOnFinishCheck")
    WebElement yesButton;

    @FindBy(css ="#noClickedOnFinishCheck")
    WebElement noButton;

    @FindBy(css = "h2.title")
    WebElement title;

    public HomePageDriver(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isOpened() {
        return (new WebDriverWait(driver, Duration.ofMinutes(20)))
                .until(ExpectedConditions.textToBePresentInElement(title, "Current driving"));
    }

    public void clickOnStart() {
        (new WebDriverWait(driver, Duration.ofMinutes(20)))
                .until(ExpectedConditions.elementToBeClickable(startButton)).click();
    }

    public void clickOnFinish() {
        (new WebDriverWait(driver, Duration.ofMinutes(20)))
                .until(ExpectedConditions.elementToBeClickable(finishButton)).click();
    }

    public void clickOnNoOption() {
        (new WebDriverWait(driver, Duration.ofMinutes(20)))
                .until(ExpectedConditions.elementToBeClickable(noButton)).click();
    }

    public void clickOnYesOption() {
        (new WebDriverWait(driver, Duration.ofMinutes(20)))
                .until(ExpectedConditions.elementToBeClickable(yesButton)).click();
    }


}
