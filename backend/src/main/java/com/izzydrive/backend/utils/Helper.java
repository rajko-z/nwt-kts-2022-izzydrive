package com.izzydrive.backend.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Helper {

    private Helper() {}

    public static int getDurationInMinutesFromSeconds(double duration) {
        return (int)Math.ceil(duration / 60);
    }

    public static  String convertDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }

    public static Long getDistanceBetweenDays(LocalDateTime start, LocalDateTime end){
        return  start.until(end, ChronoUnit.DAYS) + 1;
    }
}
