package com.izzydrive.backend.repository.orderingRide;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.model.Location;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.repository.users.driver.DriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-ordering-ride.properties"})
public class DriverRepositoryTest {

    private final static int ACTIVE_DRIVERS_COUNT = 3;

    private final static int WORKING_INTERVAL_SIZE = 4;

    private final Location LOCATION_1 = new Location(45.242176, 19.800172,  true);
    private final Location LOCATION_2 = new Location(445.24259, 19.79992,  true);
    private final Location LOCATION_3 = new Location(45.242367, 19.799138,  true);

    @Autowired
    private DriverRepository driverRepository;

    @Test
    public void test_find_all_active_drivers() {
        List<Driver> drivers = driverRepository.findAllActiveDrivers();
        assertEquals(ACTIVE_DRIVERS_COUNT, drivers.size());
        drivers.sort(Comparator.comparingDouble(User::getId));

        assertEquals(DriverConst.D_MIKA_EMAIL, drivers.get(0).getEmail());
        assertEquals(DriverConst.D_PREDRAG_EMAIL, drivers.get(1).getEmail());
        assertEquals(DriverConst.D_MILAN_EMAIL, drivers.get(2).getEmail());
    }

    @Test
    public void test_find_by_email_with_working_intervals() {
        Optional<Driver> driverOpt = driverRepository.findByEmailWithWorkingIntervals(DriverConst.D_MIKA_EMAIL);
        assertTrue(driverOpt.isPresent());
        assertEquals(WORKING_INTERVAL_SIZE, driverOpt.get().getWorkingIntervals().size());
    }

    @Test
    public void test_find_by_email_with_empty_working_intervals() {
        Optional<Driver> driverOpt = driverRepository.findByEmailWithWorkingIntervals(DriverConst.D_PREDRAG_EMAIL);
        assertTrue(driverOpt.isPresent());
        assertTrue(driverOpt.get().getWorkingIntervals().isEmpty());
    }

    @Test
    public void test_find_by_email_with_current_driving_and_locations() {
        Optional<Driver> driver = driverRepository.findByEmailWithCurrentDrivingAndLocations(DriverConst.D_MIKA_EMAIL);
        assertTrue(driver.isPresent());
        assertEquals(DriverConst.D_MIKA_EMAIL, driver.get().getEmail());
        assertNotNull(driver.get().getCurrentDriving());
        assertEquals(1, driver.get().getCurrentDriving().getId());

        List<Location> expectedLocations = Arrays.asList(LOCATION_1, LOCATION_2, LOCATION_3);
        assertNotNull(driver.get().getCurrentDriving().getLocations());
        locationsSame(expectedLocations.get(0), LOCATION_1);
        locationsSame(expectedLocations.get(1), LOCATION_2);
        locationsSame(expectedLocations.get(2), LOCATION_3);
    }

    private void locationsSame(Location l1, Location l2) {
        assertEquals(l1.getLatitude(), l2.getLatitude());
        assertEquals(l1.getLongitude(), l2.getLongitude());
        assertEquals(l1.getId(), l2.getId());
    }
}
