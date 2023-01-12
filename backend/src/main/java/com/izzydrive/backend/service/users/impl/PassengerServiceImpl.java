package com.izzydrive.backend.service.users.impl;

import com.izzydrive.backend.confirmationToken.ConfirmationTokenService;
import com.izzydrive.backend.dto.NewPassengerDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.email.EmailSender;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.RoleRepository;
import com.izzydrive.backend.repository.users.PassengerRepository;
import com.izzydrive.backend.repository.users.UserRepository;
import com.izzydrive.backend.utils.Validator;
import com.izzydrive.backend.service.users.PassengerService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PasswordEncoder passwordEncoder;

    private final ConfirmationTokenService confirmationTokenService;

    private final RoleRepository roleRepository;

    private final EmailSender emailSender;

    private final UserRepository userRepository;

    private final PassengerRepository passengerRepository;

    public void registerPassenger(NewPassengerDTO newPassengerData){
        validateNewPassengerData(newPassengerData);

        Optional<User> user = userRepository.findByEmail(newPassengerData.getEmail());
        if (user.isEmpty()){
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
        }
        else{
            throw new BadRequestException(ExceptionMessageConstants.USER_ALREADY_EXISTS_MESSAGE);
        }
    }

    private void validateNewPassengerData(NewPassengerDTO newPassengerData)  {
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
}
