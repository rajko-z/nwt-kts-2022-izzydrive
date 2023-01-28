package com.izzydrive.backend.service.drivingprocessing.shared.drivingsaver;

import com.izzydrive.backend.dto.driving.DrivingRequestDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.model.users.Passenger;

public interface DrivingSaverFromRequest {

    Driving makeAndSaveDrivingFromRegularRequest(DrivingRequestDTO request,
                                                 Driver driver,
                                                 Passenger passenger);

    Driving makeAndSaveDrivingFromReservationRequest(DrivingRequestDTO request,
                                                     Driver driver,
                                                     Passenger passenger);
}
