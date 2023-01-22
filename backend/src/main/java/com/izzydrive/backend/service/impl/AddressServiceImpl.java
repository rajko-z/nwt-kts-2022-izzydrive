package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.repository.AddressRepository;
import com.izzydrive.backend.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public Optional<Address> getAddressByName(String name) {
        return addressRepository.getAddressByName(name);
    }

    @Override
    public Address getAddressFromAddressOnMap(AddressOnMapDTO address) {
        Optional<Address> retVal = getAddressByName(address.getName());
        return retVal.orElseGet(() -> new Address(address.getLongitude(), address.getLatitude(), address.getName()));
    }
}
