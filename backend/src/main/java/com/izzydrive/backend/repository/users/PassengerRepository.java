package com.izzydrive.backend.repository.users;

import com.izzydrive.backend.model.users.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    @Query("SELECT p FROM Passenger p JOIN FETCH p.favouriteRoutes WHERE p.id = ?1")
    Optional<Passenger> findByIdWithFavoriteRoutes(Long id);

    @Query("select p from Passenger p left join fetch p.currentDriving c left join fetch c.passengers pp left join fetch c.route r left join fetch c.driver d where p.email = ?1")
    Optional<Passenger> findByEmailWithCurrentDriving(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Passenger p where p.email = ?1")
    Optional<Passenger> findByEmailLocked(String email);
}

