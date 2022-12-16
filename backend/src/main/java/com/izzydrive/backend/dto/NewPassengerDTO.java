package com.izzydrive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewPassengerDTO {
    private String email;
    private String password;
    private String repeatedPassword;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
