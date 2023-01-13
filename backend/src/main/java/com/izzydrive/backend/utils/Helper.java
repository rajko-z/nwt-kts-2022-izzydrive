package com.izzydrive.backend.utils;

public class Helper {

    private Helper() {}

    public static int getDurationInMinutesFromSeconds(double duration) {
        return (int)Math.ceil(duration / 60);
    }


}
