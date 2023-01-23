package com.izzydrive.backend.service.drivingprocessing.reservation;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.driving.validation.DrivingValidationService;
import com.izzydrive.backend.service.NotificationService;
import com.izzydrive.backend.service.drivingprocessing.reservation.validation.DriverAvailabilityReservationValidator;
import com.izzydrive.backend.service.drivingprocessing.shared.drivingsaver.DrivingSaverFromRequest;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProcessDrivingReservationServiceImpl implements ProcessDrivingReservationService {

    private final DrivingValidationService drivingValidationService;

    private final DriverService driverService;

    private final PassengerService passengerService;

    private final NotificationService notificationService;

    private final DriverAvailabilityReservationValidator driverAvailabilityReservationValidator;

    private final DrivingSaverFromRequest drivingSaverFromRequest;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    @Override
    public void processReservation(DrivingRequestDTO request) {
        drivingValidationService.validateDrivingFinderRequest(request.getDrivingFinderRequest());
        Driver driver = getDriverFromRequest(request);

        Passenger passenger = passengerService.getCurrentlyLoggedPassengerWithDrivings();

        driverAvailabilityReservationValidator.checkIfDriverIsStillAvailable(request, driver);

        Driving driving = drivingSaverFromRequest
                .makeAndSaveDrivingFromReservationRequest(request, driver, passenger);

        notificationService.sendNotificationNewReservationDriving(driver.getEmail(), driving);
        notificationService.sendNotificationNewReservationDriving(passenger.getEmail(), driving);
        sendNotificationLinkedPassengers(request, driving);
        this.simpMessagingTemplate.convertAndSend("/driving/loadReservation", new DrivingDTO(driving));
    }

    private void sendNotificationLinkedPassengers(DrivingRequestDTO request, Driving driving) {
        for (String linkedPassenger : request.getDrivingFinderRequest().getLinkedPassengersEmails()) {
            notificationService.sendNotificationNewReservationDriving(linkedPassenger, driving);
        }
    }

    private Driver getDriverFromRequest(DrivingRequestDTO request) {
        DriverDTO driverDTO = request.getDrivingOption().getDriver();
        return driverService.findByEmail(driverDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverDTO.getEmail())));
    }
}
