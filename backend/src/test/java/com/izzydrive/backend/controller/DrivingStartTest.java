package com.izzydrive.backend.controller;

import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.utils.LoginDTOFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-start-driving.properties"})
public class DrivingStartTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private HttpHeaders headers;

    @BeforeEach
    public void setUpHeaders() {
        ResponseEntity<UserWithTokenDTO> response = testRestTemplate
                .postForEntity("/auth/login/", LoginDTOFactory.getDriverMika(), UserWithTokenDTO.class);

        String token = "Bearer " + response.getBody().getToken();
        headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Content-Type", "application/json");
    }

    /***
    Primer integracionog testa sa posebnom bazom - baza definisana za slucaj startovanja voznje

    ***/

    @Test
    public void testStartDriving() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<TextResponse> response = testRestTemplate
                .exchange("/drivings/start", HttpMethod.GET, httpEntity, TextResponse.class);

        assertEquals(response.getBody(), new TextResponse("Driving successfully started"));
    }
}
