package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.Driving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrivingRepository extends JpaRepository<Driving, Long> {

    @Query("SELECT d FROM Driving d INNER JOIN Route r ON d.route.id = r.id WHERE d.driver.id = ?1 ORDER BY d.startDate")
    List<Driving> findAllByDriverId(Long driverId);

    @Query("SELECT d FROM Driving d LEFT JOIN Passenger p ON p.id = ?1 LEFT JOIN Route r ON d.route.id = r.id WHERE ?1 IN (SELECT p.id FROM d.passengers as p) ORDER BY d.startDate")
    List<Driving>findAllByPassengerId(Long passengerId);

    @Query("select d from Driving d left join fetch d.passengers p left join fetch d.driver dr left join fetch d.route r left join fetch r.intermediateStations i where d.id = ?1")
    Driving getDrivingByIdWithDriverRouteAndPassengers(Long id);

    @Query("select d from Driving d left join fetch d.locations l where d.id = ?1")
    Driving getDrivingWithLocations(Long id);
}