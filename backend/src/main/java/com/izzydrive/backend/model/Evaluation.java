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
    private double rate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Driving driving;

    public Evaluation(String text, LocalDateTime now, Double rate, Driving driving) {
        this.text = text;
        this.timestamp = now;
        this.rate = rate;
        this.driving = driving;
    }
}
