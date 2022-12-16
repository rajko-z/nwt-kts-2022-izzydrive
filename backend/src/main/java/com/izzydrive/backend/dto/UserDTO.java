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
    private String profileImage;
}
