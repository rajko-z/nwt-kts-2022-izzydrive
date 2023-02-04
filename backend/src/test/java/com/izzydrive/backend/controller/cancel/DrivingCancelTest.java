package com.izzydrive.backend.controller.cancel;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.dto.CancellationReasonDTO;
import com.izzydrive.backend.dto.LoginDTO;
import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.payment.CurrentPayingDTO;
import com.izzydrive.backend.dto.payment.KeyPairDTO;
import com.izzydrive.backend.exception.ErrorMessage;
import com.izzydrive.backend.model.users.driver.DriverStatus;
import com.izzydrive.backend.service.maps.MapService;
import com.izzydrive.backend.utils.AddressesUtil;
import com.izzydrive.backend.utils.DrivingFinderUtil;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import com.izzydrive.backend.utils.LoginDTOUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.izzydrive.backend.constants.PassengerConst.P_SARA_E_ADDRESS;
import static com.izzydrive.backend.constants.PassengerConst.P_SARA_SECRET_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-cancel-driving.properties"})
public class DrivingCancelTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    private HttpHeaders headers;

    public void setUpHeaders(LoginDTO loginDTO) {
        ResponseEntity<UserWithTokenDTO> response = testRestTemplate
                .postForEntity("/auth/login/", loginDTO, UserWithTokenDTO.class);

        String token = "Bearer " + response.getBody().getToken();
        headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Content-Type", "application/json");
    }

    @Test
    void should_throw_driving_doesnt_exist_when_drivingID_not_exits(){ //not exiting driving id
        setUpHeaders(LoginDTOUtil.getDriverMika());
        Long unexcitingDrivingID = 10L;
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", unexcitingDrivingID);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/reject-regular-driver",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.DRIVING_DOESNT_EXIST, response.getBody().getMessage());
    }
    @Test
    void should_throw_cant_not_found_driving__exception_when_drivingIDs_not_matching(){ //postoji curretn driving ali se ne poklapa sa prosledjenim id-em
        setUpHeaders(LoginDTOUtil.getDriverMika());
        Long notMatchingID = 5L;
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", notMatchingID);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/reject-regular-driver",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.CANT_FIND_DRIVING_TO_CANCEL, response.getBody().getMessage());
    }

    @Test
    void should_throw_cant_not_found_driving__exception_when_driver_doesnt_have_current_driving(){ //current driving is null
        setUpHeaders(LoginDTOUtil.getDriverPetar());
        Long drivingId = 5L;
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", drivingId);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/reject-regular-driver",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.CANT_FIND_DRIVING_TO_CANCEL, response.getBody().getMessage());
    }

    @Test
    void should_throw_cant_not_found_driving_exception_when_current_driving_status_not_waiting(){ //curretn postoji, taj id je prosledjen, ali je ona active
        setUpHeaders(LoginDTOUtil.getDriverMika());
        Long drivingId = 1L;
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", drivingId);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/reject-regular-driver",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.CANT_FIND_DRIVING_TO_CANCEL, response.getBody().getMessage());
    }

    @Test
    void should_throw_cant_not_found_driving_exception_when_next_drivingID_is_invalid(){ //current je active, next postoji ali nije dobar id prosledjen
        setUpHeaders(LoginDTOUtil.getDriverMika());
        Long drivingId = 5L;
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", drivingId);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/reject-regular-driver",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.CANT_FIND_DRIVING_TO_CANCEL, response.getBody().getMessage());
    }

    @Test
    void should_throw_cant_not_found_driving_exception_when_current_and_next_drivingID_is_invalid(){ //current je waiting, next je waiting ali nije dobar id prosledjen
        setUpHeaders(LoginDTOUtil.getDriverPredrag());
        Long drivingId = 5L;
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", drivingId);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/reject-regular-driver",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.CANT_FIND_DRIVING_TO_CANCEL, response.getBody().getMessage());
    }

    @Test
    void should_cancel_driving_when_next_drivingID_is_valid(){ //next je waiting i taj ID je prosledjen
        setUpHeaders(LoginDTOUtil.getDriverPredrag());
        Long drivingId = 6L;
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", drivingId);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<TextResponse> response = testRestTemplate
                .exchange("/drivings/reject-regular-driver",  HttpMethod.POST, httpEntity, TextResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new TextResponse("Successfully denied driving"), response.getBody());
    }
    @Test
    void should_cancel_driving_when_current_drivingID_is_valid(){ //current driving je waiting i taj ID je prosledjen
        KeyPairDTO keyPairDTO = new KeyPairDTO(P_SARA_SECRET_KEY, P_SARA_E_ADDRESS);
        pay(keyPairDTO);
        setUpHeaders(LoginDTOUtil.getDriverPredrag());
        Long drivingId = 3L;

        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO("Reason", drivingId);
        HttpEntity<CancellationReasonDTO> httpEntity = new HttpEntity<>(cancellationReasonDTO, headers);
        ResponseEntity<TextResponse> response = testRestTemplate
                .exchange("/drivings/reject-regular-driver",  HttpMethod.POST, httpEntity, TextResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new TextResponse("Successfully denied driving"), response.getBody());
    }
    private void pay(KeyPairDTO keyPairDTO){
        setUpHeaders(LoginDTOUtil.getPassengerSara());

        CurrentPayingDTO paymentData = new CurrentPayingDTO(true, keyPairDTO);
        HttpEntity<CurrentPayingDTO> httpEntityRunDriving = new HttpEntity<>(paymentData, headers);

        ResponseEntity<ErrorMessage> responseRunDriving = testRestTemplate
                .exchange("/payment/pay",  HttpMethod.PUT, httpEntityRunDriving, ErrorMessage.class);
    }
}
