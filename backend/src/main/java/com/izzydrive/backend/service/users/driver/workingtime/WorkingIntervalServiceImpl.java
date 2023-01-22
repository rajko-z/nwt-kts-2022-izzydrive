package com.izzydrive.backend.service.users.driver.workingtime;

import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.WorkingInterval;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.service.users.driver.workingtime.WorkingIntervalService;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorkingIntervalServiceImpl implements WorkingIntervalService {


    private final DriverService driverService;

    public WorkingIntervalServiceImpl(@Lazy DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public Long getNumberOfMinutesDriverHasWorkedInLast24Hours(String driverEmail) {
        Optional<Driver> driver = driverService.findByEmailWithWorkingIntervals(driverEmail);
        if (driver.isEmpty()) {
            throw new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail));
        }
        return calculateSumInMinutesFromWorkingIntervals(driver.get().getWorkingIntervals());
    }

    private Long calculateSumInMinutesFromWorkingIntervals(List<WorkingInterval> intervals) {
        long totalMinutes = 0;
        LocalDateTime previousDay = LocalDateTime.now().minusDays(1);

        for (WorkingInterval wi : intervals) {
            if (wi.getEndTime() == null) {
                totalMinutes += Duration.between(wi.getStartTime(), LocalDateTime.now()).toMinutes();
            }
            else if (wi.getEndTime().isAfter(previousDay) && wi.getStartTime().isBefore(previousDay)) {
                totalMinutes += Duration.between(previousDay, wi.getEndTime()).toMinutes();
            }
            else if (wi.getStartTime().isAfter(previousDay)){
                totalMinutes += Duration.between(wi.getStartTime(), wi.getEndTime()).toMinutes();
            }
        }
        return totalMinutes;
    }

    @Override
    public void setCurrentLoggedDriverStatusToActive() {
        String driverEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Driver> driver = driverService.findByEmailWithWorkingIntervals(driverEmail);
        if (driver.isEmpty()) {
            throw new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail));
        }

        Optional<WorkingInterval> workingInterval = this.getLastWorkingIntervalFromDriver(driver.get());
        if (workingInterval.isPresent() && workingInterval.get().getEndTime() == null) {
            return;
        }

        WorkingInterval wt = new WorkingInterval();
        wt.setStartTime(LocalDateTime.now());
        wt.setEndTime(null);
        driver.get().getWorkingIntervals().add(wt);
        driver.get().setActive(true);
        driverService.save(driver.get());
    }

    @Override
    public void setCurrentLoggedDriverStatusToInActive() {
        String driverEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Driver> driver = driverService.findByEmailWithWorkingIntervals(driverEmail);
        if (driver.isEmpty()) {
            throw new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail));
        }

        checkDriverCurrentDrivings(driverEmail);

        Optional<WorkingInterval> workingInterval = this.getLastWorkingIntervalFromDriver(driver.get());
        workingInterval.ifPresent(interval -> interval.setEndTime(LocalDateTime.now()));
        driver.get().setActive(false);
        driverService.save(driver.get());
    }

    private void checkDriverCurrentDrivings(String email) {
        Driver driver = driverService.findByEmailWithAllDrivings(email).get();

        if (driver.getCurrentDriving() != null || driver.getNextDriving() != null) {
            throw new BadRequestException(ExceptionMessageConstants.CANT_CHANGE_DS_TO_INACTIVE_CAUSE_DRIVINGS_EXISTS);
        }
        if (driver.getReservedFromClientDriving() != null) {
            long minutesLeft = Duration.between(LocalDateTime.now(), driver.getReservedFromClientDriving().getStartDate()).toMinutes();
            if (minutesLeft <= 15) {
                throw new BadRequestException(ExceptionMessageConstants.cantChangeDSToInactiveBecauseFutureDrivingIsStartingSoon(minutesLeft));
            }
        }
    }

    private Optional<WorkingInterval> getLastWorkingIntervalFromDriver(Driver driver) {
        if (driver.getWorkingIntervals().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(driver.getWorkingIntervals().get(driver.getWorkingIntervals().size() - 1));
    }
}
