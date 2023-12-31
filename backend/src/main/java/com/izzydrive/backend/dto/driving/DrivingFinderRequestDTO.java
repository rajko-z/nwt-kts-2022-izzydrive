package com.izzydrive.backend.dto.driving;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime scheduleTime;
}
