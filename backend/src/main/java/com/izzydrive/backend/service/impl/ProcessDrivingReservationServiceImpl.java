package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.Location;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverLocker;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.*;
import com.izzydrive.backend.service.users.DriverService;
import com.izzydrive.backend.service.users.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.izzydrive.backend.utils.Helper.getDurationInMinutesFromSeconds;

@Service
@AllArgsConstructor
public class ProcessDrivingReservationServiceImpl implements ProcessDrivingReservationService {

    private final DrivingFinderService drivingFinderService;

    private final DriverService driverService;

    private final PassengerService passengerService;

    private final DrivingService drivingService;

    private final ProcessDrivingRequestService processDrivingRequestService;

    private final NotificationService notificationService;

    private final DriverLockerService driverLockerService;

    @Transactional
    @Override
    public void processReservation(DrivingRequestDTO request) {
        drivingFinderService.validateDrivingFinderRequest(request.getDrivingFinderRequest());
        Driver driver = getDriverFromRequest(request);

        String passengerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Passenger passenger = passengerService.findByEmailWithReservedDriving(passengerEmail)
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(passengerEmail)));

        checkIfDriverIsStillAvailable(request, driver);

        Driving driving = makeAndSaveDrivingReservationFromRequest(request, driver, passenger);

        notificationService.sendNotificationNewReservationDriving(passengerEmail, driving);
        sendNotificationLinkedPassengers(request, driving);

    }

    private void sendNotificationLinkedPassengers(DrivingRequestDTO request, Driving driving) {
        for (String linkedPassenger : request.getDrivingFinderRequest().getLinkedPassengersEmails()) {
            notificationService.sendNotificationNewReservationDriving(linkedPassenger, driving);
        }
    }


    private Driving makeAndSaveDrivingReservationFromRequest(DrivingRequestDTO request,
                                                             Driver driver,
                                                             Passenger passenger) {

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
        driving.setReservation(true);
        driving.setRoute(processDrivingRequestService.getRouteFromRequest(request.getDrivingFinderRequest()));
        driving.setPassengers(passengers);
        driving.setLocations(getLocationsNeededForDriving(option.getStartToEndPath()));
        driving.setDriver(driver);
        driving.setReservationDate(request.getDrivingFinderRequest().getScheduleTime().plusHours(1));//add 1 hour

        drivingService.save(driving);
        updatePassengersReservationDrivings(passengers, driving);

        driver.setReservedFromClientDriving(driving);
        driverService.save(driver);
        return driving;
    }

    private void updatePassengersReservationDrivings(Set<Passenger> linkedPassengers, Driving driving) {
        Passenger[] pass = linkedPassengers.toArray(Passenger[]::new);

        for (Passenger p : pass) {
            p.getDrivings().add(driving);
            passengerService.save(p);
        }
    }

    private List<Passenger> getPassengersFromEmails(Set<String> passengerEmails) {
        List<Passenger> retVal = new ArrayList<>();
        for (String email : passengerEmails) {
            Passenger passenger = passengerService.findByEmailWithReservedDriving(email)
                    .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(email)));
            retVal.add(passenger);
        }
        return retVal;
    }

    private List<Location> getLocationsNeededForDriving(CalculatedRouteDTO fromStartToEnd) {
        List<Location> locations = new ArrayList<>();
        for (LocationDTO l : fromStartToEnd.getCoordinates()) {
            locations.add(new Location(l.getLat(), l.getLon(), true));
        }
        return locations;
    }

    private Driver getDriverFromRequest(DrivingRequestDTO request) {
        DriverDTO driverDTO = request.getDrivingOption().getDriver();
        return driverService.findByEmailWithAllDrivings(driverDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverDTO.getEmail()))); //ne znam da li ce mi trebati sve voznje ali mi treba findByEmail
    }

    private void checkIfDriverIsStillAvailable(DrivingRequestDTO request, Driver driver) {
        if (!driver.isActive()) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
        if(driver.getReservedFromClientDriving() != null){
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }

        DriverLocker driverLocker = this.driverLockerService.findByDriverEmail(driver.getEmail())
                .orElseThrow(()-> new BadRequestException(ExceptionMessageConstants.DRIVER_IS_AVAILABLE));

        if (driverLocker.getPassengerEmail() != null) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }

        CalculatedRouteDTO fromDriverToStart = this.driverService.getCalculateRouteFromDriverToStartWithNextDriving(driver.getEmail(), request.getDrivingFinderRequest().getStartLocation());

        if(getDurationInMinutesFromSeconds(fromDriverToStart.getDuration()) > ChronoUnit.MINUTES.between(request.getDrivingFinderRequest().getScheduleTime(), LocalDateTime.now()) ){
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }

        List<CalculatedRouteDTO> fromStartToEndRoutes = List.of(request.getDrivingOption().getStartToEndPath());

        if (!driverService.driverWillNotOutworkFuture(fromStartToEndRoutes, driver, request.getDrivingFinderRequest().getEndLocation())) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
    }
}
