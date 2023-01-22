package com.izzydrive.backend.service.drivingfinder.reservation;

import com.izzydrive.backend.converters.DriverDTOConverter;
import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.enumerations.IntermediateStationsOrderType;
import com.izzydrive.backend.enumerations.OptimalDrivingType;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverLocker;
import com.izzydrive.backend.service.users.driver.car.CarService;
import com.izzydrive.backend.service.users.driver.locker.DriverLockerService;
import com.izzydrive.backend.service.driving.validation.DrivingValidationService;
import com.izzydrive.backend.service.drivingfinder.helper.DrivingFinderHelper;
import com.izzydrive.backend.service.users.driver.workingtime.validation.DriverWorkTimeValidationServiceImpl;
import com.izzydrive.backend.service.users.driver.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DrivingFinderReservationServiceImpl implements DrivingFinderReservationService{

    private final DriverService driverService;

    private final CarService carService;

    private final DriverLockerService driverLockerService;

    private final DrivingValidationService drivingValidationService;

    private final DriverWorkTimeValidationServiceImpl driverWorkTimeValidationServiceImpl;

    private final DrivingFinderHelper drivingFinderHelper;

    @Override
    public List<DrivingOptionDTO> getScheduleDrivingOptions(DrivingFinderRequestDTO request) {
        drivingValidationService.checkReservationScheduledTime(request.getScheduleTime());

        drivingValidationService.validateDrivingFinderRequest(request);

        List<DrivingOptionDTO> options = getAllDrivingOptionsForReservation(
                drivingFinderHelper.getAllPointsFromDrivingFinderRequest(request),
                request.getOptimalDrivingType(),
                request.getIntermediateStationsOrderType(),
                request.getScheduleTime()
        );

        drivingFinderHelper.sortOptionsByCriteria(
                options,
                request.getOptimalDrivingType(),
                request.getCarAccommodation()
        );

        if (options.size() <= 5) {
            return options;
        }
        return options.subList(0, 5);
    }

    private List<DrivingOptionDTO> getAllDrivingOptionsForReservation(List<AddressOnMapDTO> points,
                                                                      OptimalDrivingType optimalType,
                                                                      IntermediateStationsOrderType interType,
                                                                      LocalDateTime scheduledTime) {
        List<CalculatedRouteDTO> fromStartToEndRoutes =
                drivingFinderHelper.getCalculatedRoutesFromStartToEnd(points, optimalType, interType);

        AddressOnMapDTO startLocation = points.get(0);
        List<DrivingOptionDTO> options = new ArrayList<>();

        for (Driver driver : getAllAvailableDriversForDrivingReservation()) {
            if (driverWorkTimeValidationServiceImpl
                    .driverOnTimeForFutureDrivingReservation(
                            driver, startLocation, scheduledTime)
            ) {
                options.addAll(getCreatedOptions(fromStartToEndRoutes, driver));
            }
        }
        return options;
    }

    private List<DrivingOptionDTO> getCreatedOptions(List<CalculatedRouteDTO> fromStartToEndRoutes,
                                                     Driver driver) {

        List<DrivingOptionDTO> retVal = new ArrayList<>();
        for (CalculatedRouteDTO route : fromStartToEndRoutes) {
            CalculatedRouteDTO driverToStartPath = new CalculatedRouteDTO();
            driverToStartPath.setDuration(0);
            DrivingOptionDTO drivingOptionDTO = new DrivingOptionDTO(
                    DriverDTOConverter.convertBasicWithCar(driver, carService),
                    new LocationDTO(driver.getLon(), driver.getLat()),
                    carService.calculatePrice(driver.getCar(), route.getDistance()),
                    route,
                    driverToStartPath, true
            );
            retVal.add(drivingOptionDTO);
        }
        return retVal;
    }

    private List<Driver> getAllAvailableDriversForDrivingReservation() {
        List<Driver> retVal = new ArrayList<>();

        for (Driver d : driverService.findAllActiveDrivers()) {
            if (d.getReservedFromClientDriving() != null) {
                continue;
            }
            Optional<DriverLocker> driverLocker = this.driverLockerService.findByDriverEmail(d.getEmail());
            if (driverLocker.isEmpty() || driverLocker.get().getPassengerEmail() == null) {
                retVal.add(d);
            }
        }
        return retVal;
    }
}
