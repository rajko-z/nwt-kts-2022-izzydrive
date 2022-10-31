package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.repository.AddressRepository;
import com.izzydrive.backend.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

}
