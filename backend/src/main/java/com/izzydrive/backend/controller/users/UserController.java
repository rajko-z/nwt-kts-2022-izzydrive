package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.converters.UserDTOConverter;
import com.izzydrive.backend.dto.NewPasswordDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.service.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<UserDTO> retVal = userService.findAll().stream()
                .map(UserDTOConverter::convertFull).collect(Collectors.toList());

        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user
                .map(value -> new ResponseEntity<>(UserDTOConverter.convertFull(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_PASSENGER')")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid NewPasswordDTO newPasswordDTO) {
        userService.changePassword(newPasswordDTO);
        return new ResponseEntity<>("Password successfully changed", HttpStatus.OK);
    }
}
