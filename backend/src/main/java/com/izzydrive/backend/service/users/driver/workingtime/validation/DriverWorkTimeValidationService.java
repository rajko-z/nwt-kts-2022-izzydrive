package com.izzydrive.backend.service.users.driver.workingtime.validation;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.model.users.Driver;

import java.time.LocalDateTime;

public interface DriverWorkTimeValidationService {
    boolean driverNotOutwork(CalculatedRouteDTO fromDriverToStart,
                             CalculatedRouteDTO fromStartToEnd,
                             Driver driver);

    boolean driverOnTimeForFutureDrivingRegular(CalculatedRouteDTO fromDriverToStart,
                                                CalculatedRouteDTO fromStartToEnd,
                                                Driver driver,
                                                AddressOnMapDTO endLocation);

    boolean driverOnTimeForFutureDrivingReservation(Driver driver,
                                                    AddressOnMapDTO startLocation,
                                                    LocalDateTime scheduledTime);
}
