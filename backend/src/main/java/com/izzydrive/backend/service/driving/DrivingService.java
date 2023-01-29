package com.izzydrive.backend.service.driving;

import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.driving.DrivingDetailsDTO;
import com.izzydrive.backend.model.Driving;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DrivingService {
    List<DrivingDTO> findAllByDriverId(Long driverId);

    void save(Driving driving);

    void saveAndFlush(Driving driving);

    Driving getDrivingByIdWithDriverRouteAndPassengers(Long id);

    Driving getDrivingWithLocations(Long id);

    List<DrivingDTO> getPassengerDrivingHistory(Long passengerId);

    List<Driving> getAllDrivingsInStatusPayment();

    boolean allPassengersApprovedDriving(Long drivingId);

    List<DrivingDTO> getPassengerFutureReservations(Long passengerId);

    Driving findByIdWithLocationsAndDriver(Long id);

    List<Driving> getAllReservationWithDriverAndPassengers();

    void deleteReservation(Driving d);

    DrivingDTOWithLocations findDrivingWithLocationsDTOById(Long id);

    DrivingDTOWithLocations findReservationDrivingWithLocationsDTOById(Long id);

    void delete(Driving driving);

    Optional<Driving> findById(Long drivingId);

    List<Driving> findAllCurrentDrivings();

    DrivingDTOWithLocations findDrivingWithLocationsById(Long drivingId);

    DrivingDetailsDTO findDrivingDetailsById(Long drivingId);

    List<Driving> getDrivingReportForPassenger(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Driving> getDrivingReportForDriver(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
