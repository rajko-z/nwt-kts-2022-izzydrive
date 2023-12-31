package com.izzydrive.backend.dto;

import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.User;
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
    public UserDTO(Driver driver){
        this.id = driver.getId();
        this.email = driver.getEmail();
        this.firstName = driver.getFirstName();
        this.lastName = driver.getLastName();
        this.phoneNumber = driver.getPhoneNumber();
        this.blocked = driver.isBlocked();
        this.role = driver.getRole().getName();
    }

    public UserDTO(Passenger passenger) {
        this.id = passenger.getId();
        this.email = passenger.getEmail();
        this.firstName = passenger.getFirstName();
        this.lastName = passenger.getLastName();
        this.phoneNumber = passenger.getPhoneNumber();
        this.blocked = passenger.isBlocked();
        this.role = passenger.getRole().getName();
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.blocked = user.isBlocked();
        this.role = user.getRole().getName();
    }
}

