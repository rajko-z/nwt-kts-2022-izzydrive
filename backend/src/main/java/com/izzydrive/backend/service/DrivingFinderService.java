package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;

import java.text.ParseException;
import java.util.List;

public interface DrivingFinderService {

    List<DrivingOptionDTO> getSimpleDrivingOptions(List<AddressOnMapDTO> addresses);

    List<DrivingOptionDTO> getAdvancedDrivingOptions(DrivingFinderRequestDTO request);

    List<DrivingOptionDTO> getScheduleDrivingOptions(DrivingFinderRequestDTO request);

    void validateDrivingFinderRequest(DrivingFinderRequestDTO request);
}
