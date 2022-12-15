package com.izzydrive.backend.model.car;

import com.izzydrive.backend.model.Image;
import com.izzydrive.backend.model.users.Driver;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="cars")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String registration;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private int maxNumOfPassengers;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    @OneToOne(mappedBy = "car", fetch = FetchType.EAGER)
    private Driver driver;
    @Column
    private String carAccommodations; // to escape separate table, e.g. PETS;BABY;FOOD...

    public Car(String registration, String model, int maxNumOfPassengers, CarType carType,  String carAccommodations) {
        this.registration = registration;
        this.model = model;
        this.maxNumOfPassengers = maxNumOfPassengers;
        this.carType = carType;
        this.carAccommodations = carAccommodations;
    }
}
