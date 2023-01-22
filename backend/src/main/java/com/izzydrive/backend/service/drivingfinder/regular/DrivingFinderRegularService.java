package com.izzydrive.backend.service.drivingfinder.regular;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;

import java.util.List;

public interface DrivingFinderRegularService {

    List<DrivingOptionDTO> getSimpleDrivingOptions(List<AddressOnMapDTO> addresses);

    List<DrivingOptionDTO> getAdvancedDrivingOptions(DrivingFinderRequestDTO request);

}
