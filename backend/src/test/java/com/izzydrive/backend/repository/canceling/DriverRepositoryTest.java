package com.izzydrive.backend.repository.canceling;

import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.repository.users.driver.DriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.izzydrive.backend.repository.canceling.DrivingRepositoryTest.createCurrentDrivingPredrag;
import static com.izzydrive.backend.repository.canceling.DrivingRepositoryTest.createReservedDrivingPredrag;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = {"classpath:application.properties", "classpath:application-cancel-driving.properties"})
public class DriverRepositoryTest {

    @Autowired
    private DriverRepository driverRepository;

    private static final long PREDRAG_CURRENT_DRIVING_ID = 3l;

    //findByEmailWithCurrentDriving
    @Test
    public void should_return_driver_with_current_driving(){
        Driver driverForComparing = createPredragDriver();
        Optional<Driver> driver  = this.driverRepository.findByEmailWithCurrentDriving(DriverConst.D_PREDRAG_EMAIL);
        assertNotNull(driver.get());
        assertEquals(driverForComparing.getId(),driver.get().getCurrentDriving().getId());
        assertEquals(driverForComparing.getCurrentDriving().getDrivingState(), driver.get().getCurrentDriving().getDrivingState());
        assertEquals(driverForComparing.getCurrentDriving().getDuration(),driver.get().getCurrentDriving().getDuration());
        assertEquals(driverForComparing.getCurrentDriving().getRoute().getId(), driver.get().getCurrentDriving().getRoute().getId());
        assertEquals(driverForComparing.getCurrentDriving().getPrice(),driver.get().getCurrentDriving().getPrice());
        assertIterableEquals(driverForComparing.getCurrentDriving().getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()), driver.get().getCurrentDriving().getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));
        assertEquals( driverForComparing.getEmail(),driver.get().getEmail());
    }

    //findByEmailWithReservation
    @Test
    public void should_return_driver_with_reservation_driving(){
        Driver driverForComparing = createPredragDriver();
        Optional<Driver> driver  = this.driverRepository.findByEmailWithReservation(DriverConst.D_PREDRAG_EMAIL);
        assertNotNull(driver.get());
        assertEquals(driverForComparing.getReservedFromClientDriving().getId(),driver.get().getReservedFromClientDriving().getId());
        assertEquals(driverForComparing.getReservedFromClientDriving().getDrivingState(), driver.get().getReservedFromClientDriving().getDrivingState());
        assertEquals(driverForComparing.getReservedFromClientDriving().getDuration(),driver.get().getReservedFromClientDriving().getDuration());
        assertEquals( driverForComparing.getReservedFromClientDriving().getRoute().getId(),driver.get().getReservedFromClientDriving().getRoute().getId());
        assertEquals(driverForComparing.getReservedFromClientDriving().getPrice(), driver.get().getReservedFromClientDriving().getPrice());
        assertIterableEquals(driverForComparing.getReservedFromClientDriving().getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()), driver.get().getReservedFromClientDriving().getPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList()));
        assertEquals(driver.get().getEmail(), driverForComparing.getEmail());
    }

    private Driver createPredragDriver(){
        Driver driver = new Driver();
        driver.setEmail(DriverConst.D_PREDRAG_EMAIL);
        driver.setFirstName("Predrag");
        driver.setId(DriverConst.D_PREDRAG_ID);
        driver.setCurrentDriving(createCurrentDrivingPredrag());
        driver.setReservedFromClientDriving(createReservedDrivingPredrag());
        return driver;

    }





}
