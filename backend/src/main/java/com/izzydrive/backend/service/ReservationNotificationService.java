package com.izzydrive.backend.service;

import com.izzydrive.backend.model.ReservationNotification;

import java.util.Optional;

public interface ReservationNotificationService {
    Optional<ReservationNotification> findByDrivingId(Long id);

    void save(ReservationNotification reservationNotification);
}
