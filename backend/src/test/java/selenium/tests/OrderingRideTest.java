package selenium.tests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import selenium.helper.Helper;
import selenium.pages.LogInPage;
import selenium.pages.OrderingRidePage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderingRideTest extends TestBase {
    private static String USER_NAME_PASSENGER = "john@gmail.com";
    private static String PASSWORD_PASSENGER = "123";

    @BeforeEach
    private void setUp() throws InterruptedException {
        LogInPage orderingRidePage = new LogInPage(driver);
        orderingRidePage.clickOnSignInButton();
        orderingRidePage.fillEmailAndPassword(USER_NAME_PASSENGER, PASSWORD_PASSENGER);
        orderingRidePage.clickLogInButton();
        Helper.takeScreenshoot(driver, "login_application_full");
        justWait(500);
    }

    private void justWait(Integer howLong) throws InterruptedException {
        synchronized (driver) {
            driver.wait(howLong);
        }
    }


    @Test
    public void orderingRide() {
        OrderingRidePage orderingRideTest = new OrderingRidePage(driver);
        String currentPage = orderingRideTest.getOrderingRidePage().getText();
        assertEquals("Get a ride", currentPage);

    }
}
