package com.izzydrive.backend.service.users.impl;

import com.izzydrive.backend.dto.NewDriverDTO;
import com.izzydrive.backend.email.EmailSender;
import com.izzydrive.backend.model.car.Car;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.repository.AddressRepository;
import com.izzydrive.backend.repository.RoleRepository;
import com.izzydrive.backend.repository.users.DriverRepository;
import com.izzydrive.backend.service.CarService;
import com.izzydrive.backend.service.Validator;
import com.izzydrive.backend.service.users.DriverService;
import com.izzydrive.backend.service.users.UserService;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSender emailSender;

    @Override
    public void addNewDriver(NewDriverDTO driverDTO) throws TemplateException, MessagingException, IOException {
        if(validateNewDriver(driverDTO)){

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

            emailSender.sendDriverRegistrationMail(driverDTO.getEmail(), password, driverDTO.getFirstName());
        }
    }

    private boolean validateNewDriver(NewDriverDTO driverDTO){
        return Validator.validateFirstName(driverDTO.getFirstName()) &&
                Validator.validateLastName(driverDTO.getLastName()) &&
                Validator.validatePhoneNumber(driverDTO.getPhoneNumber()) &&
                Validator.validateEmail(driverDTO.getEmail()) &&
                Validator.validateCarRegistration(driverDTO.getCarData().getRegistration()) &&
                Validator.validateCarType(driverDTO.getCarData().getCarType());
    }
}
