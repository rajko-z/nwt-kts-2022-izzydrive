package com.izzydrive.backend.controller;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.constants.UserConst;
import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.exception.ErrorMessage;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.NotificationStatus;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.driver.DriverStatus;
import com.izzydrive.backend.service.maps.MapService;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.AddressesUtil;
import com.izzydrive.backend.utils.DrivingFinderUtil;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import com.izzydrive.backend.utils.LoginDTOUtil;
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

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:application.properties", "classpath:application-process-driving.properties"})
public class ProcessDrivingTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MapService mapService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private NotificationService notificationService;

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

    @Test
    void should_throw_exp_driver_no_longer_active() {
        HttpHeaders headers = getHeadersLoggedUser(PassengerConst.P_JOHN_EMAIL);
        DrivingFinderRequestDTO drivingFinderRequest = DrivingFinderUtil.getSimpleRequest();
        DrivingOptionDTO drivingOption = DrivingFinderUtil.getSimpleDrivingOption(DriverConst.D_MIKA_EMAIL, DriverStatus.FREE);
        DrivingRequestDTO drivingRequest = new DrivingRequestDTO(drivingFinderRequest, drivingOption);
        expectToGetDriverNoLongerAvailableException(drivingRequest, headers);
    }

    @Test
    void should_throw_exp_driver_is_reserved() {
        HttpHeaders headers = getHeadersLoggedUser(PassengerConst.P_JOHN_EMAIL);
        DrivingFinderRequestDTO drivingFinderRequest = DrivingFinderUtil.getSimpleRequest();
        DrivingOptionDTO drivingOption = DrivingFinderUtil.getSimpleDrivingOption(DriverConst.D_PREDRAG_EMAIL, DriverStatus.FREE);
        DrivingRequestDTO drivingRequest = new DrivingRequestDTO(drivingFinderRequest, drivingOption);
        expectToGetDriverNoLongerAvailableException(drivingRequest, headers);
    }

    @Test
    void should_throw_exp_driver_is_no_longer_has_free_status() {
        HttpHeaders headers = getHeadersLoggedUser(PassengerConst.P_JOHN_EMAIL);
        DrivingFinderRequestDTO drivingFinderRequest = DrivingFinderUtil.getSimpleRequest();
        DrivingOptionDTO drivingOption = DrivingFinderUtil.getSimpleDrivingOption(DriverConst.D_MILAN_EMAIL, DriverStatus.FREE);
        DrivingRequestDTO drivingRequest = new DrivingRequestDTO(drivingFinderRequest, drivingOption);
        expectToGetDriverNoLongerAvailableException(drivingRequest, headers);
    }

    @Test
    void should_throw_exp_driver_was_active_but_now_is_taken() {
        HttpHeaders headers = getHeadersLoggedUser(PassengerConst.P_JOHN_EMAIL);
        DrivingFinderRequestDTO drivingFinderRequest = DrivingFinderUtil.getSimpleRequest();
        DrivingOptionDTO drivingOption = DrivingFinderUtil.getSimpleDrivingOption(DriverConst.D_MILAN_EMAIL, DriverStatus.ACTIVE);
        DrivingRequestDTO drivingRequest = new DrivingRequestDTO(drivingFinderRequest, drivingOption);
        expectToGetDriverNoLongerAvailableException(drivingRequest, headers);
    }

    @Test
    void should_throw_exp_driver_is_locked_for_new_ride() {
        HttpHeaders headers = getHeadersLoggedUser(PassengerConst.P_JOHN_EMAIL);
        DrivingFinderRequestDTO drivingFinderRequest = DrivingFinderUtil.getSimpleRequest();
        DrivingOptionDTO drivingOption = DrivingFinderUtil.getSimpleDrivingOption(DriverConst.D_PETAR_EMAIL, DriverStatus.FREE);
        DrivingRequestDTO drivingRequest = new DrivingRequestDTO(drivingFinderRequest, drivingOption);
        expectToGetDriverNoLongerAvailableException(drivingRequest, headers);
    }

    private void expectToGetDriverNoLongerAvailableException(DrivingRequestDTO drivingRequest, HttpHeaders headers) {
        HttpEntity<DrivingRequestDTO> httpEntity = new HttpEntity<>(drivingRequest, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/process",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE, response.getBody().getMessage());
    }

    @Test
    void test_successfully_processing_of_driving_finder_request() {
        HttpHeaders headers = getHeadersLoggedUser(PassengerConst.P_KATE_EMAIL);
        int totalNumOfNotificationsBefore = getNumberOfNewDrivingNotificationsForDriver(PassengerConst.P_BOB_EMAIL);

        DrivingRequestDTO drivingRequest = setUpRequestForSuccessfullyProcessing();
        HttpEntity<DrivingRequestDTO> httpEntity = new HttpEntity<>(drivingRequest, headers);

        ResponseEntity<TextResponse> response = testRestTemplate
                .exchange("/drivings/process",  HttpMethod.POST, httpEntity, TextResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getText());

        setContextForUser(PassengerConst.P_KATE_EMAIL);
        Passenger passenger = passengerService.getCurrentlyLoggedPassenger();
        assertNotNull(passenger.getCurrentDriving());
        assertEquals(DrivingState.PAYMENT, passenger.getCurrentDriving().getDrivingState());

        int totalNumOfNotificationsAfter = getNumberOfNewDrivingNotificationsForDriver(PassengerConst.P_BOB_EMAIL);

        assertEquals(totalNumOfNotificationsBefore+1, totalNumOfNotificationsAfter);
    }

    private DrivingRequestDTO setUpRequestForSuccessfullyProcessing() {
        DrivingFinderRequestDTO drivingFinderRequest = DrivingFinderUtil.getSimpleRequest();
        AddressOnMapDTO start = AddressesUtil.getBulevarPatrijarhaPavla5Address();
        AddressOnMapDTO end = AddressesUtil.getBulevarPatrijarhaPavla11Adress();
        drivingFinderRequest.setStartLocation(start);
        drivingFinderRequest.setEndLocation(end);
        drivingFinderRequest.setLinkedPassengersEmails(new HashSet<>(List.of(PassengerConst.P_BOB_EMAIL)));

        CalculatedRouteDTO fromStartToEnd = mapService.getCalculatedRoutesFromPoints(List.of(start, end)).get(0);
        DrivingOptionDTO drivingOption = DrivingFinderUtil.getSimpleDrivingOption(DriverConst.D_MARKO_EMAIL, DriverStatus.FREE);
        drivingOption.setStartToEndPath(fromStartToEnd);

        return new DrivingRequestDTO(drivingFinderRequest, drivingOption);
    }

    private int getNumberOfNewDrivingNotificationsForDriver(String email) {
        return (int) notificationService.findAllForUserByEmail(email)
                .stream()
                .filter(n -> n.getNotificationStatus().equals(NotificationStatus.NEW_DRIVING))
                .count();
    }

    private void setContextForUser(String userEmail) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEmail, UserConst.PASSWORD));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
