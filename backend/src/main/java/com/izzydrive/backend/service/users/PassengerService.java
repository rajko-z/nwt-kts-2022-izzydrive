package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.NewPassengerDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Passenger;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PassengerService {

    void registerPassenger(NewPassengerDTO newPassengerData);

    List<UserDTO> findAllPassenger();

    Optional<Passenger> findByEmailWithCurrentDriving(String email);

    Optional<Passenger> findByEmailWithReservedDriving(String email);

    /**
     * @throws com.izzydrive.backend.exception.NotFoundException if there is no loggedPassenger
     * */
    Passenger getCurrentlyLoggedPassenger();

    void save(Passenger passenger);

    void saveAndFlush(Passenger passenger);

    DrivingDTOWithLocations getCurrentDrivingForLoggedPassenger();

    boolean passengerDoesNotHavePayingData(Passenger passenger);

    void resetPassengersPayingInfo(Set<Passenger> passengers);

    List<Driving> getPassengerDrivings(Long passengerId);

    CalculatedRouteDTO getEstimatedTimeLeftForCurrentDrivingToStart();
}