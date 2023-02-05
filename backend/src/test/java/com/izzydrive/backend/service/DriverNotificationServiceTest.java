package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.service.notification.driver.DriverNotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DriverNotificationServiceTest {

    @InjectMocks
    public DriverNotificationServiceImpl driverNotificationService;

    @Mock
    private  SimpMessagingTemplate simpMessagingTemplate;

    public static String DRIVER_EMAIL = "mika@gmail.com";

    @Test
    public void should_call_delete_current_driving_signal(){

        DrivingDTOWithLocations payload = new DrivingDTOWithLocations();
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setEmail(DRIVER_EMAIL);
        payload.setDriver(driverDTO);

        driverNotificationService.deleteCurrentDrivingSignal(DRIVER_EMAIL);
        verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/driving/loadCurrentDriving"), (Object) any());

    }
    @Test
    public void should_call_delete_next_driving_signal(){
        DrivingDTOWithLocations payload = new DrivingDTOWithLocations();
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setEmail(DRIVER_EMAIL);
        payload.setDriver(driverDTO);
        driverNotificationService.deleteNextDrivingSignal(DRIVER_EMAIL);
        verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/driving/loadNextDriving"), (Object) any());

    }
    @Test
    public void should_delete_reservation_signal() {
        driverNotificationService.deleteReservationSignal(DRIVER_EMAIL);
        verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/driving/loadReservation"), (Object) any());
    }

}
