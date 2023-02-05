package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.service.notification.passenger.PassengerNotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PassengerNotificationServiceTest {

    @InjectMocks
    private PassengerNotificationServiceImpl passengerNotificationService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    //sendRefreshedDriving
    @Test
    public void should_send_refreshed_driving_notification(){
        DriverDTO driver = new DriverDTO();
        driver.setEmail(DriverConst.D_MIKA_EMAIL);
        DrivingDTOWithLocations drivingDTOWithLocations = new DrivingDTOWithLocations();
        drivingDTOWithLocations.setDriver(driver);
        passengerNotificationService.sendRefreshedDriving(drivingDTOWithLocations);
        verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/driving/refreshedDrivingForPassengers"), (Object) any());
    }

    @Test
    void should_send_signal_that_ride_has_start(){
        List<String> passengers = new ArrayList<>(Collections.singleton(PassengerConst.P_JOHN_EMAIL));
        passengerNotificationService.sendSignalThatRideHasStart(passengers);
        verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/driving/rideStarted"), (Object) any());
    }
}
