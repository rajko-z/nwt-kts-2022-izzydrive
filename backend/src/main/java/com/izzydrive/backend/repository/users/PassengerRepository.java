package com.izzydrive.backend.repository.users;

import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Passenger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    @Query("SELECT p FROM Passenger p JOIN FETCH p.favouriteRoutes WHERE p.id = ?1")
    Optional<Passenger> findByIdWithFavoriteRoutes(Long id);

    @Query("select p from Passenger p left join fetch p.currentDriving c left join fetch c.passengers pp where p.email = ?1")
    Optional<Passenger> findByEmailWithCurrentDriving(String email);

    @Query("select p from Passenger p left join fetch p.drivings where p.email = ?1")
    Optional<Passenger> findByEmailWithReservedDriving(String email);

    @Query("SELECT p.drivings from Passenger p WHERE p.id = ?1")
    List<Driving> getPassengerDrivings(Long id);

    Optional<Passenger> findByEmail(String email);

}

