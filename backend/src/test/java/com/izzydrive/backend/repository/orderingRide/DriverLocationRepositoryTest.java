package com.izzydrive.backend.repository.orderingRide;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.model.users.driver.DriverLocation;
import com.izzydrive.backend.repository.DriverLocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:data-postgres-ordering-ride-test.sql"})
public class DriverLocationRepositoryTest {

    private static final double LAT = 45.20;
    private static final double LON = 46.20;

    @Autowired
    private DriverLocationRepository driverLocationRepository;

    @Test
    public void test_setting_new_coordinates_for_driver() {
        driverLocationRepository.updateCoordinatesForDriver(DriverConst.D_PREDRAG_EMAIL, LAT, LON);
        DriverLocation driverLocation = driverLocationRepository.findByEmail(DriverConst.D_PREDRAG_EMAIL);
        assertEquals(LAT, driverLocation.getLat());
        assertEquals(LON, driverLocation.getLon());
    }
}
