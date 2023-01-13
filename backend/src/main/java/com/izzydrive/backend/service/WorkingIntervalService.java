package com.izzydrive.backend.service;

public interface WorkingIntervalService {
    Long getNumberOfMinutesDriverHasWorkedInLast24Hours(String driverEmail);

    void setCurrentLoggedDriverStatusToActive();

    void setCurrentLoggedDriverStatusToInActive();
}
