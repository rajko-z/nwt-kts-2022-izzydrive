package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.NewPassengerDTO;
import com.izzydrive.backend.dto.UserDTO;

import java.util.List;

public interface PassengerService {

    void registerPassenger(NewPassengerDTO newPassengerData);
    List<UserDTO> findAllPassenger();
}