package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.converters.UserDTOConverter;
import com.izzydrive.backend.dto.*;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.service.ImageService;
import com.izzydrive.backend.service.notification.NotificationService;
import com.izzydrive.backend.service.users.UserService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final ImageService imageService;

    private final NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<List<UserDTO>> findAll(){
        List<UserDTO> users = userService.findAllDTO();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

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
    public ResponseEntity<TextResponse> changePassword(@RequestBody @Valid NewPasswordDTO newPasswordDTO) {
        userService.changePassword(newPasswordDTO);
        return new ResponseEntity<>(new TextResponse("Password successfully changed"), HttpStatus.OK);
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_PASSENGER')")
    @PutMapping("/change-info")
    public ResponseEntity<TextResponse> changeBasicUserInfo(@RequestParam(defaultValue = "true") boolean saveChanges, @RequestBody UserDTO userDTO){
        boolean edited = userService.changeUserInfo(userDTO, saveChanges);
        if (edited){
            return ResponseEntity.ok(new TextResponse("Successfully edited data"));
        }
        else{
            this.notificationService.sendNotificationToAdminForDriverChangeData(userDTO);
            return ResponseEntity.ok(new TextResponse("Admin will verify your change"));
        }
    }

    @PostMapping("/reset-password-email")
    public ResponseEntity<TextResponse> sendEmailForResetPassword(@RequestBody String email){
        this.userService.sendEmailForResetPassword(email);
        return ResponseEntity.ok(new TextResponse("We sent you and email with link"));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<TextResponse> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO){
        this.userService.resetPassword(resetPasswordDTO);
        return ResponseEntity.ok(new TextResponse("Successfully reset password "));
    }

    @PostMapping("/response-changes")
    public ResponseEntity<TextResponse> responseDriverChanges(@RequestBody AdminResponseOnChanges response){
        this.notificationService.sendNotificationAdminResponseForChanges(response.getDriverEmail(), response.getResponse());
        return new ResponseEntity<>(new TextResponse("success"), HttpStatus.OK);
    }
}
