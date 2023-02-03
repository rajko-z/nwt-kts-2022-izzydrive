package com.izzydrive.backend.controller.orderingride.payment;

import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.constants.UserConst;
import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.dto.payment.CurrentPayingDTO;
import com.izzydrive.backend.exception.ErrorMessage;
import com.izzydrive.backend.model.NotificationStatus;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import com.izzydrive.backend.utils.LoginDTOUtil;
import com.izzydrive.backend.utils.PaymentUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:application.properties", "classpath:application-payment.properties"})
public class PaymentTest {

    private static final String TEST_PASSENGER_1 = "test1@gmail.com";
    private static final String TEST_PASSENGER_2 = "test2@gmail.com";
    private static final String TEST_PASSENGER_3 = "test3@gmail.com";
    private static final String TEST_PASSENGER_4 = "test4@gmail.com";
    private static final String TEST_PASSENGER_5 = "test5@gmail.com";
    private static final String TEST_PASSENGER_6 = "test6@gmail.com";
    private static final String TEST_PASSENGER_7 = "test7@gmail.com";
    private static final String TEST_PASSENGER_8 = "test8@gmail.com";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PassengerService passengerService;

    private HttpHeaders getHeadersLoggedUser(String userEmail) {
        ResponseEntity<UserWithTokenDTO> response = testRestTemplate
                .postForEntity("/auth/login/", LoginDTOUtil.getLoggedUserByEmail(userEmail), UserWithTokenDTO.class);

        String token = "Bearer " + response.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Content-Type", "application/json");
        return headers;
    }

    @AfterEach
    public void clearState() {
        SecurityContextHolder.clearContext();
    }

    private void setContextForUser(String userEmail) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEmail, UserConst.PASSWORD));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void should_throw_exp_passenger_does_not_have_driving_for_payment() {
        HttpHeaders headers = getHeadersLoggedUser(TEST_PASSENGER_1);
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        HttpEntity<CurrentPayingDTO> httpEntity = new HttpEntity<>(currentPayingDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/payment/pay",  HttpMethod.PUT, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.YOU_DO_NOT_HAVE_DRIVING_FOR_PAYMENT, response.getBody().getMessage());
    }

    @Test
    public void should_throw_exp_payment_session_expired() {
        HttpHeaders headers = getHeadersLoggedUser(TEST_PASSENGER_2);
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        HttpEntity<CurrentPayingDTO> httpEntity = new HttpEntity<>(currentPayingDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/payment/pay",  HttpMethod.PUT, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.PAYMENT_SESSION_EXPIRED, response.getBody().getMessage());
    }

    @Test
    public void should_throw_exp_payment_already_approved() {
        HttpHeaders headers = getHeadersLoggedUser(TEST_PASSENGER_3);
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        HttpEntity<CurrentPayingDTO> httpEntity = new HttpEntity<>(currentPayingDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/payment/pay",  HttpMethod.PUT, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.YOU_ALREADY_APPROVED_THIS_PAYING, response.getBody().getMessage());
    }

    @Test
    public void should_throw_exp_user_does_not_have_payment_data() {
        HttpHeaders headers = getHeadersLoggedUser(TEST_PASSENGER_4);
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        HttpEntity<CurrentPayingDTO> httpEntity = new HttpEntity<>(currentPayingDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/payment/pay",  HttpMethod.PUT, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.CANT_USE_EXISTING_PAYING_DATA, response.getBody().getMessage());
    }

    @Test
    public void should_throw_exp_user_does_not_provide_valid_secret_key() {
        HttpHeaders headers = getHeadersLoggedUser(TEST_PASSENGER_5);

        CurrentPayingDTO currentPayingDTO = PaymentUtil.getWithoutExistingWithInvalidPrivateKey();

        HttpEntity<CurrentPayingDTO> httpEntity = new HttpEntity<>(currentPayingDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/payment/pay",  HttpMethod.PUT, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.ERROR_INVALID_SECRET_KEY, response.getBody().getMessage());
    }

    @Test
    public void should_throw_exp_user_does_not_provide_valid_public_key() {
        HttpHeaders headers = getHeadersLoggedUser(TEST_PASSENGER_5);

        CurrentPayingDTO currentPayingDTO = PaymentUtil.getWithoutExistingWithInvalidPublicKey();

        HttpEntity<CurrentPayingDTO> httpEntity = new HttpEntity<>(currentPayingDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/payment/pay",  HttpMethod.PUT, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.ERROR_INVALID_ETH_ADDRESS, response.getBody().getMessage());
    }

    @Test
    public void should_throw_exp_user_does_not_provide_valid_keypair_combination() {
        HttpHeaders headers = getHeadersLoggedUser(TEST_PASSENGER_5);

        CurrentPayingDTO currentPayingDTO = PaymentUtil.getWithoutExistingWithInvalidKeyPairCombination();

        HttpEntity<CurrentPayingDTO> httpEntity = new HttpEntity<>(currentPayingDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/payment/pay",  HttpMethod.PUT, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.ERROR_ETH_ADDRESS_DOES_NOT_MATCH_SECRET_KEY, response.getBody().getMessage());
    }

    @Test
    public void should_throw_payment_failure_not_enough_founds() {
        HttpHeaders headers = getHeadersLoggedUser(TEST_PASSENGER_6);

        CurrentPayingDTO currentPayingDTO = PaymentUtil.getWithoutExistingWithNotEnoughFounds();

        HttpEntity<CurrentPayingDTO> httpEntity = new HttpEntity<>(currentPayingDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/payment/pay",  HttpMethod.PUT, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.PAYMENT_FAILURE, response.getBody().getMessage());
    }

    @Test
    public void test_successfully_payment_for_one_passenger() {
        HttpHeaders headers = getHeadersLoggedUser(PassengerConst.P_BOB_EMAIL);
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        int numOfNotificationsBefore = notificationService.findAllForUserByEmail(PassengerConst.P_BOB_EMAIL).size();

        HttpEntity<CurrentPayingDTO> httpEntity = new HttpEntity<>(currentPayingDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/payment/pay",  HttpMethod.PUT, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<NotificationDTO> notifications = notificationService.findAllForUserByEmail(PassengerConst.P_BOB_EMAIL);
        assertEquals(numOfNotificationsBefore + 1, notifications.size());
        assertEquals(NotificationStatus.PAYMENT_SUCCESS, notifications.get(0).getNotificationStatus());
    }

    @Test
    public void test_success_payment_two_passengers() {
        HttpHeaders headers = getHeadersLoggedUser(PassengerConst.P_JOHN_EMAIL);
        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        int numOfNotificationsBeforeJohn = notificationService.findAllForUserByEmail(PassengerConst.P_JOHN_EMAIL).size();

        HttpEntity<CurrentPayingDTO> httpEntity = new HttpEntity<>(currentPayingDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/payment/pay",  HttpMethod.PUT, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<NotificationDTO> notifications = notificationService.findAllForUserByEmail(PassengerConst.P_JOHN_EMAIL);
        assertEquals(numOfNotificationsBeforeJohn, notifications.size()); // nije jos dobio jer treba i sara da plati


        SecurityContextHolder.clearContext();


        headers = getHeadersLoggedUser(PassengerConst.P_SARA_EMAIL);
        currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();

        int numOfNotificationsBeforeSara = notificationService.findAllForUserByEmail(PassengerConst.P_SARA_EMAIL).size();

        httpEntity = new HttpEntity<>(currentPayingDTO, headers);
        response = testRestTemplate.exchange("/payment/pay",  HttpMethod.PUT, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<NotificationDTO> notificationsAfterJohn = notificationService.findAllForUserByEmail(PassengerConst.P_JOHN_EMAIL);
        assertEquals(numOfNotificationsBeforeJohn+1, notificationsAfterJohn.size());
        assertEquals(NotificationStatus.PAYMENT_SUCCESS, notificationsAfterJohn.get(0).getNotificationStatus());

        List<NotificationDTO> notificationsAfterSara = notificationService.findAllForUserByEmail(PassengerConst.P_SARA_EMAIL);
        assertEquals(numOfNotificationsBeforeSara+1, notificationsAfterSara.size());
        assertEquals(NotificationStatus.PAYMENT_SUCCESS, notificationsAfterSara.get(0).getNotificationStatus());
    }
    // TODO:: fix test below
//    @Test
//    public void test_one_pay_other_reject() {
//        HttpHeaders headers = getHeadersLoggedUser(TEST_PASSENGER_7);
//        CurrentPayingDTO currentPayingDTO = PaymentUtil.getExistingCurrPaymentInfo();
//
//        int numOfNotificationsBeforeFirst = notificationService.findAllForUserByEmail(TEST_PASSENGER_7).size();
//
//        HttpEntity<CurrentPayingDTO> httpEntity = new HttpEntity<>(currentPayingDTO, headers);
//        ResponseEntity<ErrorMessage> response = testRestTemplate
//                .exchange("/payment/pay",  HttpMethod.PUT, httpEntity, ErrorMessage.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        List<NotificationDTO> notifications = notificationService.findAllForUserByEmail(TEST_PASSENGER_7);
//        assertEquals(numOfNotificationsBeforeFirst, notifications.size());
//
//
//        SecurityContextHolder.clearContext();
//
//
//        headers = getHeadersLoggedUser(TEST_PASSENGER_8);
//        HttpEntity<Object> rejectHttp = new HttpEntity<>(headers);
//        ResponseEntity<TextResponse> responseForReject = testRestTemplate
//                .exchange("/reject-linked-user",  HttpMethod.GET, rejectHttp, TextResponse.class);
//
//        assertEquals(HttpStatus.OK, responseForReject.getStatusCode());
//
//        notifications = notificationService.findAllForUserByEmail(TEST_PASSENGER_7);
//        assertEquals(numOfNotificationsBeforeFirst+1, notifications.size());
//        assertEquals(NotificationStatus.REJECTED_DRIVING_PASSENGER, notifications.get(0).getNotificationStatus());
//
//        setContextForUser(TEST_PASSENGER_7);
//        Passenger passenger = passengerService.getCurrentlyLoggedPassenger();
//        assertNull(passenger.getCurrentDriving());
//    }

}
