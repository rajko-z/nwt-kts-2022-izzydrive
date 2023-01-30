package com.izzydrive.backend.controller;

import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.utils.LoginDTOFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DrivingFinderTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private HttpHeaders headers;

    @BeforeEach
    public void setUpHeaders() {
        ResponseEntity<UserWithTokenDTO> response = testRestTemplate
                .postForEntity("/auth/login/", LoginDTOFactory.getAdmin(), UserWithTokenDTO.class);

        String token = "Bearer " + response.getBody().getToken();
        headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Content-Type", "application/json");
    }

    /**
     * samo proba da vidim dal prolazi
     * */
    @Test
    public void testGettingAllPassengers() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<UserDTO[]> response = testRestTemplate
                .exchange("/passengers",  HttpMethod.GET, httpEntity, UserDTO[].class);

        for (int i = 0; i < response.getBody().length; ++i) {
            System.out.println(response.getBody()[i].getEmail());
        }

        assertEquals(response.getBody().length, PassengerConst.PASSENGERS_COUNT);
    }


}
