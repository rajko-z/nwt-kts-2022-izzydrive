package com.izzydrive.backend.service.users.driver;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.map.DriverLocationDTO;
import com.izzydrive.backend.model.users.Driver;

import java.util.List;
import java.util.Optional;

public interface DriverService {

    Optional<Driver> findByEmail(String email);

    void addNewDriver(DriverDTO driverDTO);

    List<UserDTO> findAllDrivers();

    List<Driver> findAllActiveDrivers();

    List<DriverLocationDTO> findAllActiveDriversLocation();

    Optional<Driver> findByEmailWithWorkingIntervals(String email);

    void save(Driver driver);

    Optional<Driver> findByEmailWithAllDrivings(String email);

    Optional<Driver> findByEmailWithCurrentDrivingAndLocations(String email);

    void updateCoordinatesForDriver(String driverEmail, double lat, double lon);

    Optional<Driver> findByEmailWithNextDrivingAndLocations(String email);

    Optional<Driver> findByEmailWithCurrentNextAndReservedDriving(String email);


    Driver getCurrentlyLoggedDriver();

    Driver getCurrentlyLoggedDriverWithCurrentDriving();

    Driver getCurrentlyLoggedDriverWithNextDriving();
}