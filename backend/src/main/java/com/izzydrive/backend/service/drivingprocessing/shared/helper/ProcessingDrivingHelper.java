package com.izzydrive.backend.service.drivingprocessing.shared.helper;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.model.Route;
import com.izzydrive.backend.model.users.Passenger;

import java.util.List;
import java.util.Set;

public interface ProcessingDrivingHelper {
    Route getRouteFromRequest(DrivingFinderRequestDTO request);

    List<Passenger> getPassengersWithDrivingsFromEmails(Set<String> passengerEmails);

    List<Passenger> getPassengersWithCurrentDrivingFromEmails(Set<String> passengerEmails);
}
