package com.izzydrive.backend.model.users.driver;

import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.WorkingInterval;
import com.izzydrive.backend.model.car.Car;
import com.izzydrive.backend.model.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="drivers")
@Getter
@Setter
@NoArgsConstructor
public class Driver extends User {

    @Column(nullable = false)
    private boolean active;

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    @OneToOne(fetch = FetchType.LAZY)
    private Driving currentDriving;

    @OneToOne(fetch = FetchType.LAZY)
    private Driving nextDriving;

    @OneToOne(fetch = FetchType.LAZY)
    private Driving reservedFromClientDriving;

    @OneToOne(fetch = FetchType.EAGER)
    private Car car;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="driver_id")
    private List<WorkingInterval> workingIntervals;

    public Driver(String email, String password, String firstName, String lastName, String phoneNumber) {
        super(email, password, firstName, lastName, phoneNumber);
        this.setActivated(true);
        this.active = false;
        this.driverStatus = DriverStatus.FREE;
    }
}
