package com.izzydrive.backend.service.users.impl;

import com.izzydrive.backend.repository.users.PassengerRepository;
import com.izzydrive.backend.service.users.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
}
