package com.izzydrive.backend.service.driving.cancelation;

import com.izzydrive.backend.dto.CancellationReasonDTO;

public interface DrivingCancellationService {
    void cancelRegularDriving(CancellationReasonDTO cancellationReasonDTO);
}
