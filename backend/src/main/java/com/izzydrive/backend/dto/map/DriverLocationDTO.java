package com.izzydrive.backend.dto.map;

import com.izzydrive.backend.model.users.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverLocationDTO {
    private String driverEmail;
    private DriverStatus driverStatus;
    private LocationDTO location;
}
