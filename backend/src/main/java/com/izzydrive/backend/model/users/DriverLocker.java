package com.izzydrive.backend.model.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="driver_lockers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverLocker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String driverEmail;

    @Column
    private String passengerEmail;

    @Version
    private int version;

    public DriverLocker(String driverEmail, String passengerEmail, int version) {
        this.driverEmail = driverEmail;
        this.passengerEmail = passengerEmail;
        this.version = version;
    }
}
