package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.users.LoggedUserService;
import com.izzydrive.backend.service.users.passenger.PassengerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static com.izzydrive.backend.utils.HelperMapper.mockPassenger;
import static com.izzydrive.backend.utils.HelperMapper.mockPassengerWithCurrentDriving;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PassengerServiceTest {

    @Autowired
    private PassengerServiceImpl passengerService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private LoggedUserService loggedUserService;


    @Test
    public void should_set_current_driving_to_null_for_passenger(){
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(mockPassenger(PassengerConst.P_BOB_EMAIL));
        passengers.add(mockPassenger(PassengerConst.P_JOHN_EMAIL));
        this.passengerService.deleteCurrentDrivingFromPassengers(passengers);

        for(Passenger passenger : passengers){
            assertNull(passenger.getCurrentDriving());
        }

    }

    @Test
    void should_report_driver_is_success(){
        Passenger passenger = mockPassengerWithCurrentDriving(PassengerConst.P_JOHN_EMAIL, DrivingState.ACTIVE);
        Mockito.when(loggedUserService.getCurrentlyLoggedPassenger()).thenReturn(passenger);
        this.passengerService.reportDriver();
        verify(notificationService, times(1)).reportDriverNotification(passenger);
    }

    @Test
    void should_report_driver_is_invalid_passenger_has_not_current_driving(){
        Passenger passenger = mockPassenger(PassengerConst.P_JOHN_EMAIL);
        Mockito.when(loggedUserService.getCurrentlyLoggedPassenger()).thenReturn(passenger);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.passengerService.reportDriver());
        assertEquals("You can't report driver because you do not have currently active driving", exception.getMessage());
    }

    @Test
    void should_report_driver_is_invalid_passenger_has_current_driving_in_waiting_status(){
        Passenger passenger = mockPassengerWithCurrentDriving(PassengerConst.P_JOHN_EMAIL, DrivingState.WAITING);
        Mockito.when(loggedUserService.getCurrentlyLoggedPassenger()).thenReturn(passenger);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> this.passengerService.reportDriver());
        assertEquals("You can't report driver because you do not have currently active driving", exception.getMessage());
    }

}

