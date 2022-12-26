package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.converters.UserDTOConverter;
import com.izzydrive.backend.dto.NewPasswordDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.service.ImageService;
import com.izzydrive.backend.service.users.UserService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final ImageService imageService;

    // if you want to get user with image, call it like /{email}?image=true
    // default value is set to false, so it will return user without image
    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email,
                                                   @RequestParam(defaultValue = "false") boolean image) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            throw new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(email));
        }

        UserDTO retVal = UserDTOConverter.convertBase(user.get());
        if (image) {
            retVal = UserDTOConverter.convertWithImage(user.get(), imageService);
        }
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @GetMapping("/profile-img/{id}")
    public ResponseEntity<String> getUserProfileImage(@PathVariable Long id){
        return ResponseEntity.ok(userService.getProfileImage(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_PASSENGER')")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid NewPasswordDTO newPasswordDTO) {
        userService.changePassword(newPasswordDTO);
        return new ResponseEntity<>("Password successfully changed", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("block/{id}")
    public ResponseEntity<String> blockUser(@PathVariable Long id){
        userService.blockUser(id);
        return new ResponseEntity<>("The user has been successfully blocked", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("unblock/{id}")
    public ResponseEntity<String> unblockUser(@PathVariable Long id){
        userService.unblockUser(id);
        return new ResponseEntity<>("The user has been successfully unblocked", HttpStatus.OK);
    }
}
