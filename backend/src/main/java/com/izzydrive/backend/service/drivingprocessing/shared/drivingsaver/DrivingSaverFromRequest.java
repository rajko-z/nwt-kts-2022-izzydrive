package com.izzydrive.backend.service.drivingprocessing.shared.drivingsaver;

import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.Passenger;

public interface DrivingSaverFromRequest {

    Driving makeAndSaveDrivingFromRegularRequest(DrivingRequestDTO request,
                                                 Driver driver,
                                                 Passenger passenger,
                                                 CalculatedRouteDTO fromDriverToStart);

    Driving makeAndSaveDrivingFromReservationRequest(DrivingRequestDTO request,
                                                     Driver driver,
                                                     Passenger passenger);
}
