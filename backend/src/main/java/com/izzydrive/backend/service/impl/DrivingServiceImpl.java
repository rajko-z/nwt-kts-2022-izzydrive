package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverLocker;
import com.izzydrive.backend.model.users.DriverStatus;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.repository.users.DriverRepository;
import com.izzydrive.backend.repository.users.PassengerRepository;
import com.izzydrive.backend.service.DriverLockerService;
import com.izzydrive.backend.service.DrivingService;
import com.izzydrive.backend.service.users.PassengerService;
import com.izzydrive.backend.utils.Constants;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
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
@AllArgsConstructor
public class DrivingServiceImpl implements DrivingService {

    private final DrivingRepository drivingRepository;

    private final DriverRepository driverRepository;

    private final PassengerService passengerService;

    private final DriverLockerService driverLockerService;

    private final SimpMessagingTemplate simpMessagingTemplate;

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
    public boolean passengerApprovedToPayDriving(Driving driving, String passengerEmail) {
        if (driving.getPaymentApprovalIds() == null) {
            return false;
        }
        if (!driving.getPaymentApprovalIds().contains(";")) {
            return driving.getPaymentApprovalIds().equals(passengerEmail);
        }

        String[] approvals = driving.getPaymentApprovalIds().split(";");
        for (String approval : approvals) {
            if (approval.equals(passengerEmail)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean drivingExpiredForPayment(Driving driving) {
        return ChronoUnit.MINUTES.between(driving.getCreationDate(), LocalDateTime.now())
                >= Constants.MAX_NUMBER_OF_MINUTES_TO_COMPLETE_PAYMENT;
    }

    @Transactional
    @Override
    public void rejectDrivingLinkedUser(Long drivingId) {
        Optional<Driving> driving = drivingRepository.findById(drivingId);
        if (driving.isPresent()) {
            String startLocationForNotification = driving.get().getRoute().getStart().getName();
            String endLocationForNotification = driving.get().getRoute().getEnd().getName();

            List<String> passengersToSendNotifications = deleteDrivingFromPassengers(driving);

            drivingRepository.delete(driving.get());

            unlockDriverIfPossible(driving.get().getDriver());

            sendNotificationRejectDriving(passengersToSendNotifications, startLocationForNotification, endLocationForNotification);
        }
    }

    private List<String> deleteDrivingFromPassengers(Optional<Driving> driving) {
        List<String> passengersToSendNotifications = new ArrayList<>();
        if (driving.isPresent()) {
            for (Passenger passenger : driving.get().getPassengers()) {
                passengersToSendNotifications.add(passenger.getEmail());
                passenger.setCurrentDriving(null);
                passengerService.save(passenger);
            }
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
                driverLockerService.save(driverLocker.get());
            }
        } catch (OptimisticLockException ex) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_IS_AVAILABLE); //vozac je vec oslobodjen
        }
    }
}
