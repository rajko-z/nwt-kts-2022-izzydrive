package com.izzydrive.backend.repository.users;

import com.izzydrive.backend.model.users.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    @Query("SELECT p FROM Passenger p JOIN FETCH p.favouriteRoutes WHERE p.id = ?1")
    Optional<Passenger> findByIdWithFavoriteRoutes(Long id);

    @Query("select p from Passenger p left join fetch p.currentDriving c left join fetch c.passengers pp where p.email = ?1")
    Optional<Passenger> findByEmailWithCurrentDriving(String email);
}

