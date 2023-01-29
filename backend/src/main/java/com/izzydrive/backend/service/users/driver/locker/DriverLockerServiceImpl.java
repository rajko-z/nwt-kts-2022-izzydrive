package com.izzydrive.backend.service.users.driver.locker;

import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.users.driver.DriverLocker;
import com.izzydrive.backend.repository.DriverLockerRepository;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DriverLockerServiceImpl implements DriverLockerService {

    private static final Logger LOG = LoggerFactory.getLogger(DriverLockerServiceImpl.class);

    private final DriverLockerRepository driverLockerRepository;


    @Override
    public Optional<DriverLocker> findByDriverEmail(String driverEmail) {
        return this.driverLockerRepository.findByDriverEmail(driverEmail);
    }

    @Override
    public void saveAndFlush(DriverLocker driverLocker) {
        this.driverLockerRepository.saveAndFlush(driverLocker);
    }

    @Override
    @Transactional
    public void unlockDriver(String driverEmail) {
        try {
            DriverLocker driverLocker = findByDriverEmail(driverEmail)
                    .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail)));

            driverLocker.setPassengerEmail(null);
        } catch (OptimisticLockingFailureException ex) {
            LOG.info(String.format("Unlocking already unlocked driver: %s", driverEmail));
        }
    }

    @Override
    @Transactional
    public void lockDriverIfPossible(String driverEmail, String passengerEmail) {
        try {
            DriverLocker driverLocker = findByDriverEmail(driverEmail)
                    .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail)));

            if (driverLocker.getPassengerEmail() != null) {
                throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
            } else {
                driverLocker.setPassengerEmail(passengerEmail);
                saveAndFlush(driverLocker);
            }
        } catch (OptimisticLockingFailureException ex) {
            throw new BadRequestException(ExceptionMessageConstants.DRIVER_NO_LONGER_AVAILABLE);
        }
    }

    @Override
    public boolean driverIsLocked(String driverEmail) {
        Optional<DriverLocker> driverLocker = findByDriverEmail(driverEmail);
        return driverLocker.isPresent() && driverLocker.get().getPassengerEmail() != null;
    }

    @Override
    public void save(DriverLocker driverLocker) {
        driverLockerRepository.save(driverLocker);
    }
}
