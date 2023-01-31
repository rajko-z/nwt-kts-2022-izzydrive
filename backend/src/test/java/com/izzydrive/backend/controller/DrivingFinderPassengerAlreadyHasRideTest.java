package com.izzydrive.backend.controller;

import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.exception.ErrorMessage;
import com.izzydrive.backend.model.car.CarAccommodation;
import com.izzydrive.backend.utils.DrivingFinderUtil;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import com.izzydrive.backend.utils.LoginDTOUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:application.properties", "classpath:application-one-driver-free.properties" })
public class DrivingFinderPassengerAlreadyHasRideTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private HttpHeaders getLoginHeaderForUser(String email) {
        ResponseEntity<UserWithTokenDTO> response = testRestTemplate
                .postForEntity("/auth/login/", LoginDTOUtil.getLoggedUserByEmail(email), UserWithTokenDTO.class);

        String token = "Bearer " + response.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Content-Type", "application/json");
        return headers;
    }

    @Test
    void should_throw_exp_cant_link_passenger_that_already_has_current_driving() {
        HttpHeaders headers = getLoginHeaderForUser(PassengerConst.P_JOHN_EMAIL);

        CarAccommodation carAccommodation = new CarAccommodation(false, false, false, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);

        Set<String> linkedPassengers = new HashSet<>(List.of(PassengerConst.P_BOB_EMAIL));
        request.setLinkedPassengersEmails(linkedPassengers);

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.cantLinkPassengerThatAlreadyHasCurrentDriving(PassengerConst.P_BOB_EMAIL), response.getBody().getMessage());
    }

    @Test
    void should_throw_exp_you_already_have_current_driving() {
        HttpHeaders headers = getLoginHeaderForUser(PassengerConst.P_BOB_EMAIL);

        CarAccommodation carAccommodation = new CarAccommodation(false, false, false, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.YOU_ALREADY_HAVE_CURRENT_DRIVING, response.getBody().getMessage());
    }
}
