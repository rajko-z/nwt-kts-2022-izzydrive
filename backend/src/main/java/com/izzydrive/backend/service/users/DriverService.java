package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.map.DriverLocationDTO;
import com.izzydrive.backend.model.users.Driver;

import java.util.List;
import java.util.Optional;

public interface DriverService {

    void addNewDriver(DriverDTO driverDTO);
    List<UserDTO> findAllDrivers();

    List<Driver> findAllActiveDrivers();

    List<DriverLocationDTO> findAllActiveDriversLocation();

    Optional<Driver> findByEmailWithWorkingIntervals(String email);

    void save(Driver driver);

    Optional<Driver> findByEmailWithAllDrivings(String email);
}
