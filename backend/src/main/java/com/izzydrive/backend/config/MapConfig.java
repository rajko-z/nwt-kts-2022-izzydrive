package com.izzydrive.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MapConfig {
    @Value("${bounding-box.bottom-left-lon}")
    private double bottomLeftLon;

    @Value("${bounding-box.bottom-left-lat}")
    private double bottomLeftLat;

    @Value("${bounding-box.upper-right-lon}")
    private double upperRightLon;

    @Value("${bounding-box.upper-right-lat}")
    private double upperRightLat;

}
