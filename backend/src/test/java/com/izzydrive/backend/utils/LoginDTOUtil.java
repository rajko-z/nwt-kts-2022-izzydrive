package com.izzydrive.backend.utils;

import com.izzydrive.backend.constants.AdminConst;
import com.izzydrive.backend.constants.DriverConst;
import com.izzydrive.backend.constants.PassengerConst;
import com.izzydrive.backend.constants.UserConst;
import com.izzydrive.backend.dto.LoginDTO;

public class LoginDTOUtil {

    public static LoginDTO getDriverMika() {
        return new LoginDTO(DriverConst.D_MIKA_EMAIL, UserConst.PASSWORD);
    }

    public static LoginDTO getDriverMilan() {
        return new LoginDTO(DriverConst.D_MILAN_EMAIL, UserConst.PASSWORD);
    }

    public static LoginDTO getDriverMarko() {
        return new LoginDTO(DriverConst.D_MARKO_EMAIL, UserConst.PASSWORD);
    }

    public static LoginDTO getDriverPetar() {
        return new LoginDTO(DriverConst.D_PETAR_EMAIL, UserConst.PASSWORD);
    }

    public static LoginDTO getDriverPredrag() {
        return new LoginDTO(DriverConst.D_PREDRAG_EMAIL, UserConst.PASSWORD);
    }

    public static LoginDTO getPassengerJohn() {
        return new LoginDTO(PassengerConst.P_JOHN_EMAIL, UserConst.PASSWORD);
    }

    public static LoginDTO getPassengerBob() {
        return new LoginDTO(PassengerConst.P_BOB_EMAIL, UserConst.PASSWORD);
    }

    public static LoginDTO getPassengerSara() {
        return new LoginDTO(PassengerConst.P_SARA_EMAIL, UserConst.PASSWORD);
    }

    public static LoginDTO getPassengerKate() {
        return new LoginDTO(PassengerConst.P_KATE_EMAIL, UserConst.PASSWORD);
    }

    public static LoginDTO getAdmin() {
        return new LoginDTO(AdminConst.ADMIN_EMAIL, UserConst.PASSWORD);
    }

    public static LoginDTO getLoggedUserByEmail(String userEmail) {return new LoginDTO(userEmail, UserConst.PASSWORD);}
}
