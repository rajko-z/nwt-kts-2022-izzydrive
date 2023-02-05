package selenium.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import selenium.pages.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CancelRideTest extends TestBase {

    private static final String USER_NAME_PASSENGER = "natasha.lakovic@gmail.com";
    private static final String USER_NAME_DRIVER_MIKA = "mika@gmail.com";
    private static final String PASSWORD = "12345678";
    private static final String LOCATION1 = "Rumenacka 47";
    private static final String LOCATION2 = "zeleznicka stanica";

    public static WebDriver driverDriver;

    @Test
    void cancel_ride() {
        openPageForDriver(USER_NAME_DRIVER_MIKA);

        logInUser(USER_NAME_PASSENGER, driver);

        OrderingRidePage orderingRidePage = new OrderingRidePage(driver);
        assertTrue(orderingRidePage.isOpened());
        orderingRidePage.fillStartPlace(LOCATION1);
        orderingRidePage.clickLocation();
        orderingRidePage.fillEndPlace(LOCATION2);
        orderingRidePage.clickLocation();
        orderingRidePage.clickOnGetARide();

        ChooseRidePage chooseRidePage = new ChooseRidePage(driver);
        assertTrue(chooseRidePage.isOpenedWithOption());
        chooseRidePage.clickOnNextButton();

        OverviewRidePage overviewRidePage = new OverviewRidePage(driver);
        assertTrue(overviewRidePage.isOpened());
        overviewRidePage.clickOnPaymentButton();

        PaymentPage paymentPage = new PaymentPage(driver);
        assertTrue(paymentPage.isOpened());
        paymentPage.clickOnPayButton();

        CurrentDrivingPassengerPage currentDrivingPassengerPage = new CurrentDrivingPassengerPage(driver);
        assertTrue(currentDrivingPassengerPage.isOpened());

        HomePageDriver homePageDriver = new HomePageDriver(driverDriver);
        assertTrue(homePageDriver.isOpened());

        homePageDriver.clickOnStart();
        homePageDriver.clickOnFinish();
        homePageDriver.clickOnNoOption();
        homePageDriver.clickOnFinish();
        homePageDriver.clickOnYesOption();
    }

    private void openPageForDriver(String emailDriver) {
        ChromeOptions handlingSSL = new ChromeOptions();
        handlingSSL.setAcceptInsecureCerts(true);
        driverDriver = new ChromeDriver(handlingSSL);
        driverDriver.manage().window().maximize();

        LogInPage loginPage = new LogInPage(driverDriver);
        loginPage.clickOnSignInButton();
        loginPage.fillEmailAndPassword(emailDriver, PASSWORD);
        loginPage.clickLogInButton();
    }

    private void logInUser(String user, WebDriver driver) {
        LogInPage logInPage = new LogInPage(driver);
        logInPage.clickOnSignInButton();
        logInPage.fillEmailAndPassword(user, PASSWORD);
        logInPage.clickLogInButton();
    }

    @AfterEach
    public void quitDriver() {
        if (driverDriver != null) {
            driverDriver.quit();
        }
        driver.quit();
    }
}
