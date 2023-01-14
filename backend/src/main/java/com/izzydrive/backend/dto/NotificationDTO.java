package com.izzydrive.backend.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

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
    private String startLocation;
    private String endLocation;
    private List<String> intermediateLocations;
    private double duration;
    private double price;
}
