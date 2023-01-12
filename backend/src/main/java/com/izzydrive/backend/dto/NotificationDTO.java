package com.izzydrive.backend.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDTO {
    @NotBlank
    private String userEmail;
    @NotBlank
    private String message;
}
