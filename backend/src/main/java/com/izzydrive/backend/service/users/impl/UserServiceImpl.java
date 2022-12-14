package com.izzydrive.backend.service.users.impl;

import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.InternalServerException;
import com.izzydrive.backend.model.users.MyUser;
import com.izzydrive.backend.repository.users.UserRepository;
import com.izzydrive.backend.service.ImageService;
import com.izzydrive.backend.service.users.UserService;
import lombok.AllArgsConstructor;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.izzydrive.backend.utils.ExceptionMessageConstants.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private ImageService imageService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = userRepository.findByEmail(username);
        if (user.isPresent())
            return user.get();
        throw new UsernameNotFoundException("User with username: " + username + " not found");
    }

    @Override
    public List<MyUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<MyUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void processOAuthPostLogin(String username) {
        Optional<MyUser> existUser = userRepository.findByEmail(username);

    }

    @Override
    public String generatePassword() {
        CharacterRule upperCase = new CharacterRule(EnglishCharacterData.UpperCase);
        CharacterRule numbers = new CharacterRule(EnglishCharacterData.Digit);
        CharacterRule lowerCase = new CharacterRule(EnglishCharacterData.LowerCase);
        CharacterRule special = new CharacterRule(EnglishCharacterData.Special);

        List<CharacterRule> rules = new ArrayList<>();
        rules.add(upperCase);
        rules.add(lowerCase);
        rules.add(numbers);
        rules.add(special);

        PasswordGenerator passwordGenerator = new PasswordGenerator();
        return passwordGenerator.generatePassword(10, rules);

    }

    @Override
    public String getProfileImage(Long userId) {
        Optional<MyUser> user = userRepository.findById(userId);
        if(user.isPresent()){
            try {
                return imageService.convertImageToBase64(user.get().getImage());
            } catch (IOException e) {
                throw new InternalServerException(SOMETHING_WENT_WRONG_MESSAGE);
            }
        }
        else{
            throw new BadRequestException(USER_DOESNT_EXISTS);
        }
    }
}
