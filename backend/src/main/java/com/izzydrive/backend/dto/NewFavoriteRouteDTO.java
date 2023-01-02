package com.izzydrive.backend.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewFavoriteRouteDTO {

    @NotNull
    private Long passengerId;

    @NotBlank
    private String startLocation;

    @NotBlank
    private String endLocation;
}
