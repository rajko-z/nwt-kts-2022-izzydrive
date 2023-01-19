package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.model.users.DriverLocker;
import com.izzydrive.backend.repository.DriverLockerRepository;
import com.izzydrive.backend.service.DriverLockerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DriverLockerServiceImpl implements DriverLockerService {

    private final DriverLockerRepository driverLockerRepository;

    @Override
    public Optional<DriverLocker> findByDriverEmail(String driverEmail) {
        return this.driverLockerRepository.findByDriverEmail(driverEmail);
    }

    @Override
    public void saveAndFlush(DriverLocker driverLocker) {

        this.driverLockerRepository.saveAndFlush(driverLocker);
    }
}
