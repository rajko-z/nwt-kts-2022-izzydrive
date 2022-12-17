package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.DrivingDTO;

import java.util.List;

public interface DrivingService {
    List<DrivingDTO> findAllByDriverId(Long driverId);
}
