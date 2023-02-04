package com.izzydrive.backend.repository.executingRide;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.repository.DrivingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-start-driving.properties"})
public class DrivingRepositoryTest {

    @Autowired
    private DrivingRepository drivingRepository;


    @Test
    void should_get_driving_by_id_with_driver_route_passengers() {
        Driving driving = drivingRepository.getDrivingByIdWithDriverRouteAndPassengers(1L);
        assertNotNull(driving);
        assertEquals(DriverConst.D_MIKA_EMAIL, driving.getDriver().getEmail());
        assertEquals(1, driving.getRoute().getId());
        assertEquals(1, driving.getPassengers().size());
    }

    @Test
    void should_get_driving_with_locations() {
        Driving driving = drivingRepository.getDrivingWithLocations(1L);
        assertNotNull(driving);
        assertEquals(9, driving.getLocations().size());
    }
}
