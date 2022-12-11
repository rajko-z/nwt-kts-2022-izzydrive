package com.izzydrive.backend.service.users.impl;

import com.izzydrive.backend.model.users.MyUser;
import com.izzydrive.backend.repository.users.UserRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;



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


}
