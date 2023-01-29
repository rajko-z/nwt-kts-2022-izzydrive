package com.izzydrive.backend.service.driving.cancelation.reservation;

import com.izzydrive.backend.dto.CancellationReasonDTO;

public interface ReservationCancellationService {
    void passengerCancelReservation(Long drivingId);

    void driverCancelReservation(CancellationReasonDTO cancellationReasonDTO);
}
