package com.izzydrive.backend.utils;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.model.users.driver.DriverStatus;

public class DriverUtil {

    public static DriverDTO getSimpleDriverDTO(String email, DriverStatus driverStatus) {
        DriverDTO d = new DriverDTO();
        d.setEmail(email);
        d.setDriverStatus(driverStatus);
        d.setLocation(AddressesUtil.getBanijskaLocation());
        return d;
    }
}
