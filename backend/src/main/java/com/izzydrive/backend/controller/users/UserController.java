package com.izzydrive.backend.controller.users;

import com.izzydrive.backend.converters.UserDTOConverter;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.model.users.MyUser;
import com.izzydrive.backend.service.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
//            List<UserDTO> retVal = userService.findAll().stream()
//                    .map(UserDTOConverter::convertFull).collect(Collectors.toList());
//
//            return new ResponseEntity<>(retVal, HttpStatus.OK);
        return null;
    }

    // isto ne treba
    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_PASSENGER')")
    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) {
        try {
            Optional<MyUser> user = userService.findByEmail(email);
            return user
                    .map(value -> {
                        try {
                            return new ResponseEntity<>(UserDTOConverter.convertFull(value), HttpStatus.OK);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            return null;
        }
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PASSENGER', 'ROLE_DRIVER')")
    @GetMapping("/profile-img/{id}")
    public ResponseEntity<String> getUserProfileImage(@PathVariable Long id){
        return ResponseEntity.ok(userService.getProfileImage(id));
    }
}
