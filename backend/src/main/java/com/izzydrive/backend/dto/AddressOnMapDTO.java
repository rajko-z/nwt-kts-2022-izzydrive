package com.izzydrive.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class AddressOnMapDTO {
    private double longitude;
    private double latitude;
    private String name;
}
