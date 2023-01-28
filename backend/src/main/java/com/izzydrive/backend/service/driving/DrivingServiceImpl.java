package com.izzydrive.backend.service.driving;

import com.izzydrive.backend.converters.DrivingConverter;
import com.izzydrive.backend.dto.EvaluationDTO;
import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.driving.FinishedDrivingDetailsDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.*;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.repository.users.PassengerRepository;
import com.izzydrive.backend.service.EvaluationService;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DrivingServiceImpl implements DrivingService {

    private final DrivingRepository drivingRepository;

    private final PassengerService passengerService;

    private final NotificationService notificationService;

    private final EvaluationService evaluationService;

    private final PassengerRepository passengerRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final DriverService driverService;

    public DrivingServiceImpl(DrivingRepository drivingRepository, @Lazy PassengerService passengerService, NotificationService notificationService, EvaluationService evaluationService, PassengerRepository passengerRepository, SimpMessagingTemplate simpMessagingTemplate, @Lazy DriverService driverService) {
        this.drivingRepository = drivingRepository;
        this.passengerService = passengerService;
        this.notificationService = notificationService;
        this.evaluationService = evaluationService;
        this.passengerRepository = passengerRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.driverService = driverService;
    }

    @Transactional
    @Override
    public List<DrivingDTO> findAllByDriverId(Long driverId) {
        return drivingRepository.findAllByDriverId(driverId)
                .stream().map(DrivingDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<DrivingDTO> findAllByPassengerId(Long passengerId) {
        List<Driving> drivingDTOS = drivingRepository.findAllByPassengerId(passengerId);
        return drivingDTOS.stream().map(DrivingDTO::new).collect(Collectors.toList());
    }

    @Override
    public void save(Driving driving) {
        this.drivingRepository.save(driving);
    }

    private List<String> deleteDrivingFromPassengers2(Optional<Driving> driving) {
        List<String> passengersToSendNotifications = new ArrayList<>();
        if (driving.isPresent()) {
            for (Passenger passenger : driving.get().getAllPassengers()) {
                passengersToSendNotifications.add(passenger.getEmail());
                passenger.setCurrentDriving(null);
                passengerService.save(passenger);
            }
        }
        return passengersToSendNotifications;
    }

    @Override
    public DrivingDTO findDrivingDTOById(Long id) {
        Optional<Driving> driving = drivingRepository.findById(id);
        if (driving.isEmpty()) {
            throw new NotFoundException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST);
        }
        return new DrivingDTO(driving.get());
    }

    @Transactional
    @Override
    public List<DrivingDTO> getPassengerDrivingHistory(Long passengerId) {
        List<Driving> passengerDrivings = this.passengerService.getPassengerDrivings(passengerId);
        List<DrivingDTO> convertedDriving = new ArrayList<DrivingDTO>();
        for (Driving driving : passengerDrivings) {
            DrivingDTO dto = new DrivingDTO((driving));
            dto.setFavoriteRoute(this.isFavouriteRoute(driving, passengerId));
            if (!driving.isReservation()) {
                if (driving.getEndDate().isAfter(LocalDateTime.now().minusHours(72)) &&
                        driving.getDrivingState() == DrivingState.FINISHED &&
                        !isAlreadyEvaluatedDriving(driving.getId())) {
                    dto.setEvaluationAvailable(true);
                } else {
                    dto.setEvaluationAvailable(false);
                }
                convertedDriving.add(dto);
            }

        }
        return convertedDriving;
    }

    private boolean isFavouriteRoute(Driving driving, Long passengerId) {
        Optional<Passenger> passenger = passengerRepository.findByIdWithFavoriteRoutes(passengerId);
        if (passenger.isPresent()) {
            for (Route route : passenger.get().getFavouriteRoutes()) {
                if (route.getStart().getName().equals(driving.getRoute().getStart().getName()) &&
                        route.getEnd().getName().equals(driving.getRoute().getEnd().getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Transactional
    public List<DrivingDTO> getPassengerFutureReservations(Long passengerId) {
        List<Driving> drivingsReservations = this.drivingRepository.getPassengerReservations(passengerId);
        List<DrivingDTO> convertedDrivings = new ArrayList<>();
        for (Driving driving : drivingsReservations) {
            convertedDrivings.add(new DrivingDTO(driving));
        }
        return convertedDrivings;
    }

    @Override
    @Transactional
    public void cancelReservation(Long drivingId) {
        Optional<Driving> driving = drivingRepository.findById(drivingId);
        if (driving.isPresent()) {
            List<String> passengersToSendNotifications = deleteDrivingFromPassengers2(driving);
            if (driving.get().getDriver() != null) {
                releaseDriverFromReservation(driving.get());
            }

            for (String passengerEmail : passengersToSendNotifications) {
                this.deleteFromPassangerDrivingtable(passengerEmail, driving.get());
            }

            for (String passengerEmail : passengersToSendNotifications) {
                this.notificationService.sendNotificationCancelDriving(passengerEmail, driving.get());
            }
            this.notificationService.sendNotificationCancelDriving(driving.get().getDriver().getEmail(), driving.get());
            this.simpMessagingTemplate.convertAndSend("/driving/loadReservation", new DrivingDTO(driving.get().getDriver().getEmail()));

            driving.get().setDeleted(true);
            drivingRepository.save(driving.get());
        }
    }

    @Transactional
    public void deleteFromPassangerDrivingtable(String email, Driving driving) {
        Optional<Passenger> pass = passengerRepository.findByEmail(email);
        Long drivingId = driving.getId();
        if (pass.isPresent()) {
            Passenger passenger = pass.get();
            passenger.getDrivings().removeIf(d -> Objects.equals(d.getId(), drivingId));
            driving.getAllPassengers().removeIf(p -> Objects.equals(p.getId(), passenger.getId()));
            passengerRepository.save(passenger);
            drivingRepository.save(driving);
        }
    }

    private void releaseDriverFromReservation(Driving reservation) {
        Driver d = reservation.getDriver();
        d.setReservedFromClientDriving(null);
    }

    @Override
    public Driving getDrivingByIdWithDriverRouteAndPassengers(Long id) {
        return this.drivingRepository.getDrivingByIdWithDriverRouteAndPassengers(id);
    }

    @Override
    public Driving getDrivingWithLocations(Long id) {
        return this.drivingRepository.getDrivingWithLocations(id);
    }

    private boolean isAlreadyEvaluatedDriving(Long drivingId) {
        List<Evaluation> existingEvaluations = this.evaluationService.findAll();
        for (Evaluation evaluation : existingEvaluations) {
            if (Objects.equals(evaluation.getDriving().getId(), drivingId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Driving> getAllDrivingsInStatusPayment() {
        return this.drivingRepository.getAllDrivingsInStatusPayment();
    }

    @Override
    public boolean allPassengersApprovedDriving(Long drivingId) {
        Optional<Driving> driving = this.drivingRepository.findById(drivingId);
        return driving.filter(value -> value.getPassengers().stream()
                .filter(Passenger::isApprovedPaying)
                .count() == value.getPassengers().size()).isPresent();
    }

    @Override
    public DrivingDTO getReservation() {
        Driver driver = driverService.getCurrentlyLoggedDriverWithReservation();
        if (driver.getReservedFromClientDriving() != null) {
            return findDriving(driver.getReservedFromClientDriving().getId());
        }
        return null;
    }

    private DrivingDTO findDriving(Long drivingId) {
        try {
            Driving driving = drivingRepository.findById(drivingId)
                    .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST));
            if (!driving.isRejected()) {
                return new DrivingDTO(driving);
            }
        } catch (OptimisticLockingFailureException e) {
        }
        return null;
    }

    @Override
    public void deleteDriving(Long id) {
        Driving driving = drivingRepository.findByIdWithPassengers(id)
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST));
        for (Passenger p : driving.getPassengers()) {
            Passenger passenger = this.passengerService.findByEmailWithCurrentDriving(p.getEmail());
            passenger.setCurrentDriving(null);
            if (driving.isReservation()) {
                passenger.getDrivings().removeIf(d -> !Objects.equals(d.getId(), driving.getId()));
            }
            passengerService.save(passenger);
            this.simpMessagingTemplate.convertAndSend("/driving/deleteDriving", passenger.getEmail());
        }
        drivingRepository.delete(driving);
    }

    @Override
    public void saveAndFlush(Driving driving) {
        this.drivingRepository.saveAndFlush(driving);
    }

    @Override
    public Driving findByIdWithLocationsAndDriver(Long id) {
        return this.drivingRepository.findByIdWithLocationsAndDriver(id);
    }

    @Override
    @Transactional
    public DrivingDTOWithLocations findDrivingWithLocationsDTOById(Long id) {
        Driving driving = getDrivingByIdWithDriverRouteAndPassengers(id);
        List<Location> locations = getDrivingWithLocations(id).getLocations();
        return DrivingConverter.convertWithLocationsAndDriver(driving, locations);
    }

    @Override
    public void delete(Driving driving) {
        drivingRepository.delete(driving);
    }

    @Override
    public List<Driving> getAllReservationWithDriverAndPassengers() {
        return this.drivingRepository.getAllReservationWithDriverAndPassengers();
    }

    @Override
    public void deleteReservation(Driving d) {
        d.getDriver().setReservedFromClientDriving(null);
        driverService.save(d.getDriver());
        for (Passenger p : d.getAllPassengers()) {
            if (Objects.equals(p.getCurrentDriving().getId(), d.getId())) {
                p.setCurrentDriving(null);
            }
            p.getDrivings().removeIf(driving -> !Objects.equals(d.getId(), driving.getId()));
            passengerService.save(p);
        }
        drivingRepository.delete(d);
    }

    public Optional<Driving> findById(Long drivingId) {
        return drivingRepository.findById(drivingId);
    }

    @Override
    public List<Driving> findAllCurrentDrivings() {
        return drivingRepository.findAllCurrentDrivings();
    }

    @Override
    @Transactional
    public DrivingDTOWithLocations findDrivingWithLocationsById(Long drivingId) {
        Driving currentDriving = getDrivingByIdWithDriverRouteAndPassengers(drivingId);
        if (currentDriving != null) {
            List<Location> locations = getDrivingWithLocations(drivingId).getLocations();
            return DrivingConverter.convertWithLocationsAndDriver(currentDriving, locations);
        }
        throw new NotFoundException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST);
    }

    @Override
    @Transactional
    public FinishedDrivingDetailsDTO findFinishedDrivingDetailsById(Long drivingId) {
        Driving driving = drivingRepository.findFinishedDrivingWithPassengersRouteAndDriver(drivingId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST));

        List<EvaluationDTO> evaluations = evaluationService.findAllByDrivingId(drivingId);
        List<Location> fromStartToEndLocations = getDrivingWithLocations(drivingId).getLocationsFromStartToEnd();
        return DrivingConverter.convertToFinishedDrivingDetailsDTO(driving, fromStartToEndLocations, evaluations);
    }

    public List<Driving> getDrivingReportForPassenger(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return this.drivingRepository.getDrivingReportForPassenger(userId, startDate, endDate);
    }

    @Override
    public List<Driving> getDrivingReportForDriver(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return this.drivingRepository.getDrivingReportForDriver(userId, startDate, endDate);
    }
}
