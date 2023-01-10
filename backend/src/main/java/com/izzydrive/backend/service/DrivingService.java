package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.driving.DrivingDTO;

import java.util.List;

public interface DrivingService {
    List<DrivingDTO> findAllByDriverId(Long driverId);
    List<DrivingDTO> findAllByPassengerId(Long passengerId);
}
