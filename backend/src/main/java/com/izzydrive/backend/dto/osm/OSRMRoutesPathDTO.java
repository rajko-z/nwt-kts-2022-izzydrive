package com.izzydrive.backend.dto.osm;

import lombok.Data;

import java.util.List;

@Data
public class OSRMRoutesPathDTO {

    @Data
    public static class RouteDTO {
        private GeometryDTO geometry;
        private Double duration;
        private Double distance;
    }

    @Data
    public static class GeometryDTO {
        private List<Double[]> coordinates;
    }

    private String code;
    private List<RouteDTO> routes;
}
