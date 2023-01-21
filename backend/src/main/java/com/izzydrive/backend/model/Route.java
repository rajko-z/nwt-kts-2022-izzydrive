package com.izzydrive.backend.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Address start;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Address end;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderColumn(name = "station_order")
    @JoinTable(name="intermediate_stations",
              joinColumns = @JoinColumn(name="route_id", referencedColumnName = "id"),
              inverseJoinColumns = @JoinColumn(name="address_id", referencedColumnName = "id"))
    private List<Address> intermediateStations;

    public Route(Address start, Address end){
        this.start = start;
        this.end = end;
    }
}
