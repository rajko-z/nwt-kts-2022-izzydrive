package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.*;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.repository.users.DriverRepository;
import com.izzydrive.backend.service.DriverLockerService;
import com.izzydrive.backend.service.DrivingService;
import com.izzydrive.backend.service.users.DriverService;
import com.izzydrive.backend.service.users.PassengerService;
import com.izzydrive.backend.utils.Constants;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DrivingServiceImpl implements DrivingService {

    private static final Logger LOG = LoggerFactory.getLogger(DrivingServiceImpl.class);

    private final DrivingRepository drivingRepository;

    private final DriverRepository driverRepository;

    private final PassengerService passengerService;

    private final DriverLockerService driverLockerService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final DriverService driverService;

    public DrivingServiceImpl(DrivingRepository drivingRepository, DriverRepository driverRepository, @Lazy PassengerService passengerService, DriverLockerService driverLockerService, SimpMessagingTemplate simpMessagingTemplate, DriverService driverService) {
        this.drivingRepository = drivingRepository;
        this.driverRepository = driverRepository;
        this.passengerService = passengerService;
        this.driverLockerService = driverLockerService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.driverService = driverService;
    }

    @Override
    public List<DrivingDTO> findAllByDriverId(Long driverId) {
        return drivingRepository.findAllByDriverId(driverId)
                .stream().map(DrivingDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<DrivingDTO> findAllByPassengerId(Long passengerId) {
        List<Driving> drivingDTOS = drivingRepository.findAllByPassengerId(passengerId);
        return drivingDTOS.stream().map(DrivingDTO::new).collect(Collectors.toList());
    }

    @Override
    public void save(Driving driving) {
        this.drivingRepository.save(driving);
    }

    @Override
    public boolean drivingExpiredForPayment(Driving driving) {
        return ChronoUnit.MINUTES.between(driving.getCreationDate(), LocalDateTime.now())
                >= Constants.MAX_NUMBER_OF_MINUTES_TO_COMPLETE_PAYMENT;
    }

    @Transactional
    @Override
    public void rejectDrivingLinkedUser() {
        Passenger passenger = passengerService.getCurrentlyLoggedPassenger();
        Driving driving = passenger.getCurrentDriving();
        if (driving == null || !driving.getDrivingState().equals(DrivingState.PAYMENT)) {
            throw new BadRequestException(ExceptionMessageConstants.YOU_DON_NOT_HAVING_DRIVING_FOR_PAYMENT);
        }

        String startLocationForNotification = driving.getRoute().getStart().getName();
        String endLocationForNotification = driving.getRoute().getEnd().getName();

        List<String> passengersToSendNotifications = deleteDrivingFromPassengers(driving);

        this.passengerService.resetPassengersPayingInfo(driving.getPassengers());

        drivingRepository.delete(driving);

        unlockDriverIfPossible(driving.getDriver());

        sendNotificationRejectDriving(passengersToSendNotifications, startLocationForNotification, endLocationForNotification);
    }

    @Transactional
    @Override
    public void removeDrivingPaymentSessionExpired(Long drivingId) {
        Optional<Driving> driving = drivingRepository.findById(drivingId);
        if (driving.isPresent()) {

            List<String> passengersToSendNotifications = deleteDrivingFromPassengers(driving.get());

            this.passengerService.resetPassengersPayingInfo(driving.get().getPassengers());

            drivingRepository.delete(driving.get());

            unlockDriverIfPossible(driving.get().getDriver());

            sendNotificationForPaymentExpired(passengersToSendNotifications);
        }
    }

    private List<String> deleteDrivingFromPassengers(Driving driving) {
        List<String> passengersToSendNotifications = new ArrayList<>();
        for (Passenger passenger : driving.getPassengers()) {
            passengersToSendNotifications.add(passenger.getEmail());
            passenger.setCurrentDriving(null);
            passengerService.save(passenger);
        }
        return passengersToSendNotifications;
    }

    private void sendNotificationRejectDriving(List<String> passengersToSendNotifications, String startLocationForNotification, String endLocationForNotification) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("The ride was canceled when declined by the linked user");
            notificationDTO.setStartLocation(startLocationForNotification);
            notificationDTO.setEndLocation(endLocationForNotification);
            notificationDTO.setUserEmail(passenger);
            this.simpMessagingTemplate.convertAndSend("/notification/cancelRide", notificationDTO);
        }
    }

    private void sendNotificationForPaymentExpired(List<String> passengersToSendNotifications) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Payment session has expired. Your current driving is canceled");
            notificationDTO.setUserEmail(passenger);
            this.simpMessagingTemplate.convertAndSend("/notification/paymentSessionExpired", notificationDTO);
        }
    }

    private void sendNotificationForPaymentFailure(List<String> passengersToSendNotifications) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Payment failure, canceling current driving. Make sure every passenger input correct paying info and have enough funds.");
            notificationDTO.setUserEmail(passenger);
            this.simpMessagingTemplate.convertAndSend("/notification/paymentFailure", notificationDTO);
        }
    }

    private void sendNotificationForPaymentSuccess(List<String> passengersToSendNotifications) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Payment success.");
            notificationDTO.setUserEmail(passenger);
            this.simpMessagingTemplate.convertAndSend("/notification/paymentSuccess", notificationDTO);
        }
    }

    @Override
    public DrivingDTO findById(Long id) {
        Optional<Driving> driving = drivingRepository.findById(id);
        if (driving.isEmpty()) {
            throw new NotFoundException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST);
        }
        return new DrivingDTO(driving.get());
    }

    private void unlockDriverIfPossible(Driver driver) {
        try {
            Optional<DriverLocker> driverLocker = this.driverLockerService.findByDriverEmail(driver.getEmail());
            if (driverLocker.isEmpty()) { //ako je prazno greska da vozac nije zakljucan
                throw new BadRequestException(ExceptionMessageConstants.DRIVER_IS_AVAILABLE);
            } else if (driverLocker.get().getPassengerEmail() != null) { //ovde treba postavili passenger na null
                driverLocker.get().setPassengerEmail(null);
                driverLockerService.saveAndFlush(driverLocker.get());
            }
        } catch (OptimisticLockException ex) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_IS_AVAILABLE); //vozac je vec oslobodjen
        }
    }

    @Override
    public Driving getDrivingByIdWithDriverRouteAndPassengers(Long id) {
        return this.drivingRepository.getDrivingByIdWithDriverRouteAndPassengers(id);
    }

    @Override
    public Driving getDrivingWithLocations(Long id) {
        return this.drivingRepository.getDrivingWithLocations(id);
    }

    @Override
    public List<Driving> getAllDrivingsInStatusPayment() {
        return this.drivingRepository.getAllDrivingsInStatusPayment();
    }

    @Override
    public boolean allPassengersApproveDriving(Long drivingId) {
        Optional<Driving> driving = this.drivingRepository.findById(drivingId);
        return driving.filter(value -> value.getPassengers().stream()
                .filter(Passenger::isApprovedPaying)
                .count() == value.getPassengers().size()).isPresent();
    }

    @Override
    public void cleanUpDrivingAfterFailurePaymentAndSendNotification(Driving driving) {
        List<String> passengersToSendNotifications = deleteDrivingFromPassengers(driving);
        unlockDriverIfPossible(driving.getDriver());
        passengerService.resetPassengersPayingInfo(driving.getPassengers());
        drivingRepository.delete(driving);
        sendNotificationForPaymentFailure(passengersToSendNotifications);
    }

    @Override
    public void setUpDrivingAfterSuccessPaymentAndSendNotification(Driving driving) {
        driving.setDrivingState(DrivingState.WAITING);
        driving.setLocked(false);
        this.saveAndFlush(driving);

        changeDriverStatus(driving);
        passengerService.resetPassengersPayingInfo(driving.getPassengers());
        unlockDriverIfPossible(driving.getDriver());
        sendNotificationForPaymentSuccess(driving.getPassengers().stream().map(User::getEmail).collect(Collectors.toList()));

        // TODO start navigation job
    }

    private void changeDriverStatus(Driving driving) {
        Driver driver = driving.getDriver();
        if (driver.getDriverStatus().equals(DriverStatus.FREE)) {
            driver.setDriverStatus(DriverStatus.TAKEN);
            driver.setCurrentDriving(driving);
        } else {
            driver.setDriverStatus(DriverStatus.RESERVED);
            driver.setNextDriving(driving);
        }
        driverService.save(driver);
    }

    @Override
    public void saveAndFlush(Driving driving) {
        this.drivingRepository.saveAndFlush(driving);
    }
}
