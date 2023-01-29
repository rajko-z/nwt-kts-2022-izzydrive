package com.izzydrive.backend.jobs;

import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.ReservationNotification;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.service.ReservationNotificationService;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.driver.routes.DriverRoutesServiceImpl;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.izzydrive.backend.utils.Constants.*;

@Component
@AllArgsConstructor
public class ReservationNotificationTask {
    private static final Logger LOG = LoggerFactory.getLogger(ReservationNotificationTask.class);

    private final DrivingService drivingService;

    private final PassengerService passengerService;

    private final DriverService driverService;

    private final DriverRoutesServiceImpl driverRoutesService;

    private final NotificationService notificationService;

    private final ReservationNotificationService reservationNotificationService;


    @Scheduled(cron = "${payment-session-expired-job.cron}")
    public void notifyUserAboutReservation() {
        LOG.info("> job started - notify");

        List<Driving> drivings = drivingService.getAllReservationWithDriverAndPassengers();

        for (Driving d : drivings) {
            if (d.getReservationDate() != null) {
                Optional<ReservationNotification> reservationNotification = reservationNotificationService.findByDrivingId(d.getId());
                if (reservationNotification.isPresent()) {
                    checkNotificationAndSend(d, reservationNotification.get());
                } else {
                    ReservationNotification reservationNotificationNew = new ReservationNotification(d.getId(), false, false, false);
                    reservationNotificationService.save(reservationNotificationNew);
                    checkNotificationAndSend(d, reservationNotificationNew);
                }
            }
        }
    }

    private void checkNotificationAndSend(Driving d, ReservationNotification reservationNotification) {
        if (!reservationNotification.isFirstNotification() && checkPaymentInterval(d, FIRST_INTERVAL_BEFORE_RESERVATION, SECOND_INTERVAL_BEFORE_RESERVATION)) {
            reservationNotification.setFirstNotification(true);
            reservationNotificationService.save(reservationNotification);
        }
        if (!reservationNotification.isSecondNotification() && checkNotifyInterval(d, SECOND_INTERVAL_BEFORE_RESERVATION, THIRD_INTERVAL_BEFORE_RESERVATION)) {
            reservationNotification.setSecondNotification(true);
            reservationNotificationService.save(reservationNotification);
        }
        if (!reservationNotification.isThirdNotification() && checkNotifyInterval(d, THIRD_INTERVAL_BEFORE_RESERVATION, FOURTH_INTERVAL_BEFORE_RESERVATION)) {
            reservationNotification.setThirdNotification(true);
            reservationNotificationService.save(reservationNotification);
        }
    }

    private boolean checkNotifyInterval(Driving d, Integer startMinutes, Integer endMinutes) {
        Driving driving = drivingService.getDrivingByIdWithDriverRouteAndPassengers(d.getId());
        if (driving.getDrivingState() == DrivingState.ACTIVE) {
            return true;
        }
        int minutes = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), driving.getReservationDate());
        if (endMinutes < minutes && minutes <= startMinutes) {
            List<User> userForNotification = new ArrayList<>(driving.getPassengers());
            userForNotification.add(d.getDriver());
            this.notificationService.sendNotificationReservationReminder(startMinutes, userForNotification);
            return true;
        }
        return false;
    }

    private boolean checkPaymentInterval(Driving d, Integer startMinutes, Integer endMinutes) {
        int minutes = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), d.getReservationDate());
        if (endMinutes < minutes && minutes <= startMinutes) {
            checkDriverIsActive(d);

            setDrivingToPassenger(d);

            deleteReservationFromDriver(d);

            updateDriving(d);

            notificationService.sendNotificationForPaymentReservation(d);
            notificationService.sendNotificationReservationReminder(FIRST_INTERVAL_BEFORE_RESERVATION, List.of(d.getDriver()));
            return true;
        }
        return false;
    }

    private void checkDriverIsActive(Driving d) {
        Driver driver = driverService.findByEmailWithAllDrivings(d.getDriver().getEmail())
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(d.getDriver().getEmail())));
        if (!driver.isActive() || driver.getNextDriving() != null) {
            notificationService.sendNotificationForReservationDeleted(d, "The reservation was canceled because the selected driver is no longer active");
            drivingService.deleteReservation(d);
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
    }

    private void updateDriving(Driving d) {
        d.setDrivingState(DrivingState.PAYMENT);
        d.setCreationDate(LocalDateTime.now());
        drivingService.save(d);
    }

    private void deleteReservationFromDriver(Driving d) {
        d.getDriver().setReservedFromClientDriving(null);
        driverService.save(d.getDriver());
    }

    private void setDrivingToPassenger(Driving d) {
        for (Passenger p : d.getAllPassengers()) {
            if (p.getCurrentDriving() != null) {
                notificationService.sendNotificationForReservationDeleted(d, "The reservation has been deleted because the passenger currently has a ride");
                drivingService.deleteReservation(d);
                throw new BadRequestException(ExceptionMessageConstants.PASSENGER_NO_LONGER_AVAILABLE);
            }
            Passenger passenger = passengerService.findByEmailWithDrivings(p.getEmail())
                    .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(p.getEmail())));
            passenger.setCurrentDriving(d);
            passenger.getDrivings().removeIf(driving -> Objects.equals(d.getId(), driving.getId()));
            passengerService.save(passenger);
        }
    }
}
