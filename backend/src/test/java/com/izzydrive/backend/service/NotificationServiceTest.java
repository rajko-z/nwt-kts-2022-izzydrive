package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.AdminConst;
import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Admin;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.repository.NotificationRepository;
import com.izzydrive.backend.service.notification.NotificationServiceImpl;
import com.izzydrive.backend.service.users.admin.AdminService;
import com.izzydrive.backend.service.users.admin.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.stream.Collectors;

import static com.izzydrive.backend.utils.HelperMapper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private AdminService adminService;

    @Mock
    private NotificationRepository notificationRepository;

    private static String REASON = "I want to cancel driving";

    //notificationService.sendNotificationCancelDrivingFromDriverToAdmin
    @Test
    public void should_send_notification_to_admin_for_canceled_driving(){
        Driver driver = mockDriverWithCar(DriverConst.D_MIKA_ID);
        Driving driving = mockDrivingWithRoute(1L, driver);

        this.notificationService.sendNotificationCancelDrivingFromDriverToAdmin(AdminConst.ADMIN_EMAIL, driving, driver.getEmail(), REASON);
        verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/notification/cancelRideDriver"), (Object) any());
    }

    //sendNotificationCancelDrivingFromDriverToPassengers
    @Test
    public void should_send_notification_to_all_passengers_for_canceled_driving(){
        Driver driver = mockDriverWithCar(DriverConst.D_MIKA_ID);
        Driving driving = mockDrivingWithRoute(1L, driver);
        this.notificationService.sendNotificationCancelDrivingFromDriverToPassengers(driving.getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));
        verify(simpMessagingTemplate, times(driving.getPassengers().size())).convertAndSend(eq("/notification/regularDrivingCanceledPassenger"), (Object) any());
    }

    //reportDriverNotification
    @Test
    void should_send_notification_to_admin_for_report_driver_by_passenger(){
        Passenger passenger = mockPassengerWithCurrentDriving(PassengerConst.P_JOHN_EMAIL, DrivingState.ACTIVE);
        Mockito.when(adminService.findAdmin()).thenReturn(new Admin("admin0@gmail.com", "123", "admin", "adminic", "+38122187393"));
        this.notificationService.reportDriverNotification(passenger);
        verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/notification/reportDriver"), (Object) any());

    }
}
