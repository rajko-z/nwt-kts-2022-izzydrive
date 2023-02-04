package com.izzydrive.backend.repository.executingRide;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.repository.users.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-start-driving.properties"})
public class PassengerRepositoryTest {

    @Autowired
    private PassengerRepository passengerRepository;

    @Test
    void should_find_passenger_by_email_with_current_driving(){
        Optional<Passenger> passenger = passengerRepository.findByEmailWithCurrentDriving(PassengerConst.P_JOHN_EMAIL);
        assertNotNull(passenger);
        assertEquals(PassengerConst.P_JOHN_EMAIL, passenger.get().getEmail());
        assertNotNull(passenger.get().getCurrentDriving());
    }
}
