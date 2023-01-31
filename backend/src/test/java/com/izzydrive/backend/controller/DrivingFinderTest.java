package com.izzydrive.backend.controller;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.enumerations.IntermediateStationsOrderType;
import com.izzydrive.backend.enumerations.OptimalDrivingType;
import com.izzydrive.backend.exception.ErrorMessage;
import com.izzydrive.backend.model.car.CarAccommodation;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:application.properties"})
public class DrivingFinderTest {

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
    void should_return_options_for_all_available_users() {
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<DrivingOptionDTO[]> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, DrivingOptionDTO[].class);

        List<DrivingOptionDTO> options = Arrays.asList(response.getBody());
        assertEquals(DriverConst.DRIVERS_COUNT, options.size());

        options.forEach(o ->
                assertTrue(DriverConst.DRIVERS_EMAILS.contains(o.getDriver().getEmail())));
    }

    @Test
    void should_return_options_for_all_available_users_with_marko_as_first_option() {
        // driver marko@gmail.com only has food and baggage as option so it should be returned first
        CarAccommodation carAccommodation = new CarAccommodation(true, false, true, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<DrivingOptionDTO[]> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, DrivingOptionDTO[].class);

        List<DrivingOptionDTO> options = Arrays.asList(response.getBody());
        assertEquals(DriverConst.DRIVERS_COUNT, options.size());
        assertEquals(DriverConst.D_MARKO_EMAIL, options.get(0).getDriver().getEmail());
    }

    @Test
    void should_return_recalculated_multistations() {
        AddressOnMapDTO promenada = AddressesUtil.getPromenadaAddress();
        AddressOnMapDTO cvecara = AddressesUtil.getCvecaraDraganaBulevarCaraLazaraAddress();
        AddressOnMapDTO bulevarPatrijarha = AddressesUtil.getBulevarPatrijarhaPavla11Address();

        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setIntermediateStationsOrderType(IntermediateStationsOrderType.SYSTEM_CALCULATE);
        request.setIntermediateLocations(Arrays.asList(promenada, bulevarPatrijarha, cvecara));

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<DrivingOptionDTO[]> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, DrivingOptionDTO[].class);

        List<DrivingOptionDTO> options = Arrays.asList(response.getBody());
        assertEquals(DriverConst.DRIVERS_COUNT, options.size());

        for (DrivingOptionDTO o : options) {
            List<AddressOnMapDTO> reordered = o.getStartToEndPath().getReorderedIntermediate();
            assertNotNull(reordered);
            assertEquals(3, reordered.size());

            assertEquals(bulevarPatrijarha.getName(), reordered.get(0).getName());
            assertEquals(cvecara.getName(), reordered.get(1).getName());
            assertEquals(promenada.getName(), reordered.get(2).getName());
        }
    }

    @Test
    void should_return_options_sorted_by_driver_arrival_time() {
        CarAccommodation carAccommodation = new CarAccommodation(false, false, false, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<DrivingOptionDTO[]> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, DrivingOptionDTO[].class);

        List<DrivingOptionDTO> options = Arrays.asList(response.getBody());
        List<DrivingOptionDTO> sortedOptions = options.stream()
                        .sorted((Comparator.comparingDouble(DrivingOptionDTO::getTimeForWaiting)))
                        .collect(Collectors.toList());

        assertEquals(sortedOptions, options);
    }

    @Test
    void should_return_options_sorted_by_price() {
        CarAccommodation carAccommodation = new CarAccommodation(false, false, false, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);
        request.setOptimalDrivingType(OptimalDrivingType.CHEAPEST_RIDE);

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<DrivingOptionDTO[]> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, DrivingOptionDTO[].class);

        List<DrivingOptionDTO> options = Arrays.asList(response.getBody());
        List<DrivingOptionDTO> sortedOptions = options.stream()
                .sorted((Comparator.comparingDouble(DrivingOptionDTO::getPrice)))
                .collect(Collectors.toList());

        assertEquals(sortedOptions, options);
    }

    @Test
    void should_throw_exp_max_number_of_linked_passengers() {
        CarAccommodation carAccommodation = new CarAccommodation(false, false, false, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);

        Set<String> linkedPassengers = new HashSet<>(Arrays.asList(PassengerConst.P_KATE_EMAIL, PassengerConst.P_NATASHA_EMAIL, PassengerConst.P_SARA_EMAIL, PassengerConst.P_BOB_EMAIL));
        request.setLinkedPassengersEmails(linkedPassengers);

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.MAX_NUMBER_OF_LINKED_PASSENGERS, response.getBody().getMessage());
    }

    @Test
    void should_throw_exp_cant_link_yourself_to_ride() {
        CarAccommodation carAccommodation = new CarAccommodation(false, false, false, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);

        Set<String> linkedPassengers = new HashSet<>(List.of(PassengerConst.P_JOHN_EMAIL));
        request.setLinkedPassengersEmails(linkedPassengers);

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.YOU_CAN_NOT_LINK_YOURSELF_FOR_DRIVE, response.getBody().getMessage());
    }

    @Test
    void should_throw_error_size_of_intermediate_locations() {
        CarAccommodation carAccommodation = new CarAccommodation(false, false, false, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);

        AddressOnMapDTO i1 = AddressesUtil.getBulevarPatrijarhaPavla11Address();
        AddressOnMapDTO i2 = AddressesUtil.getMaksimaGorkog11Address();
        AddressOnMapDTO i3 = AddressesUtil.getCvecaraDraganaBulevarCaraLazaraAddress();
        AddressOnMapDTO i4 = AddressesUtil.getPromenadaAddress();
        request.setIntermediateLocations(Arrays.asList(i1,i2,i3,i4));

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.ERROR_SIZE_OF_INTERMEDIATE_LOCATIONS, response.getBody().getMessage());
    }

    @Test
    void should_throw_error_start_location_outside_of_novi_sad() {
        CarAccommodation carAccommodation = new CarAccommodation(false, false, false, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);
        request.setStartLocation(AddressesUtil.getLocationOutsideOfNoviSad());

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.LOCATION_OUTSIDE_OF_NOVI_SAD, response.getBody().getMessage());
    }


    @Test
    void should_throw_error_end_location_outside_of_novi_sad() {
        CarAccommodation carAccommodation = new CarAccommodation(false, false, false, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);
        request.setEndLocation(AddressesUtil.getLocationOutsideOfNoviSad());

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.LOCATION_OUTSIDE_OF_NOVI_SAD, response.getBody().getMessage());
    }

    @Test
    void should_throw_intermediate_location_outside_of_novi_sad() {
        CarAccommodation carAccommodation = new CarAccommodation(false, false, false, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);
        request.setIntermediateLocations(List.of(AddressesUtil.getLocationOutsideOfNoviSad()));

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.LOCATION_OUTSIDE_OF_NOVI_SAD, response.getBody().getMessage());
    }

    @Test
    void should_throw_invalid_locations_uniqueness_for_names() {
        CarAccommodation carAccommodation = new CarAccommodation(false, false, false, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);

        AddressOnMapDTO promenada = AddressesUtil.getPromenadaAddress();
        request.setStartLocation(promenada);
        request.setIntermediateLocations(List.of(promenada));

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.INVALID_LOCATIONS_UNIQUENESS, response.getBody().getMessage());
    }

    @Test
    void should_throw_invalid_locations_uniqueness_for_coordinates() {
        CarAccommodation carAccommodation = new CarAccommodation(false, false, false, false);
        DrivingFinderRequestDTO request = DrivingFinderUtil.getSimpleRequest();
        request.setCarAccommodation(carAccommodation);

        AddressOnMapDTO i1 = new AddressOnMapDTO(19.8000299,45.2421329, "a1" );
        AddressOnMapDTO i2 = new AddressOnMapDTO(19.8000299,45.2421329, "a2" );
        request.setIntermediateLocations(List.of(i1,i2));

        HttpEntity<DrivingFinderRequestDTO> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .exchange("/drivings/finder/advanced",  HttpMethod.POST, httpEntity, ErrorMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ExceptionMessageConstants.INVALID_LOCATIONS_UNIQUENESS, response.getBody().getMessage());
    }

}
