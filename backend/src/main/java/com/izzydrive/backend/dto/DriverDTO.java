package com.izzydrive.backend.dto;

import com.izzydrive.backend.model.users.DriverStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DriverDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private DriverStatus driverStatus;
    private CarDTO carData;
}
