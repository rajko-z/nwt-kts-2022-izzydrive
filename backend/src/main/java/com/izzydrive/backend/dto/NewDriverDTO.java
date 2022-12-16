package com.izzydrive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewDriverDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private CarDTO carData;
}
