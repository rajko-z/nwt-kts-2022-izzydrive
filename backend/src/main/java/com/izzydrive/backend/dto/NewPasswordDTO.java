package com.izzydrive.backend.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewPasswordDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String currentPassword;
    @NotBlank
    private String newPassword;
}
