package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.model.ReservationNotification;
import com.izzydrive.backend.repository.ReservationNotificationRepository;
import com.izzydrive.backend.service.ReservationNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationNotificationImpl implements ReservationNotificationService {

    private final ReservationNotificationRepository reservationNotificationRepository;
    @Override
    public Optional<ReservationNotification> findByDrivingId(Long id) {
        return reservationNotificationRepository.findByDrivingId(id);
    }

    @Override
    public void save(ReservationNotification reservationNotification) {
        reservationNotificationRepository.save(reservationNotification);
    }
}
