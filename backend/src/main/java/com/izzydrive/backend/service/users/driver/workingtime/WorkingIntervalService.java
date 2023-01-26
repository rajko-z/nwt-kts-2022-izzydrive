package com.izzydrive.backend.service.users.driver.workingtime;

public interface WorkingIntervalService {
    Long getNumberOfMinutesDriverHasWorkedInLast24Hours(String driverEmail);

    Long getNumberOfMinutesLoggedDriverHasWorkedInLast24Hours();

    void setCurrentLoggedDriverStatusToActive();

    void setCurrentLoggedDriverStatusToInActive();

    boolean isLoggedDriverActive();
}
