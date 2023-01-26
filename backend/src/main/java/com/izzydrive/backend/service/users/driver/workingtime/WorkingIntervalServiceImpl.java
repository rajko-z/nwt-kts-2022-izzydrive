package com.izzydrive.backend.service.users.driver.workingtime;

import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.WorkingInterval;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.driver.locker.DriverLockerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class WorkingIntervalServiceImpl implements WorkingIntervalService {

    private final DriverService driverService;

    private final DriverLockerService driverLockerService;

    public WorkingIntervalServiceImpl(@Lazy DriverService driverService, DriverLockerService driverLockerService) {
        this.driverService = driverService;
        this.driverLockerService = driverLockerService;
    }

    @Override
    public Long getNumberOfMinutesDriverHasWorkedInLast24Hours(String driverEmail) {
        Driver driver = driverService.findByEmailWithWorkingIntervals(driverEmail)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail)));
        return calculateSumInMinutesFromWorkingIntervals(driver.getWorkingIntervals());
    }

    @Override
    public Long getNumberOfMinutesLoggedDriverHasWorkedInLast24Hours() {
        Driver driver = driverService.findLoggedDriverWithWorkingIntervals();
        return calculateSumInMinutesFromWorkingIntervals(driver.getWorkingIntervals());
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
    @Transactional
    public void setCurrentLoggedDriverStatusToActive() {
        Driver driver = driverService.getCurrentlyLoggedDriverWithCurrentDriving();

        Optional<WorkingInterval> workingInterval = this.getLastWorkingIntervalFromDriver(driver);
        if (workingInterval.isPresent() && workingInterval.get().getEndTime() == null) {
            return;
        }

        WorkingInterval wt = new WorkingInterval();
        wt.setStartTime(LocalDateTime.now());
        wt.setEndTime(null);
        driver.getWorkingIntervals().add(wt);
        driver.setActive(true);
        driverService.save(driver);
    }

    @Override
    @Transactional
    public void setCurrentLoggedDriverStatusToInActive() {
        Driver driver = driverService.findLoggedDriverWithWorkingIntervals();

        checkDriverCurrentDrivings(driver.getEmail());

        Optional<WorkingInterval> workingInterval = this.getLastWorkingIntervalFromDriver(driver);
        workingInterval.ifPresent(interval -> interval.setEndTime(LocalDateTime.now()));
        driver.setActive(false);
        driverService.save(driver);
    }

    private void checkDriverCurrentDrivings(String email) {
        if (driverLockerService.driverIsLocked(email)) {
            throw new BadRequestException(ExceptionMessageConstants.CANT_CHANGE_DS_TO_INACTIVE_CAUSE_DRIVINGS_EXISTS);
        }

        Driver driver = driverService.findByEmailWithAllDrivings(email)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(email)));

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
        driver.getWorkingIntervals().sort(Comparator.comparing(WorkingInterval::getStartTime));

        return Optional.of(driver.getWorkingIntervals().get(driver.getWorkingIntervals().size() - 1));
    }

    @Override
    public boolean isLoggedDriverActive() {
        return driverService.getCurrentlyLoggedDriverWithCurrentDriving().isActive();
    }
}
