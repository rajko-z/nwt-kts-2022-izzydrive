package com.izzydrive.backend.dto.driving;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrivingRequestDTO {
    private DrivingFinderRequestDTO drivingFinderRequest;
    private DrivingOptionDTO drivingOption;
}
