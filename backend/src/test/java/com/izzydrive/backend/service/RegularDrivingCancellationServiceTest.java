package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.dto.CancellationReasonDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Admin;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.driving.cancelation.regular.RegularDrivingCancellationServiceImpl;
import com.izzydrive.backend.service.driving.execution.DrivingExecutionService;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.notification.driver.DriverNotificationService;
import com.izzydrive.backend.service.users.admin.AdminService;
import com.izzydrive.backend.service.users.driver.DriverServiceImpl;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.izzydrive.backend.utils.HelperMapper.mockDriver;
import static com.izzydrive.backend.utils.HelperMapper.mockDrivingWithRoute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegularDrivingCancellationServiceTest {

    @Mock
    private DrivingExecutionService drivingExecutionService;

    @Mock
    private PassengerService passengerService;

    @Mock
    private DrivingService drivingService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AdminService adminService;

    @Mock
    private DriverServiceImpl driverService;

    @InjectMocks
    private RegularDrivingCancellationServiceImpl regularDrivingCancellationService;

    @Mock
    private DriverNotificationService driverNotificationService;

    private static String REASON = "I want to cancel driving";
    private static Long CURRENT_DRIVING_ID = Long.valueOf(1);
    private static Long NEXT_DRIVING_ID = Long.valueOf(2);
    private static Long INVALID_DRIVING_ID = Long.valueOf(3);
    private static Long NO_MATCHING_DRIVING_ID = Long.valueOf(3);


    @Test
    public void should_call_cancel_current_driving_when_driver_and_driving_are_valid(){
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, CURRENT_DRIVING_ID);
        Driving driving = mockDrivingWithRoute(CURRENT_DRIVING_ID, DrivingState.WAITING);
        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, driving, true, null);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithCurrentDriving()).thenReturn(driver);
        Mockito.when(drivingService.findById(CURRENT_DRIVING_ID)).thenReturn(Optional.of(driving));
        Admin admin = new Admin();
        admin.setEmail("admin0@gmail.com");
        Mockito.when(adminService.findAdmin()).thenReturn(admin);

        this.regularDrivingCancellationService.cancelRegularDriving(cancellationReasonDTO);

        Mockito.when(adminService.findAdmin()).thenReturn(admin);
        verify(drivingExecutionService, times(1)).stopCurrentDrivingAndMoveToNextIfExist(driver);
        verify(passengerService, times(1)).deleteCurrentDrivingFromPassengers(driving.getPassengers());
        verify(drivingService, times(1)).delete(driving);
        verify(notificationService, times(1)).sendNotificationCancelDrivingFromDriverToAdmin(admin.getEmail(), driving, driver.getEmail(), cancellationReasonDTO.getText());
        verify(notificationService, times(1)).sendNotificationCancelDrivingFromDriverToPassengers(driving.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));
    }

    @Test
    public void should_call_cancel_next_driving_when_driver_and_driving_are_valid(){
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, NEXT_DRIVING_ID);
        Driving currentDriving = mockDrivingWithRoute(CURRENT_DRIVING_ID, DrivingState.ACTIVE);
        Driving nextDriving = mockDrivingWithRoute(NEXT_DRIVING_ID, DrivingState.WAITING);
        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, currentDriving, true, nextDriving);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithCurrentDriving()).thenReturn(driver);
        Mockito.when(drivingService.findById(CURRENT_DRIVING_ID)).thenReturn(Optional.of(currentDriving));
        Mockito.when(drivingService.findById(NEXT_DRIVING_ID)).thenReturn(Optional.of(nextDriving));
        Admin admin = new Admin();
        admin.setEmail("admin0@gmail.com");
        Mockito.when(adminService.findAdmin()).thenReturn(admin);

        this.regularDrivingCancellationService.cancelRegularDriving(cancellationReasonDTO);

        verify(drivingExecutionService, times(0)).stopCurrentDrivingAndMoveToNextIfExist(driver);
        verify(passengerService, times(1)).deleteCurrentDrivingFromPassengers(nextDriving.getPassengers());
        verify(drivingService, times(1)).delete(nextDriving);
        verify(notificationService, times(1)).sendNotificationCancelDrivingFromDriverToAdmin(admin.getEmail(), nextDriving, driver.getEmail(), cancellationReasonDTO.getText());
        verify(notificationService, times(1)).sendNotificationCancelDrivingFromDriverToPassengers(nextDriving.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));


    }

    @Test
    public void should_throw_CANT_FIND_DRIVING_TO_CANCEL_when_driver_doesnt_have_driving(){
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, CURRENT_DRIVING_ID);
        Driving driving = mockDrivingWithRoute(CURRENT_DRIVING_ID, DrivingState.WAITING);
        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, null, true, null);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithCurrentDriving()).thenReturn(driver);
        Mockito.when(drivingService.findById(CURRENT_DRIVING_ID)).thenReturn(Optional.of(driving));
        Admin admin = new Admin();
        admin.setEmail("admin0@gmail.com");
        Mockito.when(adminService.findAdmin()).thenReturn(admin);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.regularDrivingCancellationService.cancelRegularDriving(cancellationReasonDTO));
        assertEquals("Can't find driving to cancel", exception.getMessage());

        verify(drivingExecutionService, times(0)).stopCurrentDrivingAndMoveToNextIfExist(driver);
        verify(passengerService, times(0)).deleteCurrentDrivingFromPassengers(driving.getPassengers());
        verify(drivingService, times(0)).delete(driving);
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToAdmin(admin.getEmail(), driving, driver.getEmail(), cancellationReasonDTO.getText());
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToPassengers(driving.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));

    }

    @Test
    public void should_throw_CANT_FIND_DRIVING_TO_CANCEL_when_current_driving_state_not_waiting(){
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, CURRENT_DRIVING_ID);
        Driving driving = mockDrivingWithRoute(CURRENT_DRIVING_ID, DrivingState.ACTIVE);
        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, driving, true, null);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithCurrentDriving()).thenReturn(driver);
        Mockito.when(drivingService.findById(CURRENT_DRIVING_ID)).thenReturn(Optional.of(driving));
        Admin admin = new Admin();
        admin.setEmail("admin0@gmail.com");
        Mockito.when(adminService.findAdmin()).thenReturn(admin);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.regularDrivingCancellationService.cancelRegularDriving(cancellationReasonDTO));
        assertEquals("Can't find driving to cancel", exception.getMessage());

        verify(drivingExecutionService, times(0)).stopCurrentDrivingAndMoveToNextIfExist(driver);
        verify(passengerService, times(0)).deleteCurrentDrivingFromPassengers(driving.getPassengers());
        verify(drivingService, times(0)).delete(driving);
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToAdmin(admin.getEmail(), driving, driver.getEmail(), cancellationReasonDTO.getText());
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToPassengers(driving.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));
    }

    @Test
    public void should_throw_CANT_FIND_DRIVING_TO_CANCEL_when_next_driving_state_not_waiting(){
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, NEXT_DRIVING_ID);
        Driving currentDriving = mockDrivingWithRoute(CURRENT_DRIVING_ID, DrivingState.ACTIVE);
        Driving nextDriving = mockDrivingWithRoute(NEXT_DRIVING_ID, DrivingState.PAYMENT);
        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, currentDriving, true, nextDriving);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithCurrentDriving()).thenReturn(driver);
        Mockito.when(drivingService.findById(CURRENT_DRIVING_ID)).thenReturn(Optional.of(currentDriving));
        Mockito.when(drivingService.findById(NEXT_DRIVING_ID)).thenReturn(Optional.of(nextDriving));
        Admin admin = new Admin();
        admin.setEmail("admin0@gmail.com");
        Mockito.when(adminService.findAdmin()).thenReturn(admin);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.regularDrivingCancellationService.cancelRegularDriving(cancellationReasonDTO));
        assertEquals("Can't find driving to cancel", exception.getMessage());

        verify(drivingExecutionService, times(0)).stopCurrentDrivingAndMoveToNextIfExist(driver);
        verify(passengerService, times(0)).deleteCurrentDrivingFromPassengers(nextDriving.getPassengers());
        verify(drivingService, times(0)).delete(nextDriving);
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToAdmin(admin.getEmail(), nextDriving, driver.getEmail(), cancellationReasonDTO.getText());
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToPassengers(nextDriving.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));
    }

    @Test
    public void should_throw_CANT_FIND_DRIVING_TO_CANCEL_when_next_next_drivingID_not_equal_driver_current_drivingID(){
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, NO_MATCHING_DRIVING_ID);
        Driving currentDriving = mockDrivingWithRoute(CURRENT_DRIVING_ID, DrivingState.ACTIVE);
        Driving notMatchingDriving = mockDrivingWithRoute(NO_MATCHING_DRIVING_ID, DrivingState.WAITING);
        Driving nextDriving = mockDrivingWithRoute(NEXT_DRIVING_ID, DrivingState.WAITING);
        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, currentDriving, true, nextDriving);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithCurrentDriving()).thenReturn(driver);
        Mockito.when(drivingService.findById(CURRENT_DRIVING_ID)).thenReturn(Optional.of(currentDriving));
        Mockito.when(drivingService.findById(NO_MATCHING_DRIVING_ID)).thenReturn(Optional.of(notMatchingDriving));
        Mockito.when(drivingService.findById(NEXT_DRIVING_ID)).thenReturn(Optional.of(nextDriving));
        Admin admin = new Admin();
        admin.setEmail("admin0@gmail.com");
        Mockito.when(adminService.findAdmin()).thenReturn(admin);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.regularDrivingCancellationService.cancelRegularDriving(cancellationReasonDTO));
        assertEquals("Can't find driving to cancel", exception.getMessage());

        verify(drivingExecutionService, times(0)).stopCurrentDrivingAndMoveToNextIfExist(driver);
        verify(passengerService, times(0)).deleteCurrentDrivingFromPassengers(currentDriving.getPassengers());
        verify(drivingService, times(0)).delete(currentDriving);
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToAdmin(admin.getEmail(), currentDriving, driver.getEmail(), cancellationReasonDTO.getText());
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToPassengers(currentDriving.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));
    }

    @Test
    public void should_throw_CANT_FIND_DRIVING_TO_CANCEL_when_current_drivingID_not_equal_driver_current_drivingID(){
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, NO_MATCHING_DRIVING_ID);
        Driving currentDriving = mockDrivingWithRoute(CURRENT_DRIVING_ID, DrivingState.WAITING);
        Driving notMatchingDriving = mockDrivingWithRoute(NO_MATCHING_DRIVING_ID, DrivingState.WAITING);
        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, currentDriving, true, null);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithCurrentDriving()).thenReturn(driver);
        Mockito.when(drivingService.findById(CURRENT_DRIVING_ID)).thenReturn(Optional.of(currentDriving));
        Mockito.when(drivingService.findById(NO_MATCHING_DRIVING_ID)).thenReturn(Optional.of(notMatchingDriving));
        Admin admin = new Admin();
        admin.setEmail("admin0@gmail.com");
        Mockito.when(adminService.findAdmin()).thenReturn(admin);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.regularDrivingCancellationService.cancelRegularDriving(cancellationReasonDTO));
        assertEquals("Can't find driving to cancel", exception.getMessage());

        verify(drivingExecutionService, times(0)).stopCurrentDrivingAndMoveToNextIfExist(driver);
        verify(passengerService, times(0)).deleteCurrentDrivingFromPassengers(currentDriving.getPassengers());
        verify(drivingService, times(0)).delete(currentDriving);
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToAdmin(admin.getEmail(), currentDriving, driver.getEmail(), cancellationReasonDTO.getText());
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToPassengers(currentDriving.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));
    }

    @Test
    public void should_throw_DRIVING_DOESNT_EXIST_when_drivingID_is_invalid(){
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, INVALID_DRIVING_ID);
        Driving currentDriving = mockDrivingWithRoute(INVALID_DRIVING_ID, DrivingState.ACTIVE);
        Mockito.when(drivingService.findById(NEXT_DRIVING_ID)).thenReturn(Optional.empty());
        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, currentDriving, true, null);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithCurrentDriving()).thenReturn(driver);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.regularDrivingCancellationService.cancelRegularDriving(cancellationReasonDTO));
        assertEquals("Driving with that id doesn't exists", exception.getMessage());
    }




}
