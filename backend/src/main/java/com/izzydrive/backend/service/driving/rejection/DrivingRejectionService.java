package com.izzydrive.backend.service.driving.rejection;

import com.izzydrive.backend.model.Driving;

public interface DrivingRejectionService {
    void rejectDrivingLinkedUser();

    void removeDrivingPaymentSessionExpired(Long drivingId);

    void rejectDriving(Driving driving);
}
