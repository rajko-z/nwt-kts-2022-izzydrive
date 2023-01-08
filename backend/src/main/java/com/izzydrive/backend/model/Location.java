package com.izzydrive.backend.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="locations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double latitude;

    @Column
    private double longitude;
}
