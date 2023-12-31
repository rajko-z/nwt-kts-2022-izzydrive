package com.izzydrive.backend.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="adresses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String state;

    @Column
    private String city;

    @Column
    private String street;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private double latitude;

    @Column
    private String name;

    public Address(String name){
        this.name = name;
    }

    public Address(double longitude, double latitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

}
