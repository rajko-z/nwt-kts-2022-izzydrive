package com.izzydrive.backend.service.users.impl;

import com.izzydrive.backend.dto.DriverDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.map.CalculatedRouteDTO;
import com.izzydrive.backend.dto.map.DriverLocationDTO;
import com.izzydrive.backend.dto.map.LocationDTO;
import com.izzydrive.backend.email.EmailSender;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.car.Car;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.RoleRepository;
import com.izzydrive.backend.repository.users.DriverRepository;
import com.izzydrive.backend.service.CarService;
import com.izzydrive.backend.service.maps.MapService;
import com.izzydrive.backend.service.users.DriverService;
import com.izzydrive.backend.service.users.UserService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import com.izzydrive.backend.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    private final CarService carService;

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailSender emailSender;

    private final MapService mapService;

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
    public Optional<Driver> findByEmailWithWorkingIntervals(String email) {
        return this.driverRepository.findByEmailWithWorkingIntervals(email);
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
    public CalculatedRouteDTO getEstimatedRouteLeftFromCurrentDriving(String driverEmail) {
        Optional<Driver> driver = this.driverRepository.findByEmailWithCurrentDrivingAndLocations(driverEmail);
        if (driver.isEmpty()) {
            throw new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(driverEmail));
        }
        if (driver.get().getCurrentDriving() == null) {
            return new CalculatedRouteDTO(new ArrayList<>(), 0, 0);
        }

        Driving currDriving = driver.get().getCurrentDriving();

        if (!currDriving.getDrivingState().equals(DrivingState.FINISHED) &&
            !currDriving.getDrivingState().equals(DrivingState.ACTIVE)) {
            return getEstimatedRouteLeftForDrivingThatDidNotStartYet(driver.get());
        }
        else if (currDriving.getDrivingState().equals(DrivingState.ACTIVE)) {
            return getEstimatedRouteLeftForActiveDriving(driver.get());
        }

        return new CalculatedRouteDTO(new ArrayList<>(), 0, 0);
    }

    private CalculatedRouteDTO getEstimatedRouteLeftForDrivingThatDidNotStartYet(Driver driver) {
        AddressOnMapDTO driverLocation = new AddressOnMapDTO(driver.getLon(), driver.getLat());
        Address tmp = driver.getCurrentDriving().getRoute().getStart();
        AddressOnMapDTO startLocation = new AddressOnMapDTO(tmp.getLongitude(), tmp.getLatitude());

        CalculatedRouteDTO fromDriverToStart = mapService
                .getCalculatedRoutesFromPoints(Arrays.asList(driverLocation, startLocation)).get(0);

        CalculatedRouteDTO fromStartToEnd = convertDrivingToCalculatedRouteDTO(driver.getCurrentDriving());

        return mapService.concatRoutesIntoOne(Arrays.asList(fromDriverToStart, fromStartToEnd));
    }

    private CalculatedRouteDTO getEstimatedRouteLeftForActiveDriving(Driver driver) {
        CalculatedRouteDTO route = convertDrivingToCalculatedRouteDTO(driver.getCurrentDriving());
        List<LocationDTO> coords = route.getCoordinates();
        List<LocationDTO> coordsLeft = new ArrayList<>();
        int coordinatesPassed = 0;

        for (int i = 0; i < coords.size(); ++i) {
            if (driver.getLon() == coords.get(i).getLon() && driver.getLat() == coords.get(i).getLat()) {
                coordinatesPassed = i;
                coordsLeft = coords.subList(i+1, coords.size());
            }
        }

        if (coordinatesPassed == 0) {
            return new CalculatedRouteDTO(new ArrayList<>(), 0, 0);
        }

        double proportion = coords.size() * 1.0 / coordinatesPassed;

        double estimatedDuration = route.getDuration() / proportion;
        double estimatedDistance = route.getDistance() / proportion;

        return new CalculatedRouteDTO(coordsLeft, estimatedDistance, estimatedDuration);
    }

    private CalculatedRouteDTO convertDrivingToCalculatedRouteDTO(Driving driving) {
        List<LocationDTO> coordinates = driving.getLocations().stream()
                .map(l -> new LocationDTO(l.getLongitude(), l.getLatitude()))
                .collect(Collectors.toList());
        return new CalculatedRouteDTO(coordinates,driving.getDistance(), driving.getDuration());
    }

}
