package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.Driving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrivingRepository extends JpaRepository<Driving, Long> {

    @Query("SELECT d FROM Driving d INNER JOIN Route r ON d.route.id = r.id WHERE d.driver.id = ?1 ORDER BY d.startDate")
    List<Driving> findAllByDriverId(Long driverId);
}
