package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.service.notification.passenger.PassengerNotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PassengerNotificationServiceTest {

    @Autowired
    @InjectMocks
    private PassengerNotificationServiceImpl passengerNotificationService;

    @MockBean
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

}
