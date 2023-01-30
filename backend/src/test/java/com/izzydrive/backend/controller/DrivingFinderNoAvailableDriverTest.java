package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.utils.DrivingFinderRequestUtil;
import com.izzydrive.backend.utils.LoginDTOUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = { "classpath:application.properties", "classpath:application-no-driver-available.properties" })
public class DrivingFinderNoAvailableDriverTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private HttpHeaders headers;

    @BeforeEach
    public void setUpHeaders() {
        ResponseEntity<UserWithTokenDTO> response = testRestTemplate
                .postForEntity("/auth/login/", LoginDTOUtil.getPassengerJohn(), UserWithTokenDTO.class);

        String token = "Bearer " + response.getBody().getToken();
        headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Content-Type", "application/json");
    }

    @Test
    void should_return_zero_options_for_no_available_driver() {
        DrivingFinderRequestDTO request = DrivingFinderRequestUtil.getSimpleRequest();

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<DrivingOptionDTO[]> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, DrivingOptionDTO[].class);


        List<DrivingOptionDTO> options = Arrays.asList(response.getBody());
        assertEquals(0, options.size());
    }

}

