package com.izzydrive.backend.controller.executingRide;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.constants.UserConst;
import com.izzydrive.backend.dto.LoginDTO;
import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.exception.ErrorMessage;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.NotificationStatus;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.model.users.driver.DriverStatus;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.utils.LoginDTOUtil;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-start-driving.properties"})
public class DrivingStartTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DriverService driverService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private NotificationService notificationService;

    @Test
    void should_start_driving_is_success() {
        HttpHeaders headers = logInUsers(LoginDTOUtil.getDriverMika());
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        setContextForUser(DriverConst.D_MIKA_EMAIL);
        Driver driverBefore = driverService.getCurrentlyLoggedDriverWithCurrentDriving();

        ResponseEntity<TextResponse> response = testRestTemplate
                .exchange("/drivings/start", HttpMethod.GET, httpEntity, TextResponse.class);

        Driver driverAfter = driverService.getCurrentlyLoggedDriverWithCurrentDriving();
        DrivingDTOWithLocations driving = driverService.getCurrentDriving();

        if (driverBefore.getDriverStatus() == DriverStatus.TAKEN) {
            assertEquals(DriverStatus.ACTIVE, driverAfter.getDriverStatus());
        }
        assertEquals(new TextResponse("Driving successfully started"), response.getBody());
        assertEquals(DrivingState.ACTIVE, driving.getDrivingState());
        assertNotNull(driving.getStartDate());
    }

    @Test
    void should_throw_epx_driver_do_not_have_current_waiting_driving_to_start_when_driver_do_not_has_driving() {
        HttpHeaders headers = logInUsers(LoginDTOUtil.getDriverPredrag());
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/start", HttpMethod.GET, httpEntity, ErrorMessage.class);

        assertEquals("You don't have current waiting driving to start", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void should_throw_epx_driver_do_not_have_current_waiting_driving_to_start_when_driver_has_current_driving_status_other_then_waiting() {
        HttpHeaders headers = logInUsers(LoginDTOUtil.getDriverMilan()); //has current driving in state - PAYMENT
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/start", HttpMethod.GET, httpEntity, ErrorMessage.class);

        assertEquals("You don't have current waiting driving to start", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void should_throw_epx_cant_start_driving_not_at_location_when_driver_do_not_arrived_at_start_location() {
        HttpHeaders headers = logInUsers(LoginDTOUtil.getDriverPetar());
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/start", HttpMethod.GET, httpEntity, ErrorMessage.class);

        assertEquals("You can't start driving because you didn't arrived at start location", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void should_throw_epx_user_does_not_has_role_driver() {
        HttpHeaders headers = logInUsers(LoginDTOUtil.getPassengerJohn());
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/start", HttpMethod.GET, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    private HttpHeaders logInUsers(LoginDTO user) {
        ResponseEntity<UserWithTokenDTO> response = testRestTemplate
                .postForEntity("/auth/login/", user, UserWithTokenDTO.class);

        String token = "Bearer " + Objects.requireNonNull(response.getBody()).getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Content-Type", "application/json");
        return headers;
    }

    private int getNumberOfNewDrivingNotificationsForDriver(String email) {
        return (int) notificationService.findAllForUserByEmail(email)
                .stream()
                .filter(n -> n.getNotificationStatus().equals(NotificationStatus.DRIVER_ARRIVED_AT_START))
                .count();
    }

    private void setContextForUser(String userEmail) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEmail, UserConst.PASSWORD));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
