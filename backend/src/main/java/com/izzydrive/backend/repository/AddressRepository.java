package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("select a from Address a where a.name = ?1")
    Optional<Address> getAddressByName(String name);
}
