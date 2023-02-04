package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.model.users.driver.DriverStatus;
import com.izzydrive.backend.service.driving.validation.DrivingValidationServiceImpl;
import com.izzydrive.backend.service.drivingprocessing.regular.ProcessDrivingRegularServiceImpl;
import com.izzydrive.backend.service.drivingprocessing.regular.validation.DriverAvailabilityRegularValidatorImpl;
import com.izzydrive.backend.service.drivingprocessing.shared.drivingsaver.DrivingSaverFromRequestImpl;
import com.izzydrive.backend.service.notification.NotificationServiceImpl;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.driver.locker.DriverLockerServiceImpl;
import com.izzydrive.backend.service.users.driver.routes.DriverRoutesServiceImpl;
import com.izzydrive.backend.service.users.passenger.PassengerServiceImpl;
import com.izzydrive.backend.utils.CalculatedRouteUtil;
import com.izzydrive.backend.utils.DrivingFinderUtil;
import com.izzydrive.backend.utils.HelperMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcessDrivingRegularServiceTest {

    private static String DRIVER_EMAIL_TEST = "testdriver@gmail.com";
    private static String PASSENGER_EMAIL_TEST = "passenger@gmail.com";

    private static String LINKED_PASS_ONE = "linkedPassOne@gmail.com";
    private static String LINKED_PASS_TWO = "linkedPassTwo@gmail.com";

    @InjectMocks
    private ProcessDrivingRegularServiceImpl processDrivingRegularService;

    @Mock
    private DriverService driverService;

    @Mock
    private DriverLockerServiceImpl driverLockerService;

    @Mock
    private PassengerServiceImpl passengerService;

    @Mock
    private NotificationServiceImpl notificationService;

    @Mock
    private DrivingValidationServiceImpl drivingValidationService;

    @Mock
    private DriverRoutesServiceImpl driverRoutesService;

    @Mock
    private DriverAvailabilityRegularValidatorImpl driverAvailabilityRegularValidator;

    @Mock
    private DrivingSaverFromRequestImpl drivingSaverFromRequest;

    @Test
    public void testProcess() {
        DrivingRequestDTO drivingRequestDTO = DrivingFinderUtil.getSimpleDrivingRequest(DRIVER_EMAIL_TEST, DriverStatus.FREE);

        Set<String> linkedPass = new HashSet<>();
        linkedPass.add(LINKED_PASS_ONE);
        linkedPass.add(LINKED_PASS_TWO);
        drivingRequestDTO.getDrivingFinderRequest().setLinkedPassengersEmails(linkedPass);

        Driver driver = HelperMapper.mockDriver(DRIVER_EMAIL_TEST, null, true, null);
        Optional<Driver> driverOpt = Optional.of(driver);
        when(driverService.findByEmailWithCurrentDrivingAndLocations(DRIVER_EMAIL_TEST))
                .thenReturn(driverOpt);

        Passenger passenger = new Passenger();
        passenger.setEmail(PASSENGER_EMAIL_TEST);
        when(passengerService.getCurrentlyLoggedPassenger()).thenReturn(passenger);

        CalculatedRouteDTO routeFromDriverToStart = CalculatedRouteUtil.getExampleOfCalculatedRoute();
        when(driverRoutesService.getCalculatedRouteFromDriverToStart(DRIVER_EMAIL_TEST, drivingRequestDTO.getDrivingFinderRequest().getStartLocation()))
                .thenReturn(routeFromDriverToStart);

        Driving driving = new Driving();
        when(drivingSaverFromRequest.makeAndSaveDrivingFromRegularRequest(drivingRequestDTO, driver, passenger))
                .thenReturn(driving);

        processDrivingRegularService.process(drivingRequestDTO);

        verify(drivingValidationService).validateDrivingFinderRequest(drivingRequestDTO.getDrivingFinderRequest());
        verify(driverAvailabilityRegularValidator).checkIfDriverIsStillAvailable(drivingRequestDTO, driver, routeFromDriverToStart);
        verify(driverLockerService).lockDriverIfPossible(DRIVER_EMAIL_TEST, PASSENGER_EMAIL_TEST);
        verify(notificationService).sendNotificationNewDriving(LINKED_PASS_ONE, driving);
        verify(notificationService).sendNotificationNewDriving(LINKED_PASS_TWO, driving);
    }
}
