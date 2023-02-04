package com.izzydrive.backend.repository.orderingRide;

import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.repository.DrivingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-ordering-ride.properties"})
public class DrivingRepositoryTest {

    private static final double DISTANCE = 5614.2;
    private static final double DURATION = 441.2;

    @Autowired
    private DrivingRepository drivingRepository;

    @Test
    public void should_get_driving_with_passengers_and_driver() {
        Optional<Driving> driving = drivingRepository.findById(1L);
        assertTrue(driving.isPresent());
        assertEquals(2, driving.get().getPassengers().size());
        assertNotNull(driving.get().getDriver());
        assertEquals(DrivingState.FINISHED, driving.get().getDrivingState());
        assertEquals(DISTANCE, driving.get().getDistance());
        assertEquals(DURATION, driving.get().getDuration());
    }

    @Test
    public void should_not_get_driving_with_passengers_and_driver() {
        Optional<Driving> driving = drivingRepository.findById(1000000L);
        assertTrue(driving.isEmpty());
    }
}
