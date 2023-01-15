package com.izzydrive.backend.dto.driving;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.RouteDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.model.DrivingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrivingDTOWithLocations {
    private Long id;
    private double price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime creationTime;
    private RouteDTO route;
    private List<String> passengers;
    private boolean isReservation;
    private DrivingState drivingState;
    private double duration;
    private double distance;
    private DriverDTO driver;
    private List<LocationDTO> fromDriverToStart;
    private List<LocationDTO> fromStartToEnd;
}
