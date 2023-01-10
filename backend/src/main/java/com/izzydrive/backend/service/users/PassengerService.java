package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.NewPassengerDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.model.users.Passenger;

import java.util.List;
import java.util.Optional;

public interface PassengerService {

    void registerPassenger(NewPassengerDTO newPassengerData);
    List<UserDTO> findAllPassenger();

    Optional<Passenger> findByEmailWithCurrentDriving(String email);
}