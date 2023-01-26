package com.izzydrive.backend.service.driving.execution;

import com.izzydrive.backend.model.users.Driver;

public interface DrivingExecutionService {

    void startDriving();

    void endDriving();

    void stopCurrentDrivingAndMoveToNextIfExist(Driver driver);
}
