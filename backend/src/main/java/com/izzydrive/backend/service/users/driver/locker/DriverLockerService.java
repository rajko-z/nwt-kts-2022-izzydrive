package com.izzydrive.backend.service.users.driver.locker;

import com.izzydrive.backend.model.users.DriverLocker;

import java.util.Optional;

public interface DriverLockerService {
    Optional<DriverLocker> findByDriverEmail(String driverEmail);

    void saveAndFlush(DriverLocker driverLocker);

    void unlockDriver(String driverEmail);

    void lockDriverIfPossible(String driverEmail, String passengerEmail);

    boolean driverIsLocked(String driverEmail);
}
