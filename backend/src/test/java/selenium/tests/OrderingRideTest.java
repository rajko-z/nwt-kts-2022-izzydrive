package selenium.tests;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import selenium.pages.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class OrderingRideTest extends TestBase {
    private static final String USER_NAME_PASSENGER = "john@gmail.com";
    private static final String USER_NAME_LIKED_USER = "bob@gmail.com";
    private static final String USER_WITH_INVALID_PAYMENT_DATA = "kate@gmail.com";
    private static final String USER_NAME_DRIVER_MILAN = "milan@gmail.com";
    private static final String USER_NAME_DRIVER_MIKA = "mika@gmail.com";
    private static final String PASSWORD = "12345678";
    private static final String LOCATION1 = "banijska";
    private static final String LOCATION2 = "zeleznicka stanica";

    public static WebDriver driverDriver1;
    public static WebDriver driverDriver2;
    public static WebDriver driverLinkedUser;


    @Test
    void should_1_location_input_is_invalid() {
        logInUser(USER_NAME_PASSENGER, driver);

        OrderingRidePage orderingRidePage = new OrderingRidePage(driver);
        assertTrue(orderingRidePage.isOpened());
        String currentPage = orderingRidePage.getOrderingRidePage().getText();
        assertEquals("Get a ride", currentPage);

        //location is required
        orderingRidePage.clickOnGetARide();
        String errorMessage = orderingRidePage.getErrorMessageLocation();
        assertEquals("Please select valid start location", errorMessage);

        //location name is not valid
        orderingRidePage.fillStartPlace(LOCATION1);
        orderingRidePage.clickOnGetARide();
        errorMessage = orderingRidePage.getErrorMessageLocation();
        assertEquals("Please select valid start location", errorMessage);
    }

    @Test
    void should_2_intermediate_station_does_not_have_right_order() {
        logInUser(USER_NAME_PASSENGER, driver);

        OrderingRidePage orderingRidePage = new OrderingRidePage(driver);
        assertTrue(orderingRidePage.isOpened());

        orderingRidePage.fillStartPlace(LOCATION1);
        orderingRidePage.clickLocation();
        orderingRidePage.fillEndPlace(LOCATION2);
        orderingRidePage.clickLocation();
        orderingRidePage.clickOnIntermediateStationContainer();
        orderingRidePage.fillThirdIntermediateStation("kosovska");
        orderingRidePage.clickLocation();
        orderingRidePage.clickOnGetARide();

        assertEquals("First and second intermediate stations should be selected before third", orderingRidePage.getSnackBarError());
        orderingRidePage.clickOnErrorMessage();
//        Helper.takeScreenshoot(driver, "order");

        orderingRidePage.clickOnButtonMinusThird();
        orderingRidePage.fillSecondIntermediateStation("kosovska");
        orderingRidePage.clickLocation();
        orderingRidePage.clickOnGetARide();

        assertEquals("First intermediate stations should be selected before second", orderingRidePage.getSnackBarError());

        orderingRidePage.clickOnButtonMinusSecond();
        orderingRidePage.fillFirstIntermediateStation("kosovska");
        orderingRidePage.clickLocation();
        orderingRidePage.clickOnGetARide();

        ChooseRidePage chooseRidePage = new ChooseRidePage(driver);
        assertEquals("There are currently no available drivers, please try again soon.", chooseRidePage.getMessageNoAvailable());
    }

    @Test
    void should_3_other_user_email_is_invalid() {
        logInUser(USER_NAME_PASSENGER, driver);

        OrderingRidePage orderingRidePage = new OrderingRidePage(driver);
        assertTrue(orderingRidePage.isOpened());
        String currentPage = orderingRidePage.getOrderingRidePage().getText();
        assertEquals("Get a ride", currentPage);

        orderingRidePage.fillStartPlace(LOCATION1);
        orderingRidePage.clickLocation();
        orderingRidePage.fillEndPlace(LOCATION2);
        orderingRidePage.clickLocation();
        orderingRidePage.clickOnOtherUsersContainer();

        //email doesn't mach vith email pattern
        orderingRidePage.fillFirstUserEmail("bobgmail.com");
        orderingRidePage.clickOnGetARide();

        String errorMessage = orderingRidePage.getErrorMessageUserEmail();
        assertEquals("Invalid email", errorMessage);

        //user no exist
        orderingRidePage.fillFirstUserEmail("bob2@gmail.com");
        orderingRidePage.clickOnGetARide();

        errorMessage = orderingRidePage.getErrorMessageUserEmail();
        assertEquals("Invalid email", errorMessage);

        //user is currently logged user
        orderingRidePage.fillFirstUserEmail(USER_NAME_PASSENGER);
        orderingRidePage.clickOnGetARide();

        errorMessage = orderingRidePage.getErrorMessageUserEmail();
        assertEquals("Invalid email", errorMessage);
    }


    @Test
    void should_4_ordering_ride_with_no_available_drivers_and_passenger_has_no_ride_options() {
        logInUser(USER_NAME_PASSENGER, driver);

        OrderingRidePage orderingRidePage = new OrderingRidePage(driver);
        assertTrue(orderingRidePage.isOpened());
        String currentPage = orderingRidePage.getOrderingRidePage().getText();
        assertEquals("Get a ride", currentPage);

        orderingRidePage.fillStartPlace(LOCATION1);
        orderingRidePage.clickLocation();
        orderingRidePage.fillEndPlace(LOCATION2);
        orderingRidePage.clickLocation();
        orderingRidePage.clickOnGetARide();

        ChooseRidePage chooseRidePage = new ChooseRidePage(driver);
        String message = chooseRidePage.getMessageNoAvailable();
        assertEquals("There are currently no available drivers, please try again soon.", message);
    }

    @Test
    void should_5_ordering_ride_and_passenger_has_ride_options() {
        openPageForDriver(USER_NAME_DRIVER_MIKA, driverDriver1);
        openPageForDriver(USER_NAME_DRIVER_MILAN, driverDriver2);

        logInUser(USER_NAME_PASSENGER, driver);

        OrderingRidePage orderingRidePage = new OrderingRidePage(driver);
        assertTrue(orderingRidePage.isOpened());
        String currentPage = orderingRidePage.getOrderingRidePage().getText();
        assertEquals("Get a ride", currentPage);

        orderingRidePage.fillStartPlace(LOCATION1);
        orderingRidePage.clickLocation();
        orderingRidePage.fillEndPlace(LOCATION2);
        orderingRidePage.clickLocation();
        orderingRidePage.clickOnGetARide();

        ChooseRidePage chooseRidePage = new ChooseRidePage(driver);
        String choosePage = chooseRidePage.getChoosePage();
        assertEquals("Next", choosePage);
        assertTrue(chooseRidePage.getNumberOfOption() > 0);
    }

    @Test
    void should_6_ordering_ride_with_all_parameter_in_form() {
        openPageForDriver(USER_NAME_DRIVER_MIKA, driverDriver1);
        openPageForDriver(USER_NAME_DRIVER_MILAN, driverDriver2);

        logInUser(USER_NAME_PASSENGER, driver);

        OrderingRidePage orderingRidePage = new OrderingRidePage(driver);
        assertTrue(orderingRidePage.isOpened());

        orderingRidePage.fillStartPlace(LOCATION1);
        orderingRidePage.clickLocation();
        orderingRidePage.fillEndPlace(LOCATION2);
        orderingRidePage.clickLocation();
        orderingRidePage.clickOnIntermediateStationContainer();
        orderingRidePage.fillAllIntermediateStation("kosovska", "sajmiste", "liman");
        orderingRidePage.clickOnOtherUsersContainer();
        orderingRidePage.fillAllUsers("bob@gmail.com", "sara@gmail.com", "kate@gmail.com");
        orderingRidePage.selectCheapestOption();
        orderingRidePage.clickOnAllCheckbox();

        orderingRidePage.clickOnGetARide();
        ChooseRidePage chooseRidePage = new ChooseRidePage(driver);
        assertTrue(chooseRidePage.getNumberOfOption() > 0);
    }

    @Test
    void should_7_ordering_ride_with_cheapest_parameter() {
        openPageForDriver(USER_NAME_DRIVER_MIKA, driverDriver1);
        openPageForDriver(USER_NAME_DRIVER_MILAN, driverDriver2);

        logInUser(USER_NAME_PASSENGER, driver);

        OrderingRidePage orderingRidePage = new OrderingRidePage(driver);
        assertTrue(orderingRidePage.isOpened());

        orderingRidePage.fillStartPlace(LOCATION1);
        orderingRidePage.clickLocation();
        orderingRidePage.fillEndPlace(LOCATION2);
        orderingRidePage.clickLocation();
        orderingRidePage.selectCheapestOption();

        orderingRidePage.clickOnGetARide();
        ChooseRidePage chooseRidePage = new ChooseRidePage(driver);
        assertTrue(chooseRidePage.getNumberOfOption() > 0);
        assertTrue(chooseRidePage.checkPriceOrder());
    }

    @Test
    void should_8_ordering_ride_with_shortest_time() {
        openPageForDriver(USER_NAME_DRIVER_MIKA, driverDriver1);
        openPageForDriver(USER_NAME_DRIVER_MILAN, driverDriver2);

        logInUser(USER_NAME_PASSENGER, driver);

        OrderingRidePage orderingRidePage = new OrderingRidePage(driver);
        assertTrue(orderingRidePage.isOpened());

        orderingRidePage.fillStartPlace(LOCATION1);
        orderingRidePage.clickLocation();
        orderingRidePage.fillEndPlace(LOCATION2);
        orderingRidePage.clickLocation();
        orderingRidePage.selectShortestTime();

        orderingRidePage.clickOnGetARide();
        ChooseRidePage chooseRidePage = new ChooseRidePage(driver);
        assertTrue(chooseRidePage.getNumberOfOption() > 0);
        assertTrue(chooseRidePage.checkTimeOrder());
    }

    @Test
    void should_9_display_chosen_option() {
        openPageForDriver(USER_NAME_DRIVER_MIKA, driverDriver1);
        openPageForDriver(USER_NAME_DRIVER_MILAN, driverDriver2);

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
        assertTrue(chooseRidePage.getNumberOfOption() > 0);
        Integer number = chooseRidePage.getNumberOfOption();
        chooseRidePage.clickOnOption(number - 1);

        String selectedPrice = chooseRidePage.getSelectedPrice(number - 1);
        String selectedMinute = chooseRidePage.getSelectedMinute(number - 1);
        chooseRidePage.clickOnNextButton();

        OverviewRidePage overviewRidePage = new OverviewRidePage(driver);
        assertTrue(overviewRidePage.isOpened());

        String overviewPrice = overviewRidePage.getPrice();
        String overviewMinute = overviewRidePage.getMinute();

        assertEquals(selectedMinute, overviewMinute);
        assertEquals(selectedPrice, overviewPrice);
    }

    @Test
    void should_91_linked_user_get_notification_new_ride() {
        openPageForDriver(USER_NAME_DRIVER_MIKA, driverDriver1);
        openPageForDriver(USER_NAME_DRIVER_MILAN, driverDriver2);

        logInUser(USER_NAME_PASSENGER, driver);

        OrderingRidePage orderingRidePage = new OrderingRidePage(driver);
        assertTrue(orderingRidePage.isOpened());

        orderingRidePage.fillStartPlace(LOCATION1);
        orderingRidePage.clickLocation();
        orderingRidePage.fillEndPlace(LOCATION2);
        orderingRidePage.clickLocation();
        orderingRidePage.clickOnOtherUsersContainer();
        orderingRidePage.fillFirstUserEmail("bob@gmail.com");
        orderingRidePage.clickOnGetARide();

        ChooseRidePage chooseRidePage = new ChooseRidePage(driver);
        assertTrue(chooseRidePage.isOpenedWithOption());
        chooseRidePage.clickOnNextButton();

        OverviewRidePage overviewRidePage = new OverviewRidePage(driver);
        assertTrue(overviewRidePage.isOpened());
        String overviewPrice = overviewRidePage.getPrice();

        setDriverLinkedUser();
        logInUser(USER_NAME_LIKED_USER, driverLinkedUser);
        OrderingRidePage orderingRidePageLinkedUser = new OrderingRidePage(driverLinkedUser);
        overviewRidePage.clickOnPaymentButton();
        PaymentPage paymentPage = new PaymentPage(driver);
        assertTrue(paymentPage.isOpened());

        String message = orderingRidePageLinkedUser.getMessageNewRide();
        assertEquals("You have been added to a new ride", message);
        assertEquals(overviewPrice, orderingRidePageLinkedUser.getPrice());
        orderingRidePageLinkedUser.clickOnButtonPayNotification();
        PaymentPage paymentPageLinkedUser = new PaymentPage(driver);
        assertTrue(paymentPageLinkedUser.isOpened());
        paymentPageLinkedUser.clickOnCancelButton();
    }

    @Test
    void should_92_payment_is_not_success() {
        openPageForDriver(USER_NAME_DRIVER_MIKA, driverDriver1);

        logInUser(USER_WITH_INVALID_PAYMENT_DATA, driver);

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

        assertEquals("Payment failure, canceling current driving. Make sure every passenger input correct paying info and have enough funds.", orderingRidePage.getMessage());
    }

    private void openPageForDriver(String emailDriver, WebDriver driverDriver) {
        ChromeOptions handlingSSL = new ChromeOptions();
        handlingSSL.setAcceptInsecureCerts(true);
        driverDriver = new ChromeDriver(handlingSSL);
        driverDriver.manage().window().maximize();

        LogInPage orderingRidePage = new LogInPage(driverDriver);
        orderingRidePage.clickOnSignInButton();
        orderingRidePage.fillEmailAndPassword(emailDriver, PASSWORD);
        orderingRidePage.clickLogInButton();
    }

    private void setDriverLinkedUser() {
        ChromeOptions handlingSSL = new ChromeOptions();
        handlingSSL.setAcceptInsecureCerts(true);
        driverLinkedUser = new ChromeDriver(handlingSSL);
        driverLinkedUser.manage().window().maximize();
    }

    private void logInUser(String user, WebDriver driver) {
        LogInPage logInPage = new LogInPage(driver);
        logInPage.clickOnSignInButton();
        logInPage.fillEmailAndPassword(user, PASSWORD);
        logInPage.clickLogInButton();
    }

    @AfterEach
    public void quitDriver() {
        if (driverDriver1 != null) {
            driverDriver1.quit();
        }
        if (driverDriver2 != null) {
            driverDriver2.quit();
        }
        if (driverLinkedUser != null) {
            driverLinkedUser.quit();
        }
        driver.quit();
    }

    //zakazivanje voznje - provera unosa vremena
    //provera kod vozaca da li im se otvorila njihova strana

}
