package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;

import java.util.List;

public interface DrivingFinderService {

    List<DrivingOptionDTO> getSimpleDrivingOptions(AddressOnMapDTO startLocation, AddressOnMapDTO endLocation);

    List<DrivingOptionDTO> getAdvancedDrivingOptions(DrivingFinderRequestDTO request);
}
