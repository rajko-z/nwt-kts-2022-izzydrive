package com.izzydrive.backend.service.users;

import com.izzydrive.backend.confirmationToken.ConfirmationToken;
import com.izzydrive.backend.confirmationToken.ConfirmationTokenService;
import com.izzydrive.backend.dto.NewPasswordDTO;
import com.izzydrive.backend.dto.ResetPasswordDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.email.EmailSender;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.users.UserRepository;
import com.izzydrive.backend.service.ImageService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import com.izzydrive.backend.utils.Validator;
import lombok.AllArgsConstructor;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.izzydrive.backend.utils.ExceptionMessageConstants.USER_DOESNT_EXISTS;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ImageService imageService;

    private final PasswordEncoder passwordEncoder;

    private final EmailSender emailSender;

    private final ConfirmationTokenService confirmationTokenService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent())
            return user.get();
        throw new UsernameNotFoundException("User with username: " + username + " not found");
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public List<UserDTO> findAllDTO() {
        return userRepository.findAll().stream().sorted(Comparator.comparing(User::getId))
                .map(UserDTO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getCurrentlyLoggedUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.findByEmail(userEmail)
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.userWithEmailDoesNotExist(userEmail)));
    }

    @Override
    public void changePassword(NewPasswordDTO newPasswordDTO) {
        User user = userRepository.findByEmail(newPasswordDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(newPasswordDTO.getEmail())));

        if (passwordsMatch(newPasswordDTO.getNewPassword(), user.getPassword())) {
            throw new BadRequestException(ExceptionMessageConstants.NEW_PASSWORD_SAME_AS_PREVIOUS);
        }
        if (!passwordsMatch(newPasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException(ExceptionMessageConstants.INVALID_CURRENT_PASSWORD);
        }
        if (Validator.validatePassword(newPasswordDTO.getNewPassword())) {
            user.setPassword(passwordEncoder.encode(newPasswordDTO.getNewPassword()));
            userRepository.save(user);
        }
    }

    @Override
    public void blockUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setBlocked(true);
            userRepository.save(user.get());
        }
    }

    @Override
    public void unblockUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setBlocked(false);
            userRepository.save(user.get());
        }
    }

    @Override
    public boolean changeUserInfo(UserDTO userDTO, boolean saveChanges) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(userDTO.getEmail())));

        validateNewUserData(userDTO);
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        if (userDTO.getImageName() != null) {
            imageService.convertImageFromBase64(userDTO.getImageName(), userDTO.getEmail());
        }
        if(saveChanges){
            userRepository.save(user);
            return true;
        }
        else{
            return false;
        }

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
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return imageService.convertImageToBase64(user.get().getImage());
        } else {
            throw new BadRequestException(USER_DOESNT_EXISTS);
        }
    }

    @Override
    public User disconnectFromChat(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return null;
        }

        user.get().setConnected(false);
        return userRepository.save(user.get());
    }

    @Override
    public void sendEmailForResetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(email)));
        String token = UUID.randomUUID().toString();
        this.confirmationTokenService.createVerificationToken(user, token);
        this.emailSender.sendResetPasswordLink(user.getEmail(), token);
    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        ConfirmationToken token  = confirmationTokenService.findByToken(resetPasswordDTO.getToken());
        User user = token.getUser();
        if (user != null &&
                Validator.validateMatchingPassword(resetPasswordDTO.getPassword(), resetPasswordDTO.getRepeatedPassword()) &&
                Validator.validatePassword(resetPasswordDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
            userRepository.save(user);
        }
    }

    private boolean passwordsMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private void validateNewUserData(UserDTO userDTO) {
        Validator.validateEmail(userDTO.getEmail());
        Validator.validateFirstName(userDTO.getFirstName());
        Validator.validateLastName(userDTO.getLastName());
        Validator.validatePhoneNumber(userDTO.getPhoneNumber());
    }




}
