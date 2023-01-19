package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.converters.DriverDTOConverter;
import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.enumerations.IntermediateStationsOrderType;
import com.izzydrive.backend.enumerations.OptimalDrivingType;
import com.izzydrive.backend.model.car.CarAccommodation;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverLocker;
import com.izzydrive.backend.model.users.DriverStatus;
import com.izzydrive.backend.service.CarService;
import com.izzydrive.backend.service.DriverLockerService;
import com.izzydrive.backend.service.DrivingFinderService;
import com.izzydrive.backend.service.DrivingValidationService;
import com.izzydrive.backend.service.maps.MapService;
import com.izzydrive.backend.service.users.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.izzydrive.backend.utils.Constants.MINUTES_INTERVAL_BEFORE_RESERVATION;
import static com.izzydrive.backend.utils.Helper.getDurationInMinutesFromSeconds;

@Service
@AllArgsConstructor
public class DrivingFinderServiceImpl implements DrivingFinderService {

    private final DriverService driverService;

    private final CarService carService;

    private final MapService mapService;

    private final DriverLockerService driverLockerService;

    private final DrivingValidationService drivingValidationService;

    @Override
    public List<DrivingOptionDTO> getSimpleDrivingOptions(List<AddressOnMapDTO> locations) {
        drivingValidationService.validateAllLocationsForSimpleRequest(locations);

        List<DrivingOptionDTO> options = getAllDrivingOptions(
                locations,
                OptimalDrivingType.NO_PREFERENCE,
                IntermediateStationsOrderType.IN_ORDER
        );
        options.sort(Comparator.comparingDouble(o -> o.getDriverToStartPath().getDuration()));

        if (options.size() <= 5) {
            return options;
        }
        return options.subList(0, 5);
    }

    @Override
    public List<DrivingOptionDTO> getAdvancedDrivingOptions(DrivingFinderRequestDTO request) {
        drivingValidationService.validateDrivingFinderRequest(request);

        List<DrivingOptionDTO> options = getAllDrivingOptions(
                getAllPointsFromDrivingFinderRequest(request),
                request.getOptimalDrivingType(),
                request.getIntermediateStationsOrderType()
        );

        sortOptionsByCriteria(options, request.getOptimalDrivingType(), request.getCarAccommodation());

        if (options.size() <= 5) {
            return options;
        }
        return options.subList(0, 5);
    }

    private void sortOptionsByCriteria(List<DrivingOptionDTO> options,
                                       OptimalDrivingType optimalType,
                                       CarAccommodation carAccommodation) {
        options.sort((o1, o2) -> {
            int numGoodsOption1 = o1.getDriver().getCarData().getCarAccommodation().returnNumOfSimilarGoods(carAccommodation);
            int numGoodsOption2 = o2.getDriver().getCarData().getCarAccommodation().returnNumOfSimilarGoods(carAccommodation);
            if (numGoodsOption1 == numGoodsOption2) {
                if (optimalType.equals(OptimalDrivingType.CHEAPEST_RIDE)) {
                    return Double.compare(o1.getPrice(), o2.getPrice());
                }
                return Double.compare(o1.getDriverToStartPath().getDuration(), o2.getDriverToStartPath().getDuration());
            }
            return Integer.compare(numGoodsOption2, numGoodsOption1);
        });
    }

    private List<DrivingOptionDTO> getAllDrivingOptions(List<AddressOnMapDTO> points,
                                                        OptimalDrivingType optimalType,
                                                        IntermediateStationsOrderType interType) {
        List<CalculatedRouteDTO> fromStartToEndRoutes =
                getCalculatedRoutesFromStartToEnd(points, optimalType, interType);

        AddressOnMapDTO startLocation = points.get(0);
        AddressOnMapDTO endLocation = points.get(points.size() - 1);

        List<DrivingOptionDTO> options = new ArrayList<>();

        for (Driver driver : getAllPossibleDriversForDriving()) {
            CalculatedRouteDTO fromDriverToStart =
                    this.driverService.getCalculatedRouteFromDriverToStart(driver.getEmail(), startLocation);

            if (driverService.driverWillNotOutworkAndWillBeOnTimeForFutureDriving(fromDriverToStart, fromStartToEndRoutes, driver, endLocation)) {
                feelOptions(options, fromDriverToStart, fromStartToEndRoutes, driver);
            }
        }
        return options;
    }

    private void feelOptions(List<DrivingOptionDTO> options,
                             CalculatedRouteDTO fromDriverToStart,
                             List<CalculatedRouteDTO> fromStartToEndRoutes,
                             Driver driver) {
        for (CalculatedRouteDTO route : fromStartToEndRoutes) {
            DrivingOptionDTO drivingOptionDTO = new DrivingOptionDTO(
                    DriverDTOConverter.convertBasicWithCar(driver, carService),
                    new LocationDTO(driver.getLon(), driver.getLat()),
                    getDurationInMinutesFromSeconds(fromDriverToStart.getDuration()),
                    carService.calculatePrice(driver.getCar(), route.getDistance()),
                    fromDriverToStart,
                    route, false
            );
            options.add(drivingOptionDTO);
        }
    }

    private List<AddressOnMapDTO> getAllPointsFromDrivingFinderRequest(DrivingFinderRequestDTO request) {
        List<AddressOnMapDTO> retVal = new ArrayList<>();
        retVal.add(request.getStartLocation());
        retVal.addAll(request.getIntermediateLocations());
        retVal.add(request.getEndLocation());
        return retVal;
    }

    private List<Driver> getAllPossibleDriversForDriving() {
        List<Driver> retVal = new ArrayList<>();

        for (Driver d : driverService.findAllActiveDrivers()) {
            if (d.getDriverStatus().equals(DriverStatus.RESERVED)) {
                continue;
            }
            Optional<DriverLocker> driverLocker = this.driverLockerService.findByDriverEmail(d.getEmail());
            if (driverLocker.isEmpty() || driverLocker.get().getPassengerEmail() == null) {
                retVal.add(d);
            }
        }
        return retVal;
    }

    private List<CalculatedRouteDTO> getCalculatedRoutesFromStartToEnd(List<AddressOnMapDTO> points,
                                                                       OptimalDrivingType optimalType,
                                                                       IntermediateStationsOrderType interOrderType) {
        if (interOrderType.equals(IntermediateStationsOrderType.SYSTEM_CALCULATE)) {
            return mapService.getOptimalCalculatedRoutesFromPoints(points, optimalType);
        }
        return mapService.getCalculatedRoutesFromPoints(points);
    }

    @Override
    public List<DrivingOptionDTO> getScheduleDrivingOptions(DrivingFinderRequestDTO request) {
        request.setScheduleTime(request.getScheduleTime().plusHours(1));

        drivingValidationService.checkReservationScheduledTime(request.getScheduleTime());

        drivingValidationService.validateDrivingFinderRequest(request);

        List<DrivingOptionDTO> options = getAllDrivingOptionsForReservation(
                getAllPointsFromDrivingFinderRequest(request),
                request.getOptimalDrivingType(),
                request.getIntermediateStationsOrderType(),
                request.getScheduleTime()
        );

        sortOptionsByCriteria(options, request.getOptimalDrivingType(), request.getCarAccommodation());

        if (options.size() <= 5) {
            return options;
        }
        return options.subList(0, 5);
    }

    private List<DrivingOptionDTO> getAllDrivingOptionsForReservation(List<AddressOnMapDTO> points,
                                                                      OptimalDrivingType optimalType,
                                                                      IntermediateStationsOrderType interType,
                                                                      LocalDateTime currentTime) {
        List<CalculatedRouteDTO> fromStartToEndRoutes =
                getCalculatedRoutesFromStartToEnd(points, optimalType, interType);

        AddressOnMapDTO startLocation = points.get(0);
        AddressOnMapDTO endLocation = points.get(points.size() - 1);

        List<DrivingOptionDTO> options = new ArrayList<>();

        for (Driver driver : getAllPossibleDriversForDrivingReservation()) {
            calculateTimeAndFeelOptions(currentTime, fromStartToEndRoutes, startLocation, endLocation, options, driver);
        }
        return options;
    }

    private void calculateTimeAndFeelOptions(LocalDateTime currentTime, List<CalculatedRouteDTO> fromStartToEndRoutes, AddressOnMapDTO startLocation, AddressOnMapDTO endLocation, List<DrivingOptionDTO> options, Driver driver) {
        LocalDateTime timeInterval = LocalDateTime.now().plusMinutes(MINUTES_INTERVAL_BEFORE_RESERVATION);
        CalculatedRouteDTO fromDriverToStart = this.driverService.getCalculateRouteFromDriverToStartWithNextDriving(driver.getEmail(), startLocation);

        if (getDurationInMinutesFromSeconds(fromDriverToStart.getDuration()) > ChronoUnit.MINUTES.between(LocalDateTime.now(), currentTime)) {
            return;
        }

        if (currentTime.isBefore(timeInterval)) {
            if (driverService.driverWillNotOutwork(fromDriverToStart, fromStartToEndRoutes, driver, endLocation)) {
                feelOptionsForReservation(fromStartToEndRoutes, driver, options);
            }
        } else {
            if (driverService.driverWillNotOutworkFuture(fromStartToEndRoutes, driver, endLocation)) {
                feelOptionsForReservation(fromStartToEndRoutes, driver, options);
            }
        }
    }

    private void feelOptionsForReservation(List<CalculatedRouteDTO> fromStartToEndRoutes, Driver driver, List<DrivingOptionDTO> options) {
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
            options.add(drivingOptionDTO);
        }
    }

    private List<Driver> getAllPossibleDriversForDrivingReservation() {
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
