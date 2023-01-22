package com.izzydrive.backend.service.drivingfinder.helper;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.driving.DrivingOptionDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.enumerations.IntermediateStationsOrderType;
import com.izzydrive.backend.enumerations.OptimalDrivingType;
import com.izzydrive.backend.model.car.CarAccommodation;

import java.util.List;

public interface DrivingFinderHelper {
    List<AddressOnMapDTO> getAllPointsFromDrivingFinderRequest(DrivingFinderRequestDTO request);

    List<CalculatedRouteDTO> getCalculatedRoutesFromStartToEnd(List<AddressOnMapDTO> points,
                                                               OptimalDrivingType optimalType,
                                                               IntermediateStationsOrderType interOrderType);

    void sortOptionsByCriteria(List<DrivingOptionDTO> options,
                               OptimalDrivingType optimalType,
                               CarAccommodation carAccommodation);
}
