package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.userEmail = ?1")
    List<Notification> findAllByUserEmail(String email);

    @Query("SELECT n FROM Notification n WHERE n.drivingId = ?1 AND n.userEmail = ?2")
    Optional<Notification> findByDrivingIdAndUserEmail(Long drivingId, String email);
}
