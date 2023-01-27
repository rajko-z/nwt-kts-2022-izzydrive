package com.izzydrive.backend.service.driving.searchcurrent;

import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;

import java.util.List;

public interface CurrentDrivingsSearch {

    List<DrivingDTOWithLocations> getAllCurrentDrivings();

    List<DrivingDTOWithLocations> getAllCurrentDrivingsBySearchTerm(String text);
}
