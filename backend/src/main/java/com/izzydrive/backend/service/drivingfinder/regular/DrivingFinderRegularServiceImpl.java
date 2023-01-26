package com.izzydrive.backend.service.drivingfinder.regular;

import com.izzydrive.backend.converters.DriverDTOConverter;
import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.enumerations.IntermediateStationsOrderType;
import com.izzydrive.backend.enumerations.OptimalDrivingType;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverStatus;
import com.izzydrive.backend.service.driving.validation.DrivingValidationService;
import com.izzydrive.backend.service.drivingfinder.helper.DrivingFinderHelper;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.driver.car.CarService;
import com.izzydrive.backend.service.users.driver.locker.DriverLockerService;
import com.izzydrive.backend.service.users.driver.routes.DriverRoutesService;
import com.izzydrive.backend.service.users.driver.workingtime.validation.DriverWorkTimeValidationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.izzydrive.backend.utils.Helper.getDurationInMinutesFromSeconds;

@Service
@AllArgsConstructor
public class DrivingFinderRegularServiceImpl implements DrivingFinderRegularService {

    private final DriverService driverService;

    private final CarService carService;

    private final DriverLockerService driverLockerService;

    private final DrivingValidationService drivingValidationService;

    private final DriverRoutesService driverRoutesService;

    private final DriverWorkTimeValidationService driverWorkTimeValidationService;

    private final DrivingFinderHelper drivingFinderHelper;


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
                drivingFinderHelper.getAllPointsFromDrivingFinderRequest(request),
                request.getOptimalDrivingType(),
                request.getIntermediateStationsOrderType()
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

    private List<DrivingOptionDTO> getAllDrivingOptions(List<AddressOnMapDTO> points,
                                                        OptimalDrivingType optimalType,
                                                        IntermediateStationsOrderType interType) {
        List<CalculatedRouteDTO> fromStartToEndRoutes =
                drivingFinderHelper.getCalculatedRoutesFromStartToEnd(points, optimalType, interType);

        AddressOnMapDTO startLocation = points.get(0);
        AddressOnMapDTO endLocation = points.get(points.size() - 1);

        List<DrivingOptionDTO> options = new ArrayList<>();

        for (Driver driver : getAllAvailableDriversForDriving()) {
            CalculatedRouteDTO fromDriverToStart =
                    this.driverRoutesService.getCalculatedRouteFromDriverToStart(driver.getEmail(), startLocation);

            if (driverWillNotOutworkAndWillBeOnTimeForFutureDriving(fromDriverToStart, fromStartToEndRoutes.get(0), driver, endLocation)) {
                options.addAll(getCreatedOptions(fromDriverToStart, fromStartToEndRoutes, driver));
            }
        }
        return options;
    }

    private List<DrivingOptionDTO> getCreatedOptions(CalculatedRouteDTO fromDriverToStart,
                                                     List<CalculatedRouteDTO> fromStartToEndRoutes,
                                                     Driver driver) {
        List<DrivingOptionDTO> retVal = new ArrayList<>();
        for (CalculatedRouteDTO route : fromStartToEndRoutes) {
            DrivingOptionDTO drivingOptionDTO = new DrivingOptionDTO(
                    DriverDTOConverter.convertBasicWithCar(driver),
                    new LocationDTO(driver.getLon(), driver.getLat()),
                    getDurationInMinutesFromSeconds(fromDriverToStart.getDuration()),
                    carService.calculatePrice(driver.getCar(), route.getDistance()),
                    fromDriverToStart,
                    route, false
            );
            retVal.add(drivingOptionDTO);
        }
        return retVal;
    }

    private boolean driverWillNotOutworkAndWillBeOnTimeForFutureDriving(CalculatedRouteDTO fromDriverToStart,
                                                                        CalculatedRouteDTO fromStartToEndRoute,
                                                                        Driver driver,
                                                                        AddressOnMapDTO endLocation)
    {
        return this.driverWorkTimeValidationService.driverNotOutwork(fromDriverToStart, fromStartToEndRoute, driver)
                &&
                this.driverWorkTimeValidationService.driverOnTimeForFutureDrivingRegular(fromDriverToStart, fromStartToEndRoute, driver, endLocation);
    }

    private List<Driver> getAllAvailableDriversForDriving() {
        List<Driver> retVal = new ArrayList<>();

        for (Driver d : driverService.findAllActiveDrivers()) {
            if (d.getDriverStatus().equals(DriverStatus.RESERVED)) {
                continue;
            }
            if (!driverLockerService.driverIsLocked(d.getEmail())) {
                retVal.add(d);
            }
        }
        return retVal;
    }
}
