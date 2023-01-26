package com.izzydrive.backend.service.users.driver;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.dto.map.DriverLocationDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.email.EmailSender;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.car.Car;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.RoleRepository;
import com.izzydrive.backend.repository.users.driver.DriverRepository;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.users.UserService;
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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

    private static final Logger LOG = LoggerFactory.getLogger(DriverServiceImpl.class);

    private final DriverRepository driverRepository;

    private final CarService carService;

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailSender emailSender;

    private final DrivingService drivingService;

    @Override
    public Optional<Driver> findByEmail(String email) {
        return this.driverRepository.findByEmail(email);
    }

    @Override
    public void addNewDriver(DriverDTO driverDTO) {
        validateNewDriver(driverDTO);

        Optional<Driver> existingDriver = driverRepository.findByEmail(driverDTO.getEmail());
        if(existingDriver.isEmpty()){
            Car car = carService.createNewCar(driverDTO.getCarData());
            String password = userService.generatePassword();
            Driver newDriver = new Driver(driverDTO.getEmail(),
                    passwordEncoder.encode(password),
                    driverDTO.getFirstName(),
                    driverDTO.getLastName(),
                    driverDTO.getPhoneNumber());

            newDriver.setRole(roleRepository.findByName("ROLE_DRIVER").get(0));
            newDriver.setCar(car);
            Driver savedDriver = driverRepository.save(newDriver);
            car.setDriver(savedDriver);
            carService.saveCar(car);

            emailSender.sendDriverRegistrationMail(driverDTO.getEmail(), password);
        }
        else{
            throw new BadRequestException(ExceptionMessageConstants.USER_ALREADY_EXISTS_MESSAGE);
        }
    }

    private void validateNewDriver(DriverDTO driverDTO){
        Validator.validateFirstName(driverDTO.getFirstName());
        Validator.validateLastName(driverDTO.getLastName());
        Validator.validatePhoneNumber(driverDTO.getPhoneNumber());
        Validator.validateEmail(driverDTO.getEmail());
        Validator.validateCarRegistration(driverDTO.getCarData().getRegistration());
        Validator.validateCarType(driverDTO.getCarData().getCarType());
    }

    @Override
    public List<UserDTO> findAllDrivers(){
        return driverRepository.findAll().stream().sorted(Comparator.comparing(User::getId))
                .map(UserDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<Driver> findAllActiveDrivers() {
        return driverRepository.findAllActiveDrivers();
    }

    @Override
    public List<DriverLocationDTO> findAllActiveDriversLocation() {
        return findAllActiveDrivers()
                .stream()
                .map(d -> new DriverLocationDTO(d.getEmail(), d.getDriverStatus(), new LocationDTO(d.getLon(), d.getLat())))
                .collect(Collectors.toList());
    }

    @Override
    public Driver findLoggedDriverWithWorkingIntervals() {
        String driverEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.driverRepository.findByEmailWithWorkingIntervals(driverEmail)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail)));
    }

    @Override
    public Optional<Driver> findByEmailWithWorkingIntervals(String driverEmail) {
        return this.driverRepository.findByEmailWithWorkingIntervals(driverEmail);
    }

    @Override
    public void save(Driver driver) {
        this.driverRepository.save(driver);
    }

    @Override
    public Optional<Driver> findByEmailWithAllDrivings(String email) {
        return this.driverRepository.findByEmailWithAllDrivings(email);
    }

    @Override
    public Optional<Driver> findByEmailWithCurrentDrivingAndLocations(String email) {
        return this.driverRepository.findByEmailWithCurrentDrivingAndLocations(email);
    }

    @Override
    @Transactional
    public void updateCoordinatesForDriver(String driverEmail, double lat, double lon) {
        this.driverRepository.updateCoordinatesForDriver(driverEmail, lat, lon);
    }

    @Override
    public Optional<Driver> findByEmailWithNextDrivingAndLocations(String email) {
        return this.driverRepository.findByEmailWithNextDrivingAndLocations(email);
    }

    @Override
    public Optional<Driver> findByEmailWithCurrentNextAndReservedDriving(String email) {
        return this.driverRepository.findByEmailWithCurrentNextAndReservedDriving(email);
    }

    @Override
    public Driver getCurrentlyLoggedDriverWithCurrentDriving(){
        String driverEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.driverRepository.findByEmailWithCurrentDriving(driverEmail)
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail)));
    }

    @Override
    public Driver getCurrentlyLoggedDriverWithNextDriving(){
        String driverEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.driverRepository.findByEmailWithNextDriving(driverEmail)
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail)));
    }

    @Override
    public Driver getCurrentlyLoggedDriverWithReservation() {
        String driverEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.driverRepository.findByEmailWithReservation(driverEmail)
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail)));
    }

    @Override
    @Transactional
    public DrivingDTOWithLocations getCurrentDriving() {
        try {
            Driver driver = getCurrentlyLoggedDriverWithCurrentDriving();
            if (driver.getCurrentDriving() == null) {
                return null;
            }
            if (driver.getCurrentDriving().isRejected()) {
                return null;
            }
            return drivingService.findDrivingWithLocationsDTOById(driver.getCurrentDriving().getId());
        } catch (OptimisticLockingFailureException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    @Override
    @Transactional
    public DrivingDTOWithLocations getNextDriving() {
        try {
            Driver driver = getCurrentlyLoggedDriverWithNextDriving();
            if (driver.getNextDriving() == null) {
                return null;
            }
            if (driver.getNextDriving().isRejected()) {
                return null;
            }
            return drivingService.findDrivingWithLocationsDTOById(driver.getNextDriving().getId());
        } catch (OptimisticLockingFailureException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void refresh(Driver driver) {
        driverRepository.refresh(driver);
    }
}
