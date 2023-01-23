package com.izzydrive.backend.service.users.passenger;

import com.izzydrive.backend.confirmationToken.ConfirmationTokenService;
import com.izzydrive.backend.converters.DrivingConverter;
import com.izzydrive.backend.dto.NewPassengerDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.email.EmailSender;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.Location;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.RoleRepository;
import com.izzydrive.backend.repository.users.PassengerRepository;
import com.izzydrive.backend.repository.users.UserRepository;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.driving.routes.DrivingRoutesService;
import com.izzydrive.backend.service.users.driver.car.CarService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import com.izzydrive.backend.utils.Validator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private static final Logger LOG = LoggerFactory.getLogger(PassengerServiceImpl.class);

    private final PasswordEncoder passwordEncoder;

    private final ConfirmationTokenService confirmationTokenService;

    private final RoleRepository roleRepository;

    private final EmailSender emailSender;

    private final UserRepository userRepository;

    private final PassengerRepository passengerRepository;

    private final DrivingService drivingService;

    private final CarService carService;

    private final DrivingRoutesService drivingRoutesService;

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
    public Optional<Passenger> findByEmailWithDrivings(String email) {
        return passengerRepository.findByEmailWithDrivings(email);
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
            return DrivingConverter.convertWithLocationsAndDriver(currentDriving, locations);
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
    public Passenger getCurrentlyLoggedPassengerWithDrivings() {
        String passengerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.findByEmailWithDrivings(passengerEmail)
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(passengerEmail)));
    }

    @Override
    public CalculatedRouteDTO findEstimatedTimeLeftForCurrentDrivingToStart() {
        Passenger passenger = this.getCurrentlyLoggedPassenger();
        if (passenger.getCurrentDriving() == null || !passenger.getCurrentDriving().getDrivingState().equals(DrivingState.WAITING)) {
            throw new BadRequestException(ExceptionMessageConstants.YOU_DO_NOT_HAVE_CURRENT_WAITING_DRIVING);
        }
        return drivingRoutesService.getEstimatedRouteLeftToStartOfDriving(passenger.getCurrentDriving().getId());
    }

    @Transactional
    @Override
    public void deleteDrivingFromPassengers(Collection<Passenger> passengers) {
        for (Passenger passenger : passengers) {
            passenger.setCurrentDriving(null);
        }
    }
}
