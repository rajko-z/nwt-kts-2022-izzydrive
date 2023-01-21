package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.DrivingValidationService;
import com.izzydrive.backend.service.maps.MapService;
import com.izzydrive.backend.service.users.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.izzydrive.backend.utils.Constants.*;
import static com.izzydrive.backend.utils.Helper.getDurationInMinutesFromSeconds;

@AllArgsConstructor
@Service
public class DrivingValidationServiceImpl implements DrivingValidationService {

    private final MapService mapService;

    private final PassengerService passengerService;

    @Override
    public void checkReservationScheduledTime(LocalDateTime scheduledTime) {
        LocalDateTime timeMin = LocalDateTime.now().plusMinutes(MINUTES_BEFORE_RESERVATION);
        LocalDateTime timeMax = LocalDateTime.now().plusHours(MAX_HOUR_FOR_RESERVATION);
        if (scheduledTime.isBefore(timeMin) || scheduledTime.isAfter(timeMax)) {
            throw new BadRequestException(ExceptionMessageConstants.INVALID_PERIOD_SCHEDULE_DRIVING);
        }
    }

    @Override
    public void validateDrivingFinderRequest(DrivingFinderRequestDTO request) {
        validatePassengers(request);
        validateAllLocationsFromDrivingFinderRequest(request);
    }

    @Override
    public void validateAllLocationsForSimpleRequest(List<AddressOnMapDTO> locations) {
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
        Passenger currPass = this.passengerService.findByEmailWithCurrentDriving(initiator);
        if (currPass.getCurrentDriving() != null) {
            throw new BadRequestException(ExceptionMessageConstants.YOU_ALREADY_HAVE_CURRENT_DRIVING);
        }

        for (String passengerEmail : linkedPassengers) {
            Passenger passenger = this.passengerService.findByEmailWithCurrentDriving(passengerEmail);
            if (passenger.getCurrentDriving() != null) {
                throw new BadRequestException(ExceptionMessageConstants.cantLinkPassengerThatAlreadyHasCurrentDriving(passengerEmail));
            }
            if (passengerEmail.equals(initiator)) {
                throw new BadRequestException(ExceptionMessageConstants.YOU_CAN_NOT_LINK_YOURSELF_FOR_DRIVE);
            }
        }
    }

    private void validatePassengers(DrivingFinderRequestDTO request) {
        Passenger passenger = this.passengerService.getCurrentlyLoggedPassenger();

        Set<String> linkedPassengers = request.getLinkedPassengersEmails();
        if (linkedPassengers.size() > 3) {
            throw new BadRequestException(ExceptionMessageConstants.MAX_NUMBER_OF_LINKED_PASSENGERS);
        }
        if (request.getReservation()) {
            checkIfAnyPassengerAlreadyHasReservation(linkedPassengers, passenger.getEmail(), request.getScheduleTime());
        } else {
            checkIfAnyPassengerAlreadyHasRide(linkedPassengers, passenger.getEmail());
        }

    }

    private void checkIfAnyPassengerAlreadyHasReservation(Set<String> linkedPassengers, String initiator, LocalDateTime scheduledTime) {
        Passenger currPass = this.passengerService.findByEmailWithReservedDriving(initiator);

        for (Driving driving : currPass.getDrivings()) {
            if (driving.isReservation()) {
                checkIfPassengerHasReservationInThatTime(scheduledTime, driving, initiator);
            }
        }

        for (String passengerEmail : linkedPassengers) {
            Passenger passenger = this.passengerService.findByEmailWithReservedDriving(passengerEmail);

            for (Driving driving : passenger.getDrivings()) {
                if (driving.isReservation()) {
                    checkIfPassengerHasReservationInThatTime(scheduledTime, driving, passenger.getEmail());
                }
            }
            if (passengerEmail.equals(initiator)) {
                throw new BadRequestException(ExceptionMessageConstants.YOU_CAN_NOT_LINK_YOURSELF_FOR_DRIVE);
            }
        }
    }

    private void checkIfPassengerHasReservationInThatTime(LocalDateTime scheduledTime, Driving driving, String email) {
        LocalDateTime timeStart = driving.getReservationDate().minusMinutes(MINUTE_FOR_EXIST_RESERVATION);
        LocalDateTime timeEnd = driving.getReservationDate().plusMinutes((getDurationInMinutesFromSeconds(driving.getDuration()) + MINUTE_FOR_EXIST_RESERVATION));
        if (scheduledTime.isAfter(timeStart) && scheduledTime.isBefore(timeEnd)) {
            throw new BadRequestException(ExceptionMessageConstants.passengerAlreadyHasReservation(email));
        }
    }
}
