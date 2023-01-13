package com.izzydrive.backend.dto.osm;

import lombok.Data;

@Data
public class PlaceDTO {
    private double lat;
    private double lon;
    private String display_name;
    private String error;
}
