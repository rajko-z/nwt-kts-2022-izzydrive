package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.model.Driving;

import java.util.List;

public interface DrivingService {
    List<DrivingDTO> findAllByDriverId(Long driverId);

    List<DrivingDTO> findAllByPassengerId(Long passengerId);

    void save(Driving driving);

    boolean passengerApprovedToPayDriving(Driving driving, String passengerEmail);

    boolean drivingExpiredForPayment(Driving driving);

    void rejectDrivingLinkedUser(Long drivingId);

    DrivingDTO findById(Long id);

    List<DrivingDTO> getPassengerDrivingHistory(Long passengerId);
}
