package com.izzydrive.backend.converters;


import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.model.users.MyUser;

public class UserDTOConverter {

    private UserDTOConverter() { }

    public static UserDTO convertFull(MyUser myUser) {

        return UserDTO.builder()
                .id(myUser.getId())
                .firstName(myUser.getFirstName())
                .lastName(myUser.getLastName())
                .activated(myUser.isActivated())
                .blocked(myUser.isBlocked())
                .email(myUser.getEmail())
                .imageName(myUser.getImage() == null ? null : myUser.getImage().getName())
                .role(myUser.getRole().getName())
                .phoneNumber(myUser.getPhoneNumber())
                .address(myUser.getAddress())
                .build();
    }

    public static UserWithTokenDTO convertToUserWithToken(MyUser myUser, String token) {
        return UserWithTokenDTO.builder()
                .user(convertFull(myUser))
                .token(token)
                .build();
    }
}
