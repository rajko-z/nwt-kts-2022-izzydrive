package com.izzydrive.backend.service.drivingprocessing.regular;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.driving.validation.DrivingValidationServiceImpl;
import com.izzydrive.backend.service.drivingprocessing.regular.validation.DriverAvailabilityRegularValidator;
import com.izzydrive.backend.service.drivingprocessing.shared.drivingsaver.DrivingSaverFromRequest;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.driver.locker.DriverLockerService;
import com.izzydrive.backend.service.users.driver.routes.DriverRoutesService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProcessDrivingRegularServiceImpl implements ProcessDrivingRegularService {

    private final DriverService driverService;

    private final DriverLockerService driverLockerService;

    private final PassengerService passengerService;

    private final NotificationService notificationService;

    private final DrivingValidationServiceImpl drivingValidationService;

    private final DriverRoutesService driverRoutesService;

    private final DriverAvailabilityRegularValidator driverAvailabilityRegularValidator;

    private final DrivingSaverFromRequest drivingSaverFromRequest;


    @Transactional
    public void process(DrivingRequestDTO request) {
        drivingValidationService.validateDrivingFinderRequest(request.getDrivingFinderRequest());

        Driver driver = getDriverFromRequest(request);
        Passenger passenger = this.passengerService.getCurrentlyLoggedPassenger();

        CalculatedRouteDTO fromDriverToStart =
                driverRoutesService.getCalculatedRouteFromDriverToStart(driver.getEmail(), request.getDrivingFinderRequest().getStartLocation());

        driverAvailabilityRegularValidator
                .checkIfDriverIsStillAvailable(request, driver, fromDriverToStart);

        driverLockerService.lockDriverIfPossible(driver.getEmail(), passenger.getEmail());

        Driving driving = drivingSaverFromRequest
                .makeAndSaveDrivingFromRegularRequest(
                        request, driver, passenger, fromDriverToStart);

        sendNotificationLinkedPassengers(request, driving);
    }

    private void sendNotificationLinkedPassengers(DrivingRequestDTO request, Driving driving) {
        for (String linkedPassenger : request.getDrivingFinderRequest().getLinkedPassengersEmails()) {
            notificationService.sendNotificationNewDriving(linkedPassenger, driving);
        }
    }

    private Driver getDriverFromRequest(DrivingRequestDTO request) {
        DriverDTO driverDTO = request.getDrivingOption().getDriver();
        return driverService.findByEmailWithCurrentDrivingAndLocations(driverDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverDTO.getEmail())));

    }
}
