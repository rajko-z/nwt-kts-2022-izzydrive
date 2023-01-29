package com.izzydrive.backend.service.driving.cancelation.reservation;

import com.izzydrive.backend.dto.CancellationReasonDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.notification.driver.DriverNotificationService;
import com.izzydrive.backend.service.users.admin.AdminService;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationCancellationServiceImpl implements ReservationCancellationService{

    private final DrivingRepository drivingRepository;

    private final PassengerService passengerService;

    private final NotificationService notificationService;

    private final DriverNotificationService driverNotificationService;

    private final DriverService driverService;

    private final AdminService adminService;

    @Override
    @Transactional
    public void passengerCancelReservation(Long drivingId) {
        if (!currentLoggedPassengerHasReservationWithId(drivingId)) {
            throw new BadRequestException(ExceptionMessageConstants.CANT_FIND_RESERVATION_TO_CANCEL);
        }

        Driving driving = drivingRepository.getReservationDrivingByIdWithDriverRouteAndPassengers(drivingId);
        if (driving == null) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST);
        }
        List<String> passengerEmails =
                driving.getAllPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList());

        removeDrivingFromPassengers(driving, passengerEmails);
        removeReservationFromDriverAndNotify(driving);

        passengerEmails.forEach(p -> notificationService.sendNotificationForCanceledReservation(p, driving));
        notificationService.sendNotificationForCanceledReservation(driving.getDriver().getEmail(), driving);

        drivingRepository.delete(driving);
    }

    @Override
    @Transactional
    public void driverCancelReservation(CancellationReasonDTO cancellationReasonDTO) {
        Driver driver = driverService.getCurrentlyLoggedDriverWithReservation();
        Driving driving = drivingRepository.getReservationDrivingByIdWithDriverRouteAndPassengers(cancellationReasonDTO.getDrivingId());
        if (driving == null) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST);
        }

        if (driver.getReservedFromClientDriving() == null ||
            !driver.getReservedFromClientDriving().getId().equals(cancellationReasonDTO.getDrivingId()))
        {
            throw new BadRequestException(ExceptionMessageConstants.CANT_FIND_RESERVATION_TO_CANCEL);
        }

        List<String> passengerEmails =
                driving.getAllPassengers().stream().map(Passenger::getEmail).collect(Collectors.toList());

        removeDrivingFromPassengers(driving, passengerEmails);
        removeReservationFromDriverAndNotify(driving);

        passengerEmails.forEach(p -> notificationService.sendNotificationForCanceledReservation(p, driving));
        notificationService.sendNotificationCancelDrivingFromDriverToAdmin(adminService.findAdmin().getEmail(), driving, driving.getDriver().getEmail(), cancellationReasonDTO.getText());

        drivingRepository.delete(driving);
    }

    private void removeReservationFromDriverAndNotify(Driving driving) {
        driving.getDriver().setReservedFromClientDriving(null);
        driverNotificationService.deleteReservationSignal(driving.getDriver().getEmail());
    }

    private boolean currentLoggedPassengerHasReservationWithId(Long drivingId) {
        return passengerService.getCurrentlyLoggedPassengerWithDrivings().getDrivings()
                .stream().anyMatch(d -> d.getId().equals(drivingId));
    }

    private void removeDrivingFromPassengers(Driving driving, List<String> passengerEmails) {
        for (String email : passengerEmails) {
            Passenger passenger = passengerService.findByEmailWithDrivings(email)
                    .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(email)));

            passenger.getDrivings().removeIf(d -> Objects.equals(d.getId(), driving.getId()));
            driving.getAllPassengers().removeIf(p -> Objects.equals(p.getId(), passenger.getId()));
        }
    }
}
