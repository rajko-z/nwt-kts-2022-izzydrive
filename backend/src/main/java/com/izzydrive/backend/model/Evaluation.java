package com.izzydrive.backend.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="evaluations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String text;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private double driverRate;

    @Column(nullable = false)
    private double vehicleGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    private Driving driving;

    public Evaluation(String text, LocalDateTime now, Double driverRate, Double vehicleGrade, Driving driving) {
        this.text = text;
        this.timestamp = now;
        this.driverRate = driverRate;
        this.vehicleGrade = vehicleGrade;
        this.driving = driving;
    }
}
