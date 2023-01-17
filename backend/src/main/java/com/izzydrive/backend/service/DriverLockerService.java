package com.izzydrive.backend.service;

import com.izzydrive.backend.model.users.DriverLocker;

import java.util.Optional;

public interface DriverLockerService {
    Optional<DriverLocker> findByDriverEmail(String driverEmail);

    void saveAndFlush(DriverLocker driverLocker);
}
