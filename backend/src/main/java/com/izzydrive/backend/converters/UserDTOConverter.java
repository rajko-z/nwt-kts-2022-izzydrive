package com.izzydrive.backend.converters;


import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.model.users.MyUser;
import com.izzydrive.backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class UserDTOConverter {

    private UserDTOConverter() { }

    public static UserDTO convertFull(MyUser myUser) throws IOException {

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

    public static UserWithTokenDTO convertToUserWithToken(MyUser myUser, String token) throws IOException {
        return UserWithTokenDTO.builder()
                .user(convertFull(myUser))
                .token(token)
                .build();
    }

    public static UserWithTokenDTO convertToUserWithTokenAndImage(MyUser myUser, String token, ImageService imageService) throws IOException {
        return UserWithTokenDTO.builder()
                .user(convertFullWithImage(myUser, imageService))
                .token(token)
                .build();
    }


    public static UserDTO convertFullWithImage(MyUser myUser, ImageService imageService) throws IOException {

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
                .profileImage(imageService.convertImageToBase64(myUser.getImage()))
                .build();
    }
}
