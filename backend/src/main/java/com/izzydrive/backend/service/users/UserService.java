package com.izzydrive.backend.service.users;

import com.izzydrive.backend.dto.NewPasswordDTO;
import com.izzydrive.backend.dto.ResetPasswordDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.model.users.Role;
import com.izzydrive.backend.model.users.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    List<User> findAll();
    List<UserDTO> findAllDTO();

    Optional<User> findByEmail(String email);

    String generatePassword();

    String getProfileImage(Long userId);

    void changePassword(NewPasswordDTO newPasswordDTO);

    void blockUser(Long id);

    void unblockUser(Long id);
    boolean changeUserInfo(UserDTO userDTO,boolean saveChanges);

    User getCurrentlyLoggedUser();

    void sendEmailForResetPassword(String email);

    void resetPassword(ResetPasswordDTO resetPasswordDTO);

}
