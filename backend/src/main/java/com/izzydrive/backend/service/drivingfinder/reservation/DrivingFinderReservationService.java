package com.izzydrive.backend.service.drivingfinder.reservation;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;

import java.util.List;

public interface DrivingFinderReservationService {

    List<DrivingOptionDTO> getScheduleDrivingOptions(DrivingFinderRequestDTO request);
}
