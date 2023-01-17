package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.*;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverLocker;
import com.izzydrive.backend.model.users.DriverStatus;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.*;
import com.izzydrive.backend.service.users.DriverService;
import com.izzydrive.backend.service.users.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ProcessDrivingRequestServiceImpl implements ProcessDrivingRequestService {

    private final DrivingFinderService drivingFinderService;

    private final DriverService driverService;

    private final DriverLockerService driverLockerService;

    private final PassengerService passengerService;

    private final AddressService addressService;

    private final DrivingService drivingService;

    private final NotificationService notificationService;

    @Transactional
    public void process(DrivingRequestDTO request) {
        drivingFinderService.validateDrivingFinderRequest(request.getDrivingFinderRequest());

        Driver driver = getDriverFromRequest(request);
        Passenger passenger = this.passengerService.getCurrentlyLoggedPassenger();

        CalculatedRouteDTO fromDriverToStart =
                driverService.getCalculatedRouteFromDriverToStart(driver.getEmail(), request.getDrivingFinderRequest().getStartLocation());

        checkIfDriverIsStillAvailable(request, driver, fromDriverToStart);

        lockDriverIfPossible(driver, passenger);

        Driving driving = makeAndSaveDrivingFromRequest(request, driver, passenger, fromDriverToStart);

        sendNotificationLinkedPassengers(request, driving);
    }

    private void sendNotificationLinkedPassengers(DrivingRequestDTO request, Driving driving) {
        for (String linkedPassenger : request.getDrivingFinderRequest().getLinkedPassengersEmails()) {
            notificationService.sendNotificationNewDriving(linkedPassenger, driving);
        }
    }

    private void lockDriverIfPossible(Driver driver, Passenger passenger) {
        try {
            Optional<DriverLocker> driverLocker = this.driverLockerService.findByDriverEmail(driver.getEmail());
            if (driverLocker.isEmpty()) {
                DriverLocker lock = new DriverLocker(driver.getEmail(), passenger.getEmail(), 1);
                this.driverLockerService.save(lock);
            } else if (driverLocker.get().getPassengerEmail() != null) {
                throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
            } else {
                driverLocker.get().setPassengerEmail(passenger.getEmail());
                driverLockerService.save(driverLocker.get());
            }
        } catch (OptimisticLockException ex) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
    }

    private Driving makeAndSaveDrivingFromRequest(DrivingRequestDTO request,
                                                  Driver driver,
                                                  Passenger passenger,
                                                  CalculatedRouteDTO fromDriverToStart) {

        DrivingOptionDTO option = request.getDrivingOption();
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(passenger);
        passengers.addAll(getPassengersFromEmails(request.getDrivingFinderRequest().getLinkedPassengersEmails()));

        Driving driving = new Driving();
        driving.setDrivingState(DrivingState.PAYMENT);
        driving.setCreationDate(LocalDateTime.now());
        driving.setDistance(option.getStartToEndPath().getDistance());
        driving.setDuration(option.getStartToEndPath().getDuration());
        driving.setPrice(option.getPrice());
        driving.setRejected(false);
        driving.setReservation(false);
        driving.setRoute(getRouteFromRequest(request.getDrivingFinderRequest()));
        driving.setPassengers(passengers);
        driving.setLocations(getLocationsNeededForDriving(fromDriverToStart, option.getStartToEndPath()));
        driving.setDriver(driver);
        driving.setPaymentApprovalIds(passenger.getEmail());

        drivingService.save(driving);
        updatePassengersCurrentDriving(passengers, passenger, driving);
        return driving;
    }

    private void updatePassengersCurrentDriving(Set<Passenger> linkedPassengers, Passenger initiator, Driving driving) {
        initiator.setCurrentDriving(driving);
        passengerService.save(initiator);
        for (Passenger p : linkedPassengers) {
            p.setCurrentDriving(driving);
            passengerService.save(p);
        }
    }

//    private void updateDriverDrivings(Driver driver, Driving driving) {
//        if (driver.getDriverStatus().equals(DriverStatus.FREE)) {
//            driver.setDriverStatus(DriverStatus.TAKEN);
//            driver.setCurrentDriving(driving);
//        } else {
//            driver.setDriverStatus(DriverStatus.RESERVED);
//            driver.setNextDriving(driving);
//        }
//        driverService.save(driver);
//    }

    private List<Location> getLocationsNeededForDriving(CalculatedRouteDTO fromDriverToStart, CalculatedRouteDTO fromStartToEnd) {
        List<Location> locations = new ArrayList<>();
        for (LocationDTO l : fromDriverToStart.getCoordinates()) {
            locations.add(new Location(l.getLat(), l.getLon(), false));
        }
        for (LocationDTO l : fromStartToEnd.getCoordinates()) {
            locations.add(new Location(l.getLat(), l.getLon(), true));
        }
        return locations;
    }

    private List<Passenger> getPassengersFromEmails(Set<String> passengerEmails) {
        List<Passenger> retVal = new ArrayList<>();
        for (String email : passengerEmails) {
            Optional<Passenger> passenger = passengerService.findByEmailWithCurrentDriving(email);
            passenger.ifPresent(retVal::add);
        }
        return retVal;
    }

    public Route getRouteFromRequest(DrivingFinderRequestDTO request) {
        Route route = new Route();
        route.setStart(getAddressFromAddressOnMap(request.getStartLocation()));
        route.setEnd(getAddressFromAddressOnMap(request.getEndLocation()));
        route.setIntermediateStations(getListOfAddressesFromAddressesOnMap(request.getIntermediateLocations()));
        return route;
    }

    private Address getAddressFromAddressOnMap(AddressOnMapDTO address) {
        Optional<Address> retVal = addressService.getAddressByName(address.getName());
        return retVal.orElseGet(() -> new Address(address.getLongitude(), address.getLatitude(), address.getName()));
    }

    private List<Address> getListOfAddressesFromAddressesOnMap(List<AddressOnMapDTO> addresses) {
        List<Address> retVal = new ArrayList<>();
        for (AddressOnMapDTO a : addresses) {
            retVal.add(getAddressFromAddressOnMap(a));
        }
        return retVal;
    }

    private void checkIfDriverIsStillAvailable(DrivingRequestDTO request, Driver driver, CalculatedRouteDTO fromDriverToStart) {
        if (!driver.isActive()) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
        checkIfDriverIsStillOnTheSamePath(request.getDrivingOption(), driver);

        List<CalculatedRouteDTO> fromStartToEndRoutes = List.of(request.getDrivingOption().getStartToEndPath());

        if (!driverService.driverWillNotOutworkAndWillBeOnTimeForFutureDriving(fromDriverToStart, fromStartToEndRoutes, driver, request.getDrivingFinderRequest().getEndLocation())) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
    }

    private Driver getDriverFromRequest(DrivingRequestDTO request) {
        DriverDTO driverDTO = request.getDrivingOption().getDriver();
        Optional<Driver> driver = driverService.findByEmailWithCurrentDrivingAndLocations(driverDTO.getEmail());
        if (driver.isEmpty()) {
            throw new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverDTO.getEmail()));
        }
        return driver.get();
    }

    private void checkIfDriverIsStillOnTheSamePath(DrivingOptionDTO option, Driver driver) {
        DriverStatus currStat = driver.getDriverStatus();
        DriverStatus prevStat = option.getDriver().getDriverStatus();
        List<LocationDTO> previousLocations = new ArrayList<>(List.of(option.getDriverCurrentLocation()));
        previousLocations.addAll(option.getDriverToStartPath().getCoordinates());

        if (currStat.equals(DriverStatus.RESERVED)) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
        if (currStat.equals(DriverStatus.FREE) && driverCurrentLocationNotInLocations(driver, previousLocations)) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
        if (prevStat.equals(DriverStatus.FREE) && !currStat.equals(DriverStatus.FREE)) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
        if (prevStat.equals(DriverStatus.ACTIVE)) {
            checkWhenPreviousDriverStateIsActive(driver, previousLocations);
        }
        if (prevStat.equals(DriverStatus.TAKEN)) {
            checkWhenPreviousDriverStateIsTaken(driver, previousLocations);
        }
    }

    private void checkWhenPreviousDriverStateIsActive(Driver driver, List<LocationDTO> locations) {
        DriverStatus currStat = driver.getDriverStatus();

        if (currStat.equals(DriverStatus.TAKEN)) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
        if (currStat.equals(DriverStatus.ACTIVE) && driverIsNoLongerActiveOnCurrentDriving(driver, locations)) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
    }

    private void checkWhenPreviousDriverStateIsTaken(Driver driver, List<LocationDTO> locations) {
        DriverStatus currStat = driver.getDriverStatus();

        if ((currStat.equals(DriverStatus.TAKEN) || currStat.equals(DriverStatus.ACTIVE)) &&
                driverIsNoLongerActiveOnCurrentDriving(driver, locations)) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
    }

    private boolean driverCurrentLocationNotInLocations(Driver driver, List<LocationDTO> locations) {
        LocationDTO currLoc = new LocationDTO(driver.getLon(), driver.getLat());
        return !locationPresentInLocations(currLoc, locations);
    }

    private boolean locationPresentInLocations(LocationDTO loc, List<LocationDTO> locations) {
        for (LocationDTO location : locations) {
            if (loc.getLat() == location.getLat() && loc.getLon() == location.getLon()) {
                return true;
            }
            if (Math.abs(loc.getLat() - location.getLat()) <= 0.002 && Math.abs(loc.getLon() - location.getLon()) <= 0.002) {
                return true;
            }
        }
        return false;
    }

    private boolean driverIsNoLongerActiveOnCurrentDriving(Driver driver, List<LocationDTO> locations) {
        LocationDTO currLoc = new LocationDTO(driver.getLon(), driver.getLat());
        Address endAddress = driver.getCurrentDriving().getRoute().getEnd();
        LocationDTO endLoc = new LocationDTO(endAddress.getLongitude(), endAddress.getLatitude());

        return !locationPresentInLocations(currLoc, locations) ||
                !locationPresentInLocations(endLoc, locations);

    }

}
