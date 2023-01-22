package com.izzydrive.backend.jobs;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.*;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.service.NotificationService;
import com.izzydrive.backend.service.ReservationNotificationService;
import com.izzydrive.backend.service.driving.DrivingService;
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
        int minutes = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), d.getReservationDate());
        if (endMinutes < minutes && minutes <= startMinutes) {
            List<User> userForNotification = new ArrayList<>(d.getAllPassengers());
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
        if (!d.getDriver().isActive()) {
            notificationService.sendNotificationForReservationDeleted(d, "The reservation was canceled because the selected driver is no longer active");
            drivingService.deleteReservation(d);
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
    }

    private void updateDriving(Driving d) {
        Driving driving = drivingService.getDrivingWithLocations(d.getId());
        driving.setDrivingState(DrivingState.PAYMENT);

        Address startLocation = driving.getRoute().getStart();
        AddressOnMapDTO address = new AddressOnMapDTO(startLocation.getLongitude(), startLocation.getLatitude(), startLocation.getName());
        CalculatedRouteDTO fromDriverToStart = driverRoutesService.getCalculatedRouteFromDriverToStart(d.getDriver().getEmail(), address);

        driving.setDistanceFromDriverToStart(fromDriverToStart.getDistance());
        driving.setDurationFromDriverToStart(fromDriverToStart.getDuration());
        List<Location> locationFromDriverToStart = getLocationsNeededForDriverToStart(fromDriverToStart);
        driving.getLocations().addAll(locationFromDriverToStart);

        drivingService.save(driving);
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
            p.setCurrentDriving(d);
            passengerService.save(p);
        }
    }

    private List<Location> getLocationsNeededForDriverToStart(CalculatedRouteDTO fromStartToEnd) {
        List<Location> locations = new ArrayList<>();
        for (LocationDTO l : fromStartToEnd.getCoordinates()) {
            locations.add(new Location(l.getLat(), l.getLon(), false));
        }
        return locations;
    }
}
