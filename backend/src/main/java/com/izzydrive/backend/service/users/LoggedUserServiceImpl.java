package com.izzydrive.backend.service.users;

import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoggedUserServiceImpl implements LoggedUserService{

    private final PassengerService passengerService;

    private final DriverService driverService;

    public LoggedUserServiceImpl(@Lazy PassengerService passengerService, @Lazy DriverService driverService) {
        this.passengerService = passengerService;
        this.driverService = driverService;
    }

    @Override
    public Passenger getCurrentlyLoggedPassenger() {
        String passengerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return passengerService.findByEmailWithCurrentDriving(passengerEmail);
    }
}
