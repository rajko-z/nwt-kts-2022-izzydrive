package com.izzydrive.backend.model.users;

import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.Route;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="passengers")
@Getter
@Setter
@NoArgsConstructor
public class Passenger extends User {

    @Column(nullable = false)
    private boolean driving;

    @ManyToOne(fetch = FetchType.LAZY)
    private Driving currentDriving;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="favourite_routes",
            joinColumns = @JoinColumn(name="passenger_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="route_id", referencedColumnName = "id"))
    private List<Route> favouriteRoutes = new ArrayList<>();

    public Passenger(String email, String password, String firstName, String lastName, String phoneNumber) {
        super(email, password, firstName, lastName, phoneNumber);
        this.setActivated(false);
        this.setDriving(false);
    }
}
