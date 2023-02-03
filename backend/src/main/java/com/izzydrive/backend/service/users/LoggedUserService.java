package com.izzydrive.backend.service.users;

import com.izzydrive.backend.model.users.Passenger;

public interface LoggedUserService {
    Passenger getCurrentlyLoggedPassenger();
}
