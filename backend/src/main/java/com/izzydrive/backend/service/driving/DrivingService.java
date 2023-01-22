package com.izzydrive.backend.service.driving;

import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.model.Driving;

import java.util.List;

public interface DrivingService {
    List<DrivingDTO> findAllByDriverId(Long driverId);

    List<DrivingDTO> findAllByPassengerId(Long passengerId);

    void save(Driving driving);

    void saveAndFlush(Driving driving);

    void rejectDrivingLinkedUser();

    void removeDrivingPaymentSessionExpired(Long drivingId);

    DrivingDTO findById(Long id);

    Driving getDrivingByIdWithDriverRouteAndPassengers(Long id);

    Driving getDrivingWithLocations(Long id);

    List<DrivingDTO> getPassengerDrivingHistory(Long passengerId);

    List<Driving> getAllDrivingsInStatusPayment();

    boolean allPassengersApprovedDriving(Long drivingId);

    void cleanUpDrivingAfterFailurePaymentAndSendNotification(Driving driving);

    void setUpDrivingAfterSuccessPaymentAndSendNotification(Driving driving);

    List<DrivingDTO> getPassengerFutureReservations(Long passengerId);

    void cancelReservation(Long drivingId);

    DrivingDTO getCurrentDriving();

    DrivingDTO getNextDriving();

    void deleteDriving(Long id);

    Driving findByIdWithLocationsAndDriver(Long id);
}