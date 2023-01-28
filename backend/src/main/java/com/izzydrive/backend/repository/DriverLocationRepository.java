package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.users.driver.DriverLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DriverLocationRepository extends JpaRepository<DriverLocation, Long> {

    @Modifying
    @Query("update DriverLocation d set d.lat = :lat, d.lon = :lon where d.email = :email")
    void updateCoordinatesForDriver(@Param("email") String email,
                                    @Param("lat") double lat,
                                    @Param("lon") double lon);

    DriverLocation findByEmail(String email);
}
