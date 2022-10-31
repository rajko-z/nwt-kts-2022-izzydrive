package com.izzydrive.backend.converters;


import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.model.users.User;

public class UserDTOConverter {

    private UserDTOConverter() { }

    public static UserDTO convertFull(User user) {

        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .activated(user.isActivated())
                .blocked(user.isBlocked())
                .email(user.getEmail())
                .imageName(user.getImage() == null ? null : user.getImage().getName())
                .role(user.getRole().getName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }

    public static UserWithTokenDTO convertToUserWithToken(User user, String token) {
        return UserWithTokenDTO.builder()
                .user(convertFull(user))
                .token(token)
                .build();
    }
}
