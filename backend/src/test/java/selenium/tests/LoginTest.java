package selenium.tests;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import selenium.pages.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class LoginTest extends TestBase{

    private static final String USERNAME_DRIVER_MIKA = "mika@gmail.com";
    private static final String USERNAME_PASSENGER_NATASA = "natasha.lakovic@gmail.com";
    private static final String PASSWORD = "12345678";
    private static final String NOT_EXISTING_PASSWORD = "1";
    private static final String INVALID_USERNAME = "mika";
    private static final String NOT_EXISTING_USERNAME = "mika123@gmail.com";

    @Test
    public void should_appear_error_label_when_email_not_valid_for_pattern(){
        LogInPage logInPage = new LogInPage(driver);
        logInPage.clickOnSignInButton();
        assertTrue(logInPage.isOpened());
        logInPage.fillEmail(INVALID_USERNAME);
        assertNotNull(logInPage.isPresentInvalidEmailField());
        assertNotNull(logInPage.isLoginButtonInvalid());
    }

    @Test
    public void should_appear_error_label_when_password_not_entered(){
        LogInPage logInPage = new LogInPage(driver);
        logInPage.clickOnSignInButton();
        assertTrue(logInPage.isOpened());
        logInPage.fillEmail(USERNAME_DRIVER_MIKA);
        logInPage.fillPassword("");
        logInPage.clickOutsidePasswordInput();

        assertNotNull(logInPage.isLoginButtonInvalid());
    }

    @Test
    public void should_appear_error_popup_when_email_not_exiting(){
        LogInPage logInPage = new LogInPage(driver);
        logInPage.clickOnSignInButton();
        assertTrue(logInPage.isOpened());
        logInPage.fillEmail(NOT_EXISTING_USERNAME);
        logInPage.fillPassword(PASSWORD);
        assertNotNull(logInPage.isLoginButtonValid());
        logInPage.clickLogInButton();
        logInPage.isMessagePopupOpened();
        logInPage.messagePopupContainsText("Login failed. You are not registered");
        logInPage.messagePopupButtonContainsText("ERROR");
    }

    @Test
    public void should_appear_error_popup_when_password_invalid(){
        LogInPage logInPage = new LogInPage(driver);
        logInPage.clickOnSignInButton();
        assertTrue(logInPage.isOpened());
        logInPage.fillEmail(USERNAME_DRIVER_MIKA);
        logInPage.fillPassword(NOT_EXISTING_PASSWORD);
        assertNotNull(logInPage.isLoginButtonValid());
        logInPage.clickLogInButton();
        logInPage.isMessagePopupOpened();
        logInPage.messagePopupContainsText("Login failed. You are not registered");
        logInPage.messagePopupButtonContainsText("ERROR");
    }

    @Test
    public void should_appear_password_text_and_login_success(){
        LogInPage logInPage = new LogInPage(driver);
        logInPage.clickOnSignInButton();
        assertTrue(logInPage.isOpened());
        logInPage.fillEmail(USERNAME_DRIVER_MIKA);
        logInPage.fillPassword(PASSWORD);
        logInPage.clickOnHidePassword();
        assertNotNull(logInPage.isLoginButtonValid());
        logInPage.clickLogInButton();
        assertTrue(logInPage.isOpenedNextDriverPage());
    }

    @Test
    public void should_display_error_message_when_email_not_existing(){
        LogInPage logInPage = new LogInPage(driver);
        logInPage.clickOnSignInButton();
        logInPage.clickForgotPassword();
        assertNotNull(logInPage.checkOpenEmailDialog());
        logInPage.fillEmailForForgotPassword(NOT_EXISTING_USERNAME);
        logInPage.clickButtonForResetPasswordRequest();
        assertNotNull(logInPage.isMessagePopupOpened());
        assertTrue(logInPage.messagePopupContainsText("User with email: "+ NOT_EXISTING_USERNAME +" does not exists"));
        assertTrue(logInPage.messagePopupButtonContainsText("ERROR"));
    }

    @Test
    public void should_go_to_login_page_and_login_when_password_valid(){
        LogInPage logInPage = new LogInPage(driver);
        logInPage.clickOnSignInButton();
        logInPage.clickForgotPassword();
        assertNotNull(logInPage.checkOpenEmailDialog());
        logInPage.fillEmailForForgotPassword(USERNAME_DRIVER_MIKA);
        logInPage.clickButtonForResetPasswordRequest();
        assertNotNull(logInPage.isMessagePopupOpened());
        assertTrue(logInPage.messagePopupContainsText("We send you link in email"));
        assertTrue(logInPage.messagePopupButtonContainsText("OK"));

        ResetPasswordPage resetPasswordPage = new ResetPasswordPage(driver);

        resetPasswordPage.fillNewPassword(USERNAME_PASSENGER_NATASA); //ispravno popuni
        resetPasswordPage.fillRepeatedPassword(USERNAME_PASSENGER_NATASA);
        resetPasswordPage.clickResetPassword();

        assertTrue(logInPage.isOpened()); //login
        assertNotNull(logInPage.isMessagePopupOpened());
        assertTrue(logInPage.messagePopupContainsText("Successfully reset password"));
        assertTrue(logInPage.messagePopupButtonContainsText("OK"));
        logInPage.fillEmail(USERNAME_PASSENGER_NATASA);
        logInPage.fillPassword(USERNAME_PASSENGER_NATASA);
        logInPage.clickOnHidePassword();
        assertNotNull(logInPage.isLoginButtonValid());
        logInPage.clickLogInButton();
        assertNotNull(logInPage.isOpenedNextPassengerPage());
    }



    @AfterEach
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
