package com.izzydrive.backend.repository.users;

import com.izzydrive.backend.model.users.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByEmail(String email);

    @Query("select d from Driver d left join fetch d.currentDriving c left join fetch c.route r where d.active = true")
    List<Driver> findAllActiveDrivers();
}
