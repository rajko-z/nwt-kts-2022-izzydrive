package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.Driving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DrivingRepository extends JpaRepository<Driving, Long> {

    @Query("SELECT d FROM Driving d INNER JOIN Route r ON d.route.id = r.id WHERE d.driver.id = ?1 ORDER BY d.startDate")
    List<Driving> findAllByDriverId(Long driverId);

    @Query("SELECT d FROM Driving d LEFT JOIN Passenger p ON p.id = ?1 LEFT JOIN Route r ON d.route.id = r.id WHERE ?1 IN (SELECT p.id FROM d.passengers as p) ORDER BY d.startDate")
    List<Driving> findAllByPassengerId(Long passengerId);

    @Query("SELECT d FROM Driving d LEFT JOIN FETCH d.passengers p LEFT JOIN FETCH d.driver dr WHERE d.id = ?1")
    Optional<Driving> findById(Long drivingId);

    @Query("select d from Driving d left join fetch d.passengers p left join fetch d.driver dr left join fetch d.route r left join fetch r.intermediateStations i where d.id = ?1")
    Driving getDrivingByIdWithDriverRouteAndPassengers(Long id);

    @Query("select d from Driving d left join fetch d.locations l where d.id = ?1")
    Driving getDrivingWithLocations(Long id);

    @Query("select d from Driving d where d.drivingState = 'PAYMENT'")
    List<Driving> getAllDrivingsInStatusPayment();

    @Query("select d from Driving d join fetch d.allPassengers p where d.isReservation = true and d.deleted = false and p.id = ?1")
    List<Driving> getPassengerReservations(Long passengerId);

    @Query("select d from Driving d left join fetch d.passengers p where d.id = ?1")
    Optional<Driving> findByIdWithPassengers(Long id);

    @Query("select d from Driving d" +
            " left join fetch d.locations l" +
            " left join fetch d.driver dr" +
            " left join fetch d.route r" +
            " where d.id = ?1")
    Driving findByIdWithLocationsAndDriver(Long id);

    @Query("SELECT d FROM Driving d " +
            "LEFT JOIN FETCH d.allPassengers p  LEFT JOIN FETCH d.route r LEFT JOIN FETCH r.intermediateStations " +
            "LEFT JOIN FETCH d.driver dr " +
            "LEFT JOIN FETCH dr.currentDriving " +
            "LEFT JOIN FETCH dr.nextDriving WHERE d.isReservation = true")
    List<Driving> getAllReservationWithDriverAndPassengers();

    @Query("select d from Driving d join fetch d.allPassengers p " +
            "where d.deleted = false and d.startDate >= ?2 and d.endDate <= ?3 " +
            "and p.id = ?1 and d.drivingState = 'FINISHED' " +
            "order by  d.startDate")
    List<Driving> getDrivingReportForPassenger(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select d from Driving d where d.driver.id = ?1  " +
            "and d.deleted = false and d.startDate >= ?2 and d.endDate <= ?3 " +
            "and d.drivingState = 'FINISHED' " +
            "order by  d.startDate")
    List<Driving> getDrivingReportForDriver(Long userId, LocalDateTime startDate, LocalDateTime endDate);


    @Query("select distinct d from Driving d" +
            " left join fetch d.driver dr" +
            " left join fetch d.passengers pp" +
            " left join fetch d.route ro" +
            " left join fetch ro.intermediateStations i" +
            " where d.drivingState = 'ACTIVE' or d.drivingState ='WAITING'")
    List<Driving> findAllCurrentDrivings();

    @Query("select d from Driving d" +
            " left join fetch d.allPassengers pp" +
            " left join d.route r" +
            " left join r.intermediateStations i" +
            " left join fetch d.driver dr" +
            " where d.id = ?1")
    Optional<Driving> findFinishedDrivingWithPassengersRouteAndDriver(Long id);
}