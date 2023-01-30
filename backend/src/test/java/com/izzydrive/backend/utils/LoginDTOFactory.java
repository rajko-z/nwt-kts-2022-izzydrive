package com.izzydrive.backend.utils;

import com.izzydrive.backend.constants.AdminConst;
import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.constants.UserConst;
import com.izzydrive.backend.dto.LoginDTO;

public class LoginDTOFactory {

    public static LoginDTO getDriverMika() {
        return new LoginDTO(DriverConst.D_MIKA_EMAIL, UserConst.PASSWORD);
    }

    public static LoginDTO getPassengerJohn() {
        return new LoginDTO(PassengerConst.P_JOHN_EMAIL, UserConst.PASSWORD);
    }

    public static LoginDTO getAdmin() {
        return new LoginDTO(AdminConst.ADMIN_EMAIL, UserConst.PASSWORD);
    }
}
