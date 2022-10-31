package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.service.DrivingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DrivingServiceImpl implements DrivingService {

    private final DrivingRepository drivingRepository;
}
