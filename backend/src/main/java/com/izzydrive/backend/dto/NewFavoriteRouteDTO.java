package com.izzydrive.backend.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewFavoriteRouteDTO {

    @NotBlank
    private String startLocation;

    @NotBlank
    private String endLocation;

    private List<String> intermediateLocations;

    private Long routeId;
}
