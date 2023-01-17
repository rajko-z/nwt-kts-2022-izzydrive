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
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.car.CarAccommodation;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverLocker;
import com.izzydrive.backend.model.users.DriverStatus;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.CarService;
import com.izzydrive.backend.service.DriverLockerService;
import com.izzydrive.backend.service.DrivingFinderService;
import com.izzydrive.backend.service.maps.MapService;
import com.izzydrive.backend.service.users.DriverService;
import com.izzydrive.backend.service.users.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import com.izzydrive.backend.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class DrivingFinderServiceImpl implements DrivingFinderService {

    private final DriverService driverService;

    private final CarService carService;

    private final MapService mapService;

    private final PassengerService passengerService;

    private final DriverLockerService driverLockerService;

    @Override
    public List<DrivingOptionDTO> getSimpleDrivingOptions(List<AddressOnMapDTO> locations) {
        validateAllLocationsForSimpleRequest(locations);

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
        validateDrivingFinderRequest(request);

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
        //do ovde isto

        for (Driver driver : getAllPossibleDriversForDriving()) { //+da nema rezervaciju u getAllPssible...
            CalculatedRouteDTO fromDriverToStart =
                    this.driverService.getCalculatedRouteFromDriverToStart(driver.getEmail(), startLocation);//ovo samo ako je pola sata do sat vremena pre voznje zakazana voznja

            //driverWillNotOutwork mi treba
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
                    Helper.getDurationInMinutesFromSeconds(fromDriverToStart.getDuration()),
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

    private void validateAllLocationsForSimpleRequest(List<AddressOnMapDTO> locations) {
        if (locations == null || locations.size() != 2) {
            throw new BadRequestException(ExceptionMessageConstants.ERROR_START_AND_END_LOCATION);
        }
        validateAllLocationsBelongsToNS(locations);
        validateAllLocationsForUniqueness(locations);
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
            for (int j = i + 1; j < locations.size(); ++j) {
                AddressOnMapDTO l2 = locations.get(j);
                if (l1.getName().equals(l2.getName()) || (l1.getLatitude() == l2.getLatitude() && l1.getLongitude() == l2.getLongitude())) {
                    throw new BadRequestException(ExceptionMessageConstants.INVALID_LOCATIONS_UNIQUENESS);
                }
            }
        }
    }

    private void checkIfAnyPassengerAlreadyHasRide(Set<String> linkedPassengers, String initiator) {
        Optional<Passenger> currPass = this.passengerService.findByEmailWithCurrentDriving(initiator);
        if (currPass.isEmpty()) {
            throw new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(initiator));
        }
        if (currPass.get().getCurrentDriving() != null) {
            throw new BadRequestException(ExceptionMessageConstants.YOU_ALREADY_HAVE_CURRENT_DRIVING);
        }

        for (String passengerEmail : linkedPassengers) {
            Optional<Passenger> passenger = this.passengerService.findByEmailWithCurrentDriving(passengerEmail);
            if (passenger.isEmpty()) {
                throw new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(passengerEmail));
            }
            if (passenger.get().getCurrentDriving() != null) {
                throw new BadRequestException(ExceptionMessageConstants.cantLinkPassengerThatAlreadyHasCurrentDriving(passengerEmail));
            }
            if (passengerEmail.equals(initiator)) {
                throw new BadRequestException(ExceptionMessageConstants.YOU_CAN_NOT_LINK_YOURSELF_FOR_DRIVE);
            }
        }
    }

    private void validatePassengers(DrivingFinderRequestDTO request) {
        String currPassengerEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Set<String> linkedPassengers = request.getLinkedPassengersEmails();
        if (linkedPassengers.size() > 3) {
            throw new BadRequestException(ExceptionMessageConstants.MAX_NUMBER_OF_LINKED_PASSENGERS);
        }
        if (request.getReservation()) {
            checkIfAnyPassengerAlreadyHasReservation(linkedPassengers, currPassengerEmail, request.getScheduleTime());
        } else {
            checkIfAnyPassengerAlreadyHasRide(linkedPassengers, currPassengerEmail);
        }

    }

    private void checkIfAnyPassengerAlreadyHasReservation(Set<String> linkedPassengers, String initiator, LocalDateTime time) {
        //bice findByEmailWithReservedDriving
        Optional<Passenger> currPass = this.passengerService.findByEmailWithReservedDriving(initiator);
        if (currPass.isEmpty()) {
            throw new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(initiator));
        }
        //provera da nema buducu voznju u tom vremenu
        for (Driving driving : currPass.get().getDrivings()) {
            if (driving.isReservation()) {
                checkIfPassengerHasReservationInThatTime(time, driving, currPass.get().getEmail());
            }
        }

        for (String passengerEmail : linkedPassengers) {
            Optional<Passenger> passenger = this.passengerService.findByEmailWithReservedDriving(passengerEmail);
            if (passenger.isEmpty()) {
                throw new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(passengerEmail));
            }
            for (Driving driving : passenger.get().getDrivings()) {
                if (driving.isReservation()) {
                    checkIfPassengerHasReservationInThatTime(time, driving, passenger.get().getEmail());
                }
            }
            //provera da ulinkovani korisnik nema bbuducu voznju u tom vremenu
            if (passengerEmail.equals(initiator)) {
                throw new BadRequestException(ExceptionMessageConstants.YOU_CAN_NOT_LINK_YOURSELF_FOR_DRIVE);
            }
        }
    }

    private void checkIfPassengerHasReservationInThatTime(LocalDateTime currentTime, Driving driving, String email) {
        LocalDateTime timeStart = driving.getReservationDate().minusMinutes(10);

        LocalDateTime timeEnd = driving.getReservationDate().plusMinutes((long) (driving.getDuration() / 60 + 10));
        if (currentTime.isAfter(timeStart) && currentTime.isBefore(timeEnd)) {
            //postoji vec neka voznja koja je zakazana za izabrano vreme
            throw new BadRequestException(ExceptionMessageConstants.passengerAlreadyHasReservation(email));
        }
    }


    @Override
    public void validateDrivingFinderRequest(DrivingFinderRequestDTO request) {
        validatePassengers(request);
        validateAllLocationsFromDrivingFinderRequest(request);
    }

    @Override
    public List<DrivingOptionDTO> getScheduleDrivingOptions(DrivingFinderRequestDTO request) {
        request.setScheduleTime(request.getScheduleTime().plusHours(1));
        //check time
        checkScheduleTime(request);

        //check passenger da li nemaju tada neku voznju
        //ovde mi ne treba provera da li on ima trenutnu voznju - zameni sa proverom da li nema u tom vremenu vec neku zakazanu voznju
        validateDrivingFinderRequest(request);


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

        for (Driver driver : getAllPossibleDriversForDrivingReservation()) { //+da nema rezervaciju u getAllPssible...
            calculateTimeAndFeelOptions(currentTime, fromStartToEndRoutes, startLocation, endLocation, options, driver);
        }
        return options;
    }

    private void calculateTimeAndFeelOptions(LocalDateTime currentTime, List<CalculatedRouteDTO> fromStartToEndRoutes, AddressOnMapDTO startLocation, AddressOnMapDTO endLocation, List<DrivingOptionDTO> options, Driver driver) {
        LocalDateTime timeInterval = LocalDateTime.now().plusMinutes(45);
        //ovo samo ako je 45 min pre voznje zakazana voznja
        if (currentTime.isBefore(timeInterval)) {
            CalculatedRouteDTO fromDriverToStart =
                    this.driverService.getCalculatedRouteFromDriverToStart(driver.getEmail(), startLocation);

            if (driverService.driverWillNotOutwork(fromDriverToStart, fromStartToEndRoutes, driver, endLocation)) {
                feelOptionsForReservation(fromStartToEndRoutes, driver, options);
            }
        } else {
            //ako je dosta u buducnosti
            //nemamo racunanje koliko mu treba do pocetka -- provera 8 sati
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

    private void checkScheduleTime(DrivingFinderRequestDTO request) {
        LocalDateTime timeMin = LocalDateTime.now().plusMinutes(30);
        LocalDateTime timeMax = LocalDateTime.now().plusHours(5);
        if (timeMin.isAfter(request.getScheduleTime()) || timeMax.isBefore(request.getScheduleTime())) {
            throw new BadRequestException(ExceptionMessageConstants.INVALID_PERIOD_SCHEDULE_DRIVING);
        }
    }
}
