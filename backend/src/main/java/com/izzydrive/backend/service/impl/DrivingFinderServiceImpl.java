package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.converters.DriverDTOConverter;
import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.enumerations.IntermediateStationsOrderType;
import com.izzydrive.backend.enumerations.OptimalDrivingType;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.car.CarAccommodation;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverStatus;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.CarService;
import com.izzydrive.backend.service.DrivingFinderService;
import com.izzydrive.backend.service.WorkingIntervalService;
import com.izzydrive.backend.service.maps.MapService;
import com.izzydrive.backend.service.users.DriverService;
import com.izzydrive.backend.service.users.PassengerService;
import com.izzydrive.backend.utils.Constants;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DrivingFinderServiceImpl implements DrivingFinderService {

    private final DriverService driverService;

    private final CarService carService;

    private final MapService mapService;

    private final PassengerService passengerService;

    private final WorkingIntervalService workingIntervalService;

    @Override
    public List<DrivingOptionDTO> getSimpleDrivingOptions(AddressOnMapDTO startLocation, AddressOnMapDTO endLocation) {
        List<DrivingOptionDTO> options = getAllDrivingOptions(
                Arrays.asList(startLocation, endLocation),
                OptimalDrivingType.NO_PREFERENCE,
                IntermediateStationsOrderType.IN_ORDER
        );
        options.sort(Comparator.comparingDouble(o -> o.getDriverToStartPath().getDuration()));

        if (options.size() <= 5) {
            return options;
        }
        return options.subList(0,5);
    }

    @Override
    public List<DrivingOptionDTO> getAdvancedDrivingOptions(DrivingFinderRequestDTO request) {
        validateLinkedPassengers(request.getLinkedPassengersEmails());
        validateAllLocationsFromDrivingFinderRequest(request);

        List<DrivingOptionDTO> options = getAllDrivingOptions(
                getAllPointsFromDrivingFinderRequest(request),
                request.getOptimalDrivingType(),
                request.getIntermediateStationsOrderType()
        );

        sortOptionsByCriteria(options, request.getOptimalDrivingType(), request.getCarAccommodation());

        if (options.size() <= 5) {
            return options;
        }
        return options.subList(0,5);
    }

    private void sortOptionsByCriteria(List<DrivingOptionDTO> options,
                                       OptimalDrivingType optimalType,
                                       CarAccommodation carAccommodation)
    {
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
                                      IntermediateStationsOrderType interType)
    {
        List<CalculatedRouteDTO> fromStartToEndRoutes =
                getCalculatedRoutesFromStartToEnd(points, optimalType, interType);

        AddressOnMapDTO startLocation = points.get(0);
        AddressOnMapDTO endLocation = points.get(points.size() - 1);

        List<DrivingOptionDTO> options = new ArrayList<>();

        for (Driver driver : getAllPossibleDriversForDriving()) {
            CalculatedRouteDTO fromDriverToStart = getCalculatedRouteFromDriverToStart(driver, startLocation);

            if (driverWillNotOutworkAndWillBeOnTimeForFutureDriving(fromDriverToStart, fromStartToEndRoutes, driver, endLocation)) {
                feelOptions(options, fromDriverToStart, fromStartToEndRoutes, driver);
            }
        }
        return options;
    }

    private void feelOptions(List<DrivingOptionDTO> options,
                             CalculatedRouteDTO fromDriverToStart,
                             List<CalculatedRouteDTO> fromStartToEndRoutes,
                             Driver driver)
    {
        for (CalculatedRouteDTO route : fromStartToEndRoutes) {
            DrivingOptionDTO drivingOptionDTO = new DrivingOptionDTO(
                    DriverDTOConverter.convertBasicWithCar(driver,carService),
                    new LocationDTO(driver.getLon(),driver.getLat()),
                    getDurationInMinutesFromSeconds(fromDriverToStart.getDuration()),
                    carService.calculatePrice(driver.getCar(),route.getDistance()),
                    fromDriverToStart,
                    route
            );
            options.add(drivingOptionDTO);
        }
    }

    private int getDurationInMinutesFromSeconds(double duration) {
        return (int)Math.ceil(duration / 60);
    }

    private boolean driverWillNotOutworkAndWillBeOnTimeForFutureDriving(CalculatedRouteDTO fromDriverToStart,
                                                                        List<CalculatedRouteDTO> fromStartToEnd,
                                                                        Driver driver,
                                                                        AddressOnMapDTO endLocation)
    {
        return driverWillNotOutwork(fromDriverToStart, fromStartToEnd, driver, endLocation) &&
               driverWillBeOnTimeForFutureDrivings(fromDriverToStart, fromStartToEnd, driver, endLocation);
    }


    private boolean driverWillNotOutwork(CalculatedRouteDTO fromDriverToStart,
                                         List<CalculatedRouteDTO> fromStartToEnd,
                                         Driver driver,
                                         AddressOnMapDTO endLocation)
    {
        int maxAllowed = Constants.MAX_WORKING_MINUTES;
        long minWorked = workingIntervalService.getNumberOfMinutesDriverHasWorkedInLast24Hours(driver.getEmail());

        if (minWorked >= maxAllowed) {
            return false;
        }

        minWorked += getDurationInMinutesFromSeconds(fromDriverToStart.getDuration());
        if (minWorked >= maxAllowed) {
            return false;
        }

        minWorked += getDurationInMinutesFromSeconds(fromStartToEnd.get(0).getDuration());
        if (minWorked > maxAllowed) {
            return false;
        }

        if (driver.getReservedFromClientDriving() == null) {
            return true;
        }


        minWorked += getDurationInMinutesFromSeconds(getRouteFromEndLocationToStartOfFutureDriving(endLocation, driver).getDuration());
        if (minWorked >= maxAllowed) {
            return false;
        }

        minWorked += getDurationInMinutesFromSeconds(driver.getReservedFromClientDriving().getDuration());
        return minWorked <= maxAllowed;
    }

    private boolean driverWillBeOnTimeForFutureDrivings(CalculatedRouteDTO fromDriverToStart,
                                                        List<CalculatedRouteDTO> fromStartToEnd,
                                                        Driver driver,
                                                        AddressOnMapDTO endLocation)
    {
        if (driver.getReservedFromClientDriving() == null) {
            return true;
        }

        int totalTimeNeededToGetToStartOfFutureDriving =
                getDurationInMinutesFromSeconds(fromDriverToStart.getDuration()) +
                getDurationInMinutesFromSeconds(fromStartToEnd.get(0).getDuration()) +
                getDurationInMinutesFromSeconds(getRouteFromEndLocationToStartOfFutureDriving(endLocation, driver).getDuration());

        LocalDateTime estimatedArrival = LocalDateTime.now().plusMinutes(totalTimeNeededToGetToStartOfFutureDriving);
        LocalDateTime startTime = driver.getReservedFromClientDriving().getStartDate();

        if (estimatedArrival.isAfter(startTime)) {
            return false;
        }

        return ChronoUnit.MINUTES.between(estimatedArrival, startTime) >= Constants.MIN_MINUTES_BEFORE_START_OF_RESERVED_DRIVING;
    }

    private CalculatedRouteDTO getRouteFromEndLocationToStartOfFutureDriving(AddressOnMapDTO endOfCurrentAddress, Driver driver) {
        Address tmp = driver.getReservedFromClientDriving().getRoute().getStart();
        AddressOnMapDTO startFutureLocation = new AddressOnMapDTO(tmp.getLongitude(), tmp.getLatitude());

        return mapService.getCalculatedRoutesFromPoints(Arrays.asList(endOfCurrentAddress, startFutureLocation)).get(0);
    }

    private List<AddressOnMapDTO> getAllPointsFromDrivingFinderRequest(DrivingFinderRequestDTO request) {
        List<AddressOnMapDTO> retVal = new ArrayList<>();
        retVal.add(request.getStartLocation());
        retVal.addAll(request.getIntermediateLocations());
        retVal.add(request.getEndLocation());
        return retVal;
    }

    private List<Driver> getAllPossibleDriversForDriving() {
        return driverService
                .findAllActiveDrivers()
                .stream()
                .filter(d -> !d.getDriverStatus().equals(DriverStatus.RESERVED))
                .collect(Collectors.toList());
    }

    private List<CalculatedRouteDTO> getCalculatedRoutesFromStartToEnd(List<AddressOnMapDTO> points,
                                                                       OptimalDrivingType optimalType,
                                                                       IntermediateStationsOrderType interOrderType)
    {
        if (interOrderType.equals(IntermediateStationsOrderType.SYSTEM_CALCULATE)) {
            return mapService.getOptimalCalculatedRoutesFromPoints(points, optimalType);
        }
        return mapService.getCalculatedRoutesFromPoints(points);
    }

    private CalculatedRouteDTO getCalculatedRouteFromDriverToStart(Driver driver, AddressOnMapDTO startLocation) {
        if (driver.getCurrentDriving() != null) {
            CalculatedRouteDTO getEstimatedRouteLeft = this.driverService.getEstimatedRouteLeftFromCurrentDriving(driver.getEmail());

            Address tmp = driver.getCurrentDriving().getRoute().getEnd();
            AddressOnMapDTO currDrivingEndLocation = new AddressOnMapDTO(tmp.getLongitude(), tmp.getLatitude());
            CalculatedRouteDTO getRouteFromCurrDrivingEndToStart = mapService
                    .getCalculatedRoutesFromPoints(Arrays.asList(currDrivingEndLocation, startLocation)).get(0);

            return mapService.concatRoutesIntoOne(Arrays.asList(getEstimatedRouteLeft, getRouteFromCurrDrivingEndToStart));
        }
        AddressOnMapDTO driverLocation = new AddressOnMapDTO(driver.getLon(), driver.getLat());
        return mapService.getCalculatedRoutesFromPoints(Arrays.asList(driverLocation, startLocation)).get(0);
    }

    private void validateAllLocationsFromDrivingFinderRequest(DrivingFinderRequestDTO request) {
        List<AddressOnMapDTO> locations = new ArrayList<>();
        locations.add(request.getStartLocation());
        locations.addAll(request.getIntermediateLocations());
        locations.add(request.getEndLocation());

        validateAllLocationsForUniqueness(locations);
        validateAllLocationsBelongsToNS(locations);
        validateSizeOfIntermediateLocations(request.getIntermediateLocations());
    }

    private void validateAllLocationsBelongsToNS(List<AddressOnMapDTO> locations) {
        for (AddressOnMapDTO l : locations) {
            if (!mapService.addressBelongsToBoundingBoxOfNS(l)) {
                throw new BadRequestException(ExceptionMessageConstants.LOCATION_OUTSIDE_OF_NOVI_SAD);
            }
        }
    }

    private void validateSizeOfIntermediateLocations(List<AddressOnMapDTO> locations) {
        if (locations.size() > 3) {
            throw new BadRequestException(ExceptionMessageConstants.ERROR_SIZE_OF_INTERMEDIATE_LOCATIONS);
        }
    }

    private void validateAllLocationsForUniqueness(List<AddressOnMapDTO> locations) {
        for (int i = 0; i < locations.size() - 1; ++i) {
            AddressOnMapDTO l1 = locations.get(i);
            for (int j = i+1; j < locations.size(); ++j) {
                AddressOnMapDTO l2 = locations.get(j);
                if (l1.getName().equals(l2.getName()) || (l1.getLatitude() == l2.getLatitude() && l1.getLongitude() == l2.getLongitude())) {
                    throw new BadRequestException(ExceptionMessageConstants.INVALID_LOCATIONS_UNIQUENESS);
                }
            }
        }
    }

    private void validateLinkedPassengers(Set<String> linkedPassengers) {
        String currPassengerEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        for (String passengerEmail: linkedPassengers) {
            Optional<Passenger> passenger = this.passengerService.findByEmailWithCurrentDriving(passengerEmail);
            if (passenger.isEmpty()) {
                throw new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(passengerEmail));
            }
            if (passenger.get().getCurrentDriving() != null) {
                throw new BadRequestException(ExceptionMessageConstants.cantLinkPassengerThatAlreadyHasCurrentDriving(passengerEmail));
            }
            if (currPassengerEmail.equals(passengerEmail)) {
                throw new BadRequestException(ExceptionMessageConstants.YOU_CAN_NOT_LINK_YOURSELF_FOR_DRIVE);
            }
        }
    }
}
