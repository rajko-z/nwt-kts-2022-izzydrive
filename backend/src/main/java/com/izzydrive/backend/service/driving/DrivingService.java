package com.izzydrive.backend.service.driving;

import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.reports.DrivingReportDTO;
import com.izzydrive.backend.model.Driving;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DrivingService {
    List<DrivingDTO> findAllByDriverId(Long driverId);

    List<DrivingDTO> findAllByPassengerId(Long passengerId);

    void save(Driving driving);

    void saveAndFlush(Driving driving);

    DrivingDTO findDrivingDTOById(Long id);

    Driving getDrivingByIdWithDriverRouteAndPassengers(Long id);

    Driving getDrivingWithLocations(Long id);

    List<DrivingDTO> getPassengerDrivingHistory(Long passengerId);

    List<Driving> getAllDrivingsInStatusPayment();

    boolean allPassengersApprovedDriving(Long drivingId);

    List<DrivingDTO> getPassengerFutureReservations(Long passengerId);

    void cancelReservation(Long drivingId);

    DrivingDTO getReservation();

    void deleteDriving(Long id);

    Driving findByIdWithLocationsAndDriver(Long id);

    List<Driving> getAllReservationWithDriverAndPassengers();

    void deleteReservation(Driving d);

    DrivingReportDTO getDrivingReportForPassenger(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    DrivingReportDTO getDrivingReportForDriver(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    DrivingDTOWithLocations findDrivingWithLocationsDTOById(Long id);

    void delete(Driving driving);

    Optional<Driving> findById(Long drivingId);

    List<Driving> findAllCurrentDrivings();

    DrivingDTOWithLocations findDrivingWithLocationsById(Long drivingId);
}
