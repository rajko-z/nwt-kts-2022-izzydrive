package com.izzydrive.backend.service.users.impl;

import com.izzydrive.backend.repository.users.DriverRepository;
import com.izzydrive.backend.service.users.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
}
