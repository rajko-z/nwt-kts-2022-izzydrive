package com.izzydrive.backend.repository.users;

import com.izzydrive.backend.model.users.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByEmail(String email);

    @Query("select d from Driver d left join fetch d.reservedFromClientDriving rc left join fetch rc.route rcr left join fetch d.currentDriving c left join fetch c.route r where d.active = true")
    List<Driver> findAllActiveDrivers();

    @Query("select d from Driver d left join fetch d.workingIntervals w where d.email = ?1")
    Optional<Driver> findByEmailWithWorkingIntervals(String email);

    @Query("select d from Driver d left join fetch d.currentDriving cd left join fetch d.nextDriving nd left join fetch d.reservedFromClientDriving rd where d.email = ?1")
    Optional<Driver> findByEmailWithAllDrivings(String email);

    @Query("select d from Driver d left join fetch d.currentDriving cd left join fetch cd.locations l left join fetch cd.route r where d.email = ?1")
    Optional<Driver> findByEmailWithCurrentDrivingAndLocations(String email);

    @Modifying
    @Query("update Driver d set d.lat = :lat, d.lon = :lon where d.email = :email")
    void updateCoordinatesForDriver(@Param("email") String email,
                                    @Param("lat") double lat,
                                    @Param("lon") double lon);
}
