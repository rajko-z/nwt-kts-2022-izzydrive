package com.izzydrive.backend.service.driving.cancelation.regular;

import com.izzydrive.backend.dto.CancellationReasonDTO;

public interface RegularDrivingCancellationService {
    void cancelRegularDriving(CancellationReasonDTO cancellationReasonDTO);
}
