package com.izzydrive.backend.converters;


import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.dto.UserWithTokenDTO;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.service.ImageService;

public class UserDTOConverter {

    private UserDTOConverter() { }

    public static UserDTO convertBase(User user) {
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

    public static UserWithTokenDTO convertToUserWithImageAndToken(User user, String token, ImageService imageService) {
        return UserWithTokenDTO.builder()
                .user(convertWithImage(user, imageService))
                .token(token)
                .build();
    }

    public static UserDTO convertWithImage(User user, ImageService imageService) {
        UserDTO userDTO = convertBase(user);
        if (user.getImage() != null){
            userDTO.setImageName(imageService.convertImageToBase64(user.getImage()));
        }
        return userDTO;
    }
}
