package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.ReservationNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReservationNotificationRepository extends JpaRepository<ReservationNotification, Long> {
    @Query("SELECT r FROM ReservationNotification r WHERE r.drivingId = ?1")
    Optional<ReservationNotification> findByDrivingId(Long id);
}
