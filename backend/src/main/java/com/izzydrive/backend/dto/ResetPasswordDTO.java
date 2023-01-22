package com.izzydrive.backend.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResetPasswordDTO {

    @NotBlank
    private String token;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatedPassword;
}
