package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.service.DrivingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DrivingServiceImpl implements DrivingService {

    private final DrivingRepository drivingRepository;

    @Override
    public List<DrivingDTO> findAllByDriverId(Long driverId) {
        return drivingRepository.findAllByDriverId(driverId)
                .stream().map(DrivingDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<DrivingDTO> findAllByPassengerId(Long passengerId) {
        List<Driving> drivingDTOS = drivingRepository.findAllByPassengerId(passengerId);
        return drivingDTOS.stream().map(DrivingDTO::new).collect(Collectors.toList());
    }
}
