package com.izzydrive.backend.model;

import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.Passenger;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Driving {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double price;

    @Column
    private LocalDateTime creationDate;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column(nullable = false)
    private boolean isReservation;

    @Column
    private String note;

    @Column
    private boolean rejected;

    @Column
    private boolean locked;

    @Version
    private Integer version;

    @Enumerated(EnumType.STRING)
    private DrivingState drivingState;

    @Column
    private double duration;

    @Column
    private double distance;

    @OneToMany(mappedBy = "driving", fetch = FetchType.LAZY)
    private List<Evaluation> evaluation = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private DrivingNote drivingNote;

    @OneToOne(fetch = FetchType.LAZY)
    private Driver driver;

    @OneToMany(mappedBy = "currentDriving", fetch = FetchType.LAZY)
    private Set<Passenger> passengers = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Route route;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="driving_id")
    private List<Location> locations = new ArrayList<>();

    public List<Location> getLocations() {
        return locations;
    }

    public List<Location> getLocationsFromDriverToStart() {
        return locations.stream().filter(l -> !l.isForDrive()).collect(Collectors.toList());
    }

    public List<Location> getLocationsFromStartToEnd() {
        return locations.stream().filter(Location::isForDrive).collect(Collectors.toList());
    }
}
