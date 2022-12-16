package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.NewPassengerDTO;

public interface PassengerService {

    void registerPassenger(NewPassengerDTO newPassengerData);

}