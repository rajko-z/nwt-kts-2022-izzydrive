package com.izzydrive.backend.repository.executingRide;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.repository.users.driver.DriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-start-driving.properties"})
public class DriverRepositoryTest {

    @Autowired
    private DriverRepository driverRepository;

    @Test
    public void should_find_driver_by_id_with_current_driving_return_driver() {
        Optional<Driver> driver = driverRepository.findByEmailWithCurrentDriving(DriverConst.D_MILAN_EMAIL);
        assertNotNull(driver);
        assertEquals(DriverConst.D_MILAN_EMAIL, driver.get().getEmail());
        assertNotNull(driver.get().getCurrentDriving());
    }

    @Test
    public void should_find_driver_by_id_with_current_driving_return_driver_with_no_current_driving() {
        Optional<Driver> driver = driverRepository.findByEmailWithCurrentDriving(DriverConst.D_PREDRAG_EMAIL);
        assertNotNull(driver);
        assertEquals(DriverConst.D_PREDRAG_EMAIL, driver.get().getEmail());
        assertNull(driver.get().getCurrentDriving());
    }
}
