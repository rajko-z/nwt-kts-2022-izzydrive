package com.izzydrive.backend.service;

import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static com.izzydrive.backend.utils.HelperMapper.mockPassenger;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PassengerServiceTest {

    @Autowired
    private PassengerService passengerService;

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
}

