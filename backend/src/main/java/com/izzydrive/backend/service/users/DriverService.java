package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.NewDriverDTO;
import com.izzydrive.backend.dto.UserDTO;

import java.util.List;

public interface DriverService {

    void addNewDriver(NewDriverDTO driverDTO);
    List<UserDTO> findAllDrivers();
}
