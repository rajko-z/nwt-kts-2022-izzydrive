package selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ChooseRidePage {

    private WebDriver driver;

    @FindBy(css = "div[data-index]")
    List<WebElement> listOption;

    @FindBy(css = ".message-no-available")
    WebElement messageNoAvailable;

    @FindBy(css = ".submit-button-next")
    WebElement nextButton;

    public ChooseRidePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getMessageNoAvailable() {
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.elementToBeClickable(messageNoAvailable));
        return messageNoAvailable.getText();
    }

    public Integer getNumberOfOption() {
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.elementToBeClickable(nextButton));
        return listOption.size();
    }

    public String getChoosePage() {
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.elementToBeClickable(nextButton));
        return nextButton.getText();
    }

    public boolean isOpenedWithOption() {
        return (new WebDriverWait(driver, Duration.ofSeconds(20)))
                .until(ExpectedConditions.textToBePresentInElement(nextButton, "Next"));
    }

    public void clickOnNextButton() {
        nextButton.click();
    }

    public boolean checkPriceOrder() {
        List<WebElement> elements = new ArrayList<>();
        for (int i = 0; i < listOption.size(); i++) {
            WebElement element = driver.findElement(new By.ByCssSelector(".price" + i));
            elements.add(element);
            if (elements.size() <= 1) {
                continue;
            }
            if (Integer.parseInt(element.getText()) < Integer.parseInt(elements.get(elements.size() - 2).getText())) {
                return false;
            }
        }
        return true;
    }

    public boolean checkTimeOrder() {
        List<Integer> elements = new ArrayList<>();
        for (int i = 0; i < listOption.size(); i++) {
            WebElement elementMinute = driver.findElement(new By.ByCssSelector(".minute" + i));
            WebElement elementDrivingTime = driver.findElement(new By.ByCssSelector(".driving-time" + i));
            Integer time = Integer.parseInt(elementMinute.getText()) + Integer.parseInt(elementDrivingTime.getText());
            elements.add(time);
            if (elements.size() <= 1) {
                continue;
            }
            if (time < elements.get(elements.size() - 2)) {
                return false;
            }
        }
        return true;
    }

    public String getSelectedMinute(Integer index) {
        WebElement minuteTextSelected = driver.findElement(new By.ByCssSelector(".minute" + index + ".selected"));
        return minuteTextSelected.getText();
    }

    public String getSelectedDriver(Integer index) {
        List<WebElement> driverTextSelected = driver.findElements(new By.ByCssSelector(".driver-name"));
        return driverTextSelected.get(index).getText();
    }

    public String getSelectedPrice(Integer index) {
        WebElement priceTextSelected = driver.findElement(new By.ByCssSelector(".price" + index + ".selected"));
        return priceTextSelected.getText();
    }

    public void clickOnOption(Integer index) {
        WebElement option = driver.findElement(new By.ByCssSelector("div[data-index='" + index + "']"));
        option.click();
    }
}
