package com.izzydrive.backend.dto;

import com.izzydrive.backend.model.Address;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean blocked;
    private boolean activated;
    private String role;
    private String imageName;
    private Address address;

    public UserDTO(String email, String firstName, String lastName, String phoneNumber, String profileImage){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.imageName = profileImage;
    }
}

