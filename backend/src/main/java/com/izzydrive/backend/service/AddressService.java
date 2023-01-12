package com.izzydrive.backend.service;

import com.izzydrive.backend.model.Address;

import java.util.Optional;

public interface AddressService {

    Optional<Address> getAddressByName(String name);
}
