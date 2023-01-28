package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.users.driver.DriverLocker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DriverLockerRepository extends JpaRepository<DriverLocker, Long> {

    @Query("select d from DriverLocker d where d.driverEmail = ?1")
    Optional<DriverLocker> findByDriverEmail(String driverEmail);
}
