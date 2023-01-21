package com.izzydrive.backend.service.users.impl;

import com.izzydrive.backend.confirmationToken.ConfirmationTokenService;
import com.izzydrive.backend.converters.DrivingDTOConverter;
import com.izzydrive.backend.dto.NewPassengerDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.email.EmailSender;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.Location;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.RoleRepository;
import com.izzydrive.backend.repository.users.PassengerRepository;
import com.izzydrive.backend.repository.users.UserRepository;
import com.izzydrive.backend.service.CarService;
import com.izzydrive.backend.service.DrivingService;
import com.izzydrive.backend.service.impl.DrivingServiceImpl;
import com.izzydrive.backend.service.users.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import com.izzydrive.backend.utils.Validator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private static final Logger LOG = LoggerFactory.getLogger(DrivingServiceImpl.class);

    private final PasswordEncoder passwordEncoder;

    private final ConfirmationTokenService confirmationTokenService;

    private final RoleRepository roleRepository;

    private final EmailSender emailSender;

    private final UserRepository userRepository;

    private final PassengerRepository passengerRepository;

    private final DrivingService drivingService;

    private final CarService carService;

    public void registerPassenger(NewPassengerDTO newPassengerData) {
        validateNewPassengerData(newPassengerData);

        Optional<User> user = userRepository.findByEmail(newPassengerData.getEmail());
        if (user.isEmpty()) {
            Passenger passenger = new Passenger(
                    newPassengerData.getEmail(),
                    passwordEncoder.encode(newPassengerData.getPassword()),
                    newPassengerData.getFirstName(),
                    newPassengerData.getLastName(),
                    newPassengerData.getPhoneNumber()
            );
            passenger.setBlocked(false);
            passenger.setActivated(false);
            passenger.setRole(roleRepository.findByName("ROLE_PASSENGER").get(0));


            String token = UUID.randomUUID().toString();
            confirmationTokenService.createVerificationToken(passenger, token);
            emailSender.sendConfirmationAsync(passenger.getEmail(), token, passenger.getFirstName());
        } else {
            throw new BadRequestException(ExceptionMessageConstants.USER_ALREADY_EXISTS_MESSAGE);
        }
    }

    private void validateNewPassengerData(NewPassengerDTO newPassengerData) {
        Validator.validateEmail(newPassengerData.getEmail());
        Validator.validateMatchingPassword(newPassengerData.getPassword(), newPassengerData.getRepeatedPassword());
        Validator.validateFirstName(newPassengerData.getFirstName());
        Validator.validateLastName(newPassengerData.getLastName());
        Validator.validatePhoneNumber(newPassengerData.getPhoneNumber());
    }

    @Override
    public List<UserDTO> findAllPassenger() {
        return passengerRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Override
    public Passenger findByEmailWithCurrentDriving(String email) {
        return passengerRepository.findByEmailWithCurrentDriving(email)
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(email)));
    }

    @Override
    public Passenger findByEmailWithReservedDriving(String email) {
        return passengerRepository.findByEmailWithReservedDriving(email)
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(email)));
    }

    @Override
    public Passenger getCurrentlyLoggedPassenger() {
        String passengerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.findByEmailWithCurrentDriving(passengerEmail);
    }

    @Override
    public void save(Passenger passenger) {
        this.passengerRepository.save(passenger);
    }

    @Override
    public DrivingDTOWithLocations getCurrentDrivingForLoggedPassenger() {
        Passenger passenger = this.getCurrentlyLoggedPassenger();
        if (passenger.getCurrentDriving() == null) {
            return null;
        }
        try {
            Driving currentDriving = this.drivingService.getDrivingByIdWithDriverRouteAndPassengers(passenger.getCurrentDriving().getId());
            if (currentDriving.isRejected()) {
                return null;
            }
            List<Location> locations = this.drivingService.getDrivingWithLocations(passenger.getCurrentDriving().getId()).getLocations();
            return DrivingDTOConverter.convertWithLocationsAndDriver(currentDriving, locations, carService);
        } catch (OptimisticLockingFailureException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void saveAndFlush(Passenger passenger) {
        this.passengerRepository.saveAndFlush(passenger);
    }

    @Override
    public boolean passengerDoesNotHavePayingData(Passenger passenger) {
        return passenger.getEthAddress() == null || passenger.getSecretKey() == null;
    }

    @Override
    public void resetPassengersPayingInfo(Set<Passenger> passengers) {
        for (Passenger p : passengers) {
            p.setOnceTimeSecretKey(null);
            p.setOnceTimeEthAddress(null);
            p.setApprovedPaying(false);
            p.setPayingUsingExistingInfo(!passengerDoesNotHavePayingData(p));
            this.save(p);
        }
    }

    @Override
    public List<Driving> getPassengerDrivings(Long passengerId) {
        return this.passengerRepository.getPassengerDrivings(passengerId);
    }

    @Override
    public CalculatedRouteDTO getEstimatedTimeLeftForCurrentDrivingToStart() {
        // TODO::
        return null;
    }
}
