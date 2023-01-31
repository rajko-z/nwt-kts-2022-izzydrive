package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.LoginDTO;
import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.exception.ErrorMessage;
import com.izzydrive.backend.utils.LoginDTOFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-start-driving.properties"})
public class ReportDriverFromPassengerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void should_report_driver_is_success() {
        HttpHeaders headers = logInUsers(LoginDTOFactory.getPassengerBob());
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<TextResponse> response = testRestTemplate
                .exchange("/passengers/report-driver", HttpMethod.GET, httpEntity, TextResponse.class);

        assertEquals(response.getBody(), new TextResponse("Driver is successfully reported."));
    }

    @Test
    void should_throw_epx_cant_report_driver_when_passenger_do_not_has_current_driving() {
        HttpHeaders headers = logInUsers(LoginDTOFactory.getPassengerSara());
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/passengers/report-driver", HttpMethod.GET, httpEntity, ErrorMessage.class);

        assertEquals("You can't report driver because you do not have currently active driving", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void should_throw_epx_cant_report_driver_when_passenger_current_driving_does_not_has_status_active() {
        HttpHeaders headers = logInUsers(LoginDTOFactory.getPassengerJohn()); //has current driving in state - WAITING
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/passengers/report-driver", HttpMethod.GET, httpEntity, ErrorMessage.class);

        assertEquals("You can't report driver because you do not have currently active driving", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
