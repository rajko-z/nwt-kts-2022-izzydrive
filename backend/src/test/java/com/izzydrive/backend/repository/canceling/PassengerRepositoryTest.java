package com.izzydrive.backend.repository.canceling;

import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.repository.users.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.izzydrive.backend.repository.canceling.DrivingRepositoryTest.createReservedDrivingPredrag;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-cancel-driving.properties"})

public class PassengerRepositoryTest {

    @Autowired
    private PassengerRepository passengerRepository;

    //findByEmailWithDrivings
    @Test
    public void should_return_passenger_with_drivings(){
        Optional<Passenger> passenger = this.passengerRepository.findByEmailWithDrivings(PassengerConst.P_SARA_EMAIL);
        Passenger passengerToCompare = createPassengerWithDriving();
        assertNotNull(passenger.get());
        assertEquals(passengerToCompare.getEmail(), passenger.get().getEmail());
        assertEquals(passengerToCompare.getDrivings().stream().map(Driving::getId).collect(Collectors.toList()),
                     passenger.get().getDrivings().stream().map(Driving::getId).collect(Collectors.toList()));
    }

    private Passenger createPassengerWithDriving(){
        Passenger passenger = new Passenger();
        passenger.setId(PassengerConst.P_SARA_ID);
        passenger.setEmail(PassengerConst.P_SARA_EMAIL);
        passenger.setDrivings(List.of(createReservedDrivingPredrag()));
        return passenger;
    }
}
