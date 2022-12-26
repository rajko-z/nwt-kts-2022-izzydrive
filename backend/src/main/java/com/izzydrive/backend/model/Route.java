package com.izzydrive.backend.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="routes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Address start;

    @ManyToOne(fetch = FetchType.EAGER)
    private Address end;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="intermediate_stations",
              joinColumns = @JoinColumn(name="route_id", referencedColumnName = "id"),
              inverseJoinColumns = @JoinColumn(name="address_id", referencedColumnName = "id"))
    private Set<Address> intermediateStations = new HashSet<>();

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    private List<Driving> drivings = new ArrayList<>();
}
