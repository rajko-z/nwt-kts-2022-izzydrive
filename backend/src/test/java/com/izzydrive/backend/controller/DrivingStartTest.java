package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.LoginDTO;
import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.exception.ErrorMessage;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.utils.LoginDTOFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-start-driving.properties"})
public class DrivingStartTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DriverService driverService;

    @Test
    void should_start_driving_is_success() {
        HttpHeaders headers = logInUsers(LoginDTOFactory.getDriverMika());
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<TextResponse> response = testRestTemplate
                .exchange("/drivings/start", HttpMethod.GET, httpEntity, TextResponse.class);

        Driver driver = driverService.getCurrentlyLoggedDriverWithCurrentDriving();
        DrivingDTOWithLocations driving = driverService.getCurrentDriving();

        assertEquals(new TextResponse("Driving successfully started"), response.getBody());

        assertEquals(DrivingState.ACTIVE, driving.getDrivingState());
        assertNotEquals(null, driving.getStartDate());
    }

    @Test
    void should_throw_epx_driver_do_not_have_current_waiting_driving_to_start_when_driver_do_not_has_driving() {
        HttpHeaders headers = logInUsers(LoginDTOFactory.getDriverPredrag());
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/start", HttpMethod.GET, httpEntity, ErrorMessage.class);

        assertEquals("You don't have current waiting driving to start", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void should_throw_epx_driver_do_not_have_current_waiting_driving_to_start_when_driver_has_current_driving_status_other_then_waiting() {
        HttpHeaders headers = logInUsers(LoginDTOFactory.getDriverMilan()); //has current driving in state - PAYMENT
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/start", HttpMethod.GET, httpEntity, ErrorMessage.class);

        assertEquals("You don't have current waiting driving to start", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void should_throw_epx_cant_start_driving_not_at_location_when_driver_do_not_arrived_at_start_location() {
        HttpHeaders headers = logInUsers(LoginDTOFactory.getDriverPetar());
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/start", HttpMethod.GET, httpEntity, ErrorMessage.class);

        assertEquals("You can't start driving because you didn't arrived at start location", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void should_throw_epx_user_does_not_has_role_driver() {
        HttpHeaders headers = logInUsers(LoginDTOFactory.getPassengerJohn());
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
}
