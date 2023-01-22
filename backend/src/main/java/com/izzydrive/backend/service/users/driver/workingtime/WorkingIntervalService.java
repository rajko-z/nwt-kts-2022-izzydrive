package com.izzydrive.backend.service.users.driver.workingtime;

public interface WorkingIntervalService {
    Long getNumberOfMinutesDriverHasWorkedInLast24Hours(String driverEmail);

    void setCurrentLoggedDriverStatusToActive();

    void setCurrentLoggedDriverStatusToInActive();
}
