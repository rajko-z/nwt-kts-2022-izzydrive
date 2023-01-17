package com.izzydrive.backend.dto.driving;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.enumerations.IntermediateStationsOrderType;
import com.izzydrive.backend.enumerations.OptimalDrivingType;
import com.izzydrive.backend.model.car.CarAccommodation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrivingFinderRequestDTO {
    @NotNull
    private AddressOnMapDTO startLocation;

    @NotNull
    private AddressOnMapDTO endLocation;
    private List<AddressOnMapDTO> intermediateLocations;
    private CarAccommodation carAccommodation;
    private IntermediateStationsOrderType intermediateStationsOrderType;
    private OptimalDrivingType optimalDrivingType;
    private Set<String> linkedPassengersEmails;

    @NotNull
    private Boolean reservation;
    private LocalDateTime scheduleTime;
}
