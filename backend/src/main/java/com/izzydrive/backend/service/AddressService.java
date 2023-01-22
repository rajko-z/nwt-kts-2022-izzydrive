package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.model.Address;

import java.util.Optional;

public interface AddressService {

    Optional<Address> getAddressByName(String name);

    Address getAddressFromAddressOnMap(AddressOnMapDTO address);
}
