package com.izzydrive.backend.model.users;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="admins")
@NoArgsConstructor
public class Admin extends MyUser {

    public Admin(String email, String password, String firstName, String lastName, String phoneNumber) {
        super(email, password, firstName, lastName, phoneNumber);
        this.setActivated(true);
    }
}
