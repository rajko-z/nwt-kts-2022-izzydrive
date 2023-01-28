package com.izzydrive.backend.service.driving.execution;

import com.izzydrive.backend.model.users.driver.Driver;

public interface DrivingExecutionService {

    void startDriving();

    void endDriving();

    void stopCurrentDrivingAndMoveToNextIfExist(Driver driver);
}
