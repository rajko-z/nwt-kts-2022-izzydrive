package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.service.notification.driver.DriverNotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class DriverNotificationServiceTest {

    @Autowired
    @InjectMocks
    public DriverNotificationServiceImpl driverNotificationService;

    @MockBean
    private  SimpMessagingTemplate simpMessagingTemplate;

    public static String DRIVER_EMAIL = "mika@gmail.com";

    @Test
    public void should_call_delete_current_driving_signal(){

        DrivingDTOWithLocations payload = new DrivingDTOWithLocations();
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setEmail(DRIVER_EMAIL);
        payload.setDriver(driverDTO);

        driverNotificationService.deleteCurrentDrivingSignal(DRIVER_EMAIL);
        verify(simpMessagingTemplate, times(1)).convertAndSend((String) any(), (Object) any());

    }
    @Test
    public void should_call_delete_next_driving_signal(){
        DrivingDTOWithLocations payload = new DrivingDTOWithLocations();
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setEmail(DRIVER_EMAIL);
        payload.setDriver(driverDTO);
        driverNotificationService.deleteNextDrivingSignal(DRIVER_EMAIL);
        verify(simpMessagingTemplate, times(1)).convertAndSend((String) any(), (Object) any());

    }

}
