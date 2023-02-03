package com.izzydrive.backend.controller.cancel;

import com.izzydrive.backend.constants.AdminConst;
import com.izzydrive.backend.dto.CancellationReasonDTO;
import com.izzydrive.backend.dto.LoginDTO;
import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.exception.ErrorMessage;
import com.izzydrive.backend.model.NotificationStatus;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.utils.LoginDTOUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-cancel-driving.properties"})
public class ReservationDrivingCancelTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    private HttpHeaders headers;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DrivingRepository drivingRepository;

    public void setUpHeaders(LoginDTO loginDTO) {
    ResponseEntity<UserWithTokenDTO> response = testRestTemplate
            .postForEntity("/auth/login/", loginDTO, UserWithTokenDTO.class);

    String token = "Bearer " + Objects.requireNonNull(response.getBody()).getToken();
    headers = new HttpHeaders();
    headers.add("Authorization", token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("Content-Type", "application/json");
}
    @Test
    public void should_throw_user_with_email_doesnt_exits_when_user_not_logged_in(){
        Long drivingID = 1L;
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", drivingID);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/reject-reservation-driver",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User with email: anonymousUser does not exists", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void should_throw_driving_doesnt_exits_when_drivingID_not_exits(){
        setUpHeaders(LoginDTOUtil.getDriverMika());
        Long drivingID = 10L;
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", drivingID);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/reject-reservation-driver",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Driving with that id doesn't exists", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void should_throw_cant_find_reservation_to_cancel_when_driver_not_have_reservation_by_user(){ //vozac nema rezervaciju
        setUpHeaders(LoginDTOUtil.getDriverMilan());
        Long drivingID = 1L;
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", drivingID);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/reject-reservation-driver",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Can't find reservation to cancel", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void should_throw_cant_find_reservation_to_cancel_when_driver_have_herevation_but_drivingID_invalid(){ //vozac ima rezervaciju ali nije tad drivingID prosledjen
        setUpHeaders(LoginDTOUtil.getDriverMika());
        Long drivingID = 1L;
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", drivingID);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/reject-reservation-driver",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Can't find reservation to cancel", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void should_cancel_reservation_driving_with_intermediate_stations(){
        setUpHeaders(LoginDTOUtil.getDriverMika());
        Long drivingID = 2L;

        int numberOfAdminNotifications = getNumberOfNewDrivingNotificationsForAdmin();
        int numberOfDrivings = drivingRepository.findAll().size();

        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", drivingID);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<TextResponse> response = testRestTemplate
                .exchange("/drivings/reject-reservation-driver",  HttpMethod.POST, httpEntity, TextResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully denied driving", Objects.requireNonNull(response.getBody()).getText());

        int newNotificationNumberForAdmin = getNumberOfNewDrivingNotificationsForAdmin();
        int newNumberOfDrivings = drivingRepository.findAll().size();

        assertEquals(numberOfDrivings - 1, newNumberOfDrivings);
        assertEquals(numberOfAdminNotifications + 1, newNotificationNumberForAdmin);
    }

    @Test
    public void should_cancel_reservation_driving_without_intermediate_stations(){
        setUpHeaders(LoginDTOUtil.getDriverPredrag());
        Long drivingID = 7L;

        int numberOfAdminNotifications = getNumberOfNewDrivingNotificationsForAdmin();
        int numberOfDrivings = drivingRepository.findAll().size();

        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", drivingID);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<TextResponse> response = testRestTemplate
                .exchange("/drivings/reject-reservation-driver",  HttpMethod.POST, httpEntity, TextResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully denied driving", Objects.requireNonNull(response.getBody()).getText());

        int newNotificationNumberForAdmin = getNumberOfNewDrivingNotificationsForAdmin();
        int newNumberOfDrivings = drivingRepository.findAll().size();

        assertEquals(numberOfDrivings - 1, newNumberOfDrivings);
        assertEquals(numberOfAdminNotifications + 1, newNotificationNumberForAdmin);
    }

    private int getNumberOfNewDrivingNotificationsForAdmin() {
        return (int) notificationService.findAllForUserByEmail(AdminConst.ADMIN_EMAIL)
                .stream()
                .filter(n -> n.getNotificationStatus().equals(NotificationStatus.REJECTED_DRIVING_DRIVER))
                .count();
    }
}
