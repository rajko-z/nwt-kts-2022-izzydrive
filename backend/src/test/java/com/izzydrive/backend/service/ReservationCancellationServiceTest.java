package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.AdminConst;
import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.dto.CancellationReasonDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Admin;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.service.driving.cancelation.reservation.ReservationCancellationServiceImpl;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.notification.driver.DriverNotificationService;
import com.izzydrive.backend.service.users.admin.AdminService;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static com.izzydrive.backend.constants.PassengerConst.P_NOT_EXISTING_EMAIL;
import static com.izzydrive.backend.utils.HelperMapper.mockDriver;
import static com.izzydrive.backend.utils.HelperMapper.mockDrivingWithRoute;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReservationCancellationServiceTest {

    @InjectMocks
    private ReservationCancellationServiceImpl reservationCancellationService;
    @Mock
    private DrivingRepository drivingRepository;
    @Mock
    private PassengerService passengerService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private DriverNotificationService driverNotificationService;
    @Mock
    private DriverService driverService;
    @Mock
    private AdminService adminService;

    private static final String REASON = "I want to cancel driving";
    private static final Long RESERVATION_DRIVING_ID = 1L;
    private static final Long INVALID_DRIVING_ID = 3L;
    private static final Long NO_MATCHING_DRIVING_ID = 3L;

    @Test
    public void should_throw_driving_doesnt_exist_when_drivingId_is_invalid() {
        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, null, true, null);
        Mockito.when(driverService.getCurrentlyLoggedDriverWithReservation()).thenReturn(driver);
        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, INVALID_DRIVING_ID);
        Mockito.when(drivingRepository.getReservationDrivingByIdWithDriverRouteAndPassengers(INVALID_DRIVING_ID)).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.reservationCancellationService.driverCancelReservation(cancellationReasonDTO));
        assertEquals("Driving with that id doesn't exists", exception.getMessage());

        verify(drivingRepository, times(0)).delete(any());
        verify(notificationService, times(0)).sendNotificationForCanceledReservation(any(), any());
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToAdmin(eq(AdminConst.ADMIN_EMAIL), any(), any(), any());
    }

    @Test
    public void should_throw_cant_find_reservation_to_cancel_when_driver_doesnt_have_reservation() {
        Driving driving = mockDrivingWithRoute(RESERVATION_DRIVING_ID, DrivingState.WAITING);
        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, null, true, null);
        driver.setReservedFromClientDriving(null);

        Mockito.when(driverService.getCurrentlyLoggedDriverWithReservation()).thenReturn(driver);
        Mockito.when(drivingRepository.getReservationDrivingByIdWithDriverRouteAndPassengers(RESERVATION_DRIVING_ID)).thenReturn(driving);

        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, RESERVATION_DRIVING_ID);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.reservationCancellationService.driverCancelReservation(cancellationReasonDTO));
        assertEquals("Can't find reservation to cancel", exception.getMessage());

        verify(drivingRepository, times(0)).delete(any());
        verify(notificationService, times(0)).sendNotificationForCanceledReservation(any(), any());
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToAdmin(eq(AdminConst.ADMIN_EMAIL), any(), any(), any());
    }

    @Test
    public void should_throw_cant_find_reservation_to_cancel_when_drivings_IDs_are_different() {
        Driving driving = mockDrivingWithRoute(RESERVATION_DRIVING_ID, DrivingState.WAITING);
        Driving notMatchingDriving = mockDrivingWithRoute(NO_MATCHING_DRIVING_ID, DrivingState.WAITING);
        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, null, true, null);
        driver.setReservedFromClientDriving(notMatchingDriving);

        Mockito.when(driverService.getCurrentlyLoggedDriverWithReservation()).thenReturn(driver);
        Mockito.when(drivingRepository.getReservationDrivingByIdWithDriverRouteAndPassengers(RESERVATION_DRIVING_ID)).thenReturn(driving);

        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, RESERVATION_DRIVING_ID);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.reservationCancellationService.driverCancelReservation(cancellationReasonDTO));
        assertEquals("Can't find reservation to cancel", exception.getMessage());

        verify(drivingRepository, times(0)).delete(any());
        verify(notificationService, times(0)).sendNotificationForCanceledReservation(any(), any());
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToAdmin(eq(AdminConst.ADMIN_EMAIL), any(), any(), any());
    }

    @Test
    public void should_cancel_reserved_driving_when_drivings_IDs_are_correct() {
        Driving driving = mockDrivingWithRoute(RESERVATION_DRIVING_ID, DrivingState.WAITING);
        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, null, true, null);
        driving.setDriver(driver);
        driver.setReservedFromClientDriving(driving);
        for (Passenger passenger : driving.getPassengers()) {
            Mockito.when(passengerService.findByEmailWithDrivings(passenger.getEmail())).thenReturn(Optional.of(passenger));
        }
        Mockito.when(driverService.getCurrentlyLoggedDriverWithReservation()).thenReturn(driver);
        Mockito.when(drivingRepository.getReservationDrivingByIdWithDriverRouteAndPassengers(RESERVATION_DRIVING_ID)).thenReturn(driving);
        Admin admin = new Admin();
        admin.setEmail(AdminConst.ADMIN_EMAIL);
        Mockito.when(adminService.findAdmin()).thenReturn(admin);

        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, RESERVATION_DRIVING_ID);

        this.reservationCancellationService.driverCancelReservation(cancellationReasonDTO);
        for (Passenger passenger : driving.getAllPassengers()) {
            assertTrue(passenger.getDrivings().stream().anyMatch(d -> !Objects.equals(d.getId(), driving.getId())));
        }
        assertNull(driving.getDriver().getReservedFromClientDriving());

        verify(drivingRepository, times(1)).delete(any());
        verify(notificationService, times(driving.getPassengers().size())).sendNotificationForCanceledReservation(any(), any());
        verify(notificationService, times(1)).sendNotificationCancelDrivingFromDriverToAdmin(eq(AdminConst.ADMIN_EMAIL), any(), any(), any());
        verify(driverNotificationService, times(1)).deleteReservationSignal(driving.getDriver().getEmail());
    }

    //User with email: %s does not exists
    @Test
    public void should_throw_user_with_email_does_not_exists_reserved_driving_when_drivings_IDs_are_correct() {
        Driving driving = mockDrivingWithRoute(RESERVATION_DRIVING_ID, DrivingState.WAITING);

        Driver driver = mockDriver(DriverConst.D_MIKA_EMAIL, null, true, null);
        driving.setDriver(driver);
        driver.setReservedFromClientDriving(driving);
        for (Passenger passenger : driving.getPassengers()) {
            Mockito.when(passengerService.findByEmailWithDrivings(passenger.getEmail())).thenReturn(Optional.of(passenger));
        }

        Passenger notExistingPassenger = new Passenger();
        notExistingPassenger.setEmail(P_NOT_EXISTING_EMAIL);
        driving.getAllPassengers().add(notExistingPassenger);
        Mockito.when(passengerService.findByEmailWithDrivings(notExistingPassenger.getEmail())).thenReturn(Optional.empty());


        Mockito.when(driverService.getCurrentlyLoggedDriverWithReservation()).thenReturn(driver);
        Mockito.when(drivingRepository.getReservationDrivingByIdWithDriverRouteAndPassengers(RESERVATION_DRIVING_ID)).thenReturn(driving);

        CancellationReasonDTO cancellationReasonDTO = new CancellationReasonDTO(REASON, RESERVATION_DRIVING_ID);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> this.reservationCancellationService.driverCancelReservation(cancellationReasonDTO));
        assertEquals("User with email: " + P_NOT_EXISTING_EMAIL + " does not exists", exception.getMessage());

        verify(drivingRepository, times(0)).delete(any());
        verify(notificationService, times(0)).sendNotificationForCanceledReservation(any(), any());
        verify(notificationService, times(0)).sendNotificationCancelDrivingFromDriverToAdmin(eq(AdminConst.ADMIN_EMAIL), any(), any(), any());
        verify(driverNotificationService, times(0)).deleteReservationSignal(driving.getDriver().getEmail());
    }
}
