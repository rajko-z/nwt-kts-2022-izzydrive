package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.userEmail = ?1")
    List<Notification> findAllByUserEmail(String email);
}
