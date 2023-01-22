package com.izzydrive.backend.service.users.driver.workingtime.validation;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.service.users.driver.workingtime.WorkingIntervalService;
import com.izzydrive.backend.service.users.driver.routes.DriverRoutesService;
import com.izzydrive.backend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.izzydrive.backend.utils.Helper.getDurationInMinutesFromSeconds;

@Service
@AllArgsConstructor
public class DriverWorkTimeValidationServiceImpl implements DriverWorkTimeValidationService {

    private final WorkingIntervalService workingIntervalService;

    private final DriverRoutesService driverRoutesService;

    @Override
    public boolean driverNotOutwork(CalculatedRouteDTO fromDriverToStart,
                                    CalculatedRouteDTO fromStartToEnd,
                                    Driver driver) {
        int maxAllowed = Constants.MAX_WORKING_MINUTES;
        long minWorked = workingIntervalService.getNumberOfMinutesDriverHasWorkedInLast24Hours(driver.getEmail());

        if (minWorked >= maxAllowed) {
            return false;
        }

        minWorked += getDurationInMinutesFromSeconds(fromDriverToStart.getDuration());
        if (minWorked >= maxAllowed) {
            return false;
        }

        minWorked += getDurationInMinutesFromSeconds(fromStartToEnd.getDuration());
        return minWorked <= maxAllowed;
    }

    @Override
    public boolean driverOnTimeForFutureDrivingRegular(CalculatedRouteDTO fromDriverToStart,
                                                       CalculatedRouteDTO fromStartToEnd,
                                                       Driver driver,
                                                       AddressOnMapDTO endLocation)
    {
        if (driver.getReservedFromClientDriving() == null) {
            return true;
        }

        int totalTimeNeededToGetToStartOfFutureDriving =
                getDurationInMinutesFromSeconds(fromDriverToStart.getDuration()) +
                getDurationInMinutesFromSeconds(fromStartToEnd.getDuration()) +
                getDurationInMinutesFromSeconds(this.driverRoutesService.getRouteFromEndLocationToStartOfFutureDriving(endLocation, driver).getDuration());

        LocalDateTime estimatedArrival = LocalDateTime.now().plusMinutes(totalTimeNeededToGetToStartOfFutureDriving);
        LocalDateTime startTime = driver.getReservedFromClientDriving().getReservationDate();

        if (estimatedArrival.isAfter(startTime)) {
            return false;
        }

        return ChronoUnit.MINUTES.between(estimatedArrival, startTime) >= Constants.MIN_MINUTES_BEFORE_START_OF_RESERVED_DRIVING;
    }

    @Override
    public boolean driverOnTimeForFutureDrivingReservation(Driver driver,
                                                           AddressOnMapDTO startLocation,
                                                           LocalDateTime scheduledTime)
    {
        CalculatedRouteDTO fromDriverToStart = this.driverRoutesService.getCalculatedRouteFromDriverToStart(driver.getEmail(), startLocation);
        int totalTimeNeededToGetToStart = getDurationInMinutesFromSeconds(fromDriverToStart.getDuration());
        long timeForReservationToStart = ChronoUnit.MINUTES.between(LocalDateTime.now(), scheduledTime);
        long timeWhenDriverIsExpectedToBeOnStart = timeForReservationToStart - Constants.MIN_MINUTES_BEFORE_START_OF_RESERVED_DRIVING;

        return totalTimeNeededToGetToStart <= timeWhenDriverIsExpectedToBeOnStart;
    }
}
