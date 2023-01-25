package com.izzydrive.backend.repository.users.driver;


import com.izzydrive.backend.model.users.Driver;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DriverRepositoryCustomImpl implements DriverRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void refresh(Driver driver) {
        entityManager.refresh(driver);
    }
}
