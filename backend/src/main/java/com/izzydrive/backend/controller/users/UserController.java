package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.converters.UserDTOConverter;
import com.izzydrive.backend.dto.LoginDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.service.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    // samo za testiranje, ovde authorize ni ne treba, mozda ni cela metoda
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<UserDTO> retVal = userService.findAll().stream()
                .map(UserDTOConverter::convertFull).collect(Collectors.toList());

        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    // isto ne treba
    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_PASSENGER')")
    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user
                .map(value -> new ResponseEntity<>(UserDTOConverter.convertFull(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }


}
