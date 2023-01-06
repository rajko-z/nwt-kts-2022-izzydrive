package com.izzydrive.backend.dto;

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
    private CarDTO carData;
}
