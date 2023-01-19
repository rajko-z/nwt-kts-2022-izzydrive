package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
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

    Optional<Driver> findByEmailWithCurrentDrivingAndLocations(String email);

    CalculatedRouteDTO getCalculatedRouteFromDriverToStart(String driverEmail, AddressOnMapDTO startLocation);

    CalculatedRouteDTO getEstimatedRouteLeftFromCurrentDriving(String driverEmail);

    boolean driverWillNotOutworkAndWillBeOnTimeForFutureDriving(CalculatedRouteDTO fromDriverToStart, List<CalculatedRouteDTO> fromStartToEndRoutes, Driver driver, AddressOnMapDTO endLocation);

    boolean driverWillNotOutwork(CalculatedRouteDTO fromDriverToStart,
                                 List<CalculatedRouteDTO> fromStartToEnd,
                                 Driver driver,
                                 AddressOnMapDTO endLocation);

    boolean driverWillNotOutworkFuture(List<CalculatedRouteDTO> fromStartToEnd,
                                       Driver driver,
                                       AddressOnMapDTO endLocation);

    CalculatedRouteDTO getCalculateRouteFromDriverToStartWithNextDriving(String driverEmail, AddressOnMapDTO startLocation);

    void updateCoordinatesForDriver(String driverEmail, double lat, double lon);

    // TODO:: better name and overall refactor
    CalculatedRouteDTO getEstimatedRouteLeftForDrivingThatDidNotStartUsingExistingSavedData(String driverEmail);
}
