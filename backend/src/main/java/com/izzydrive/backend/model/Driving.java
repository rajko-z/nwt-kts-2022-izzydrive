package com.izzydrive.backend.model;

import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.Passenger;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String drivingApprovalIds;

    @Column
    private String paymentApprovalIds;

    @Enumerated(EnumType.STRING)
    private DrivingState drivingState;

    @OneToMany(mappedBy = "driving", fetch = FetchType.LAZY)
    private List<Evaluation> evaluation = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private DrivingNote drivingNote;

    @OneToOne(fetch = FetchType.LAZY)
    private Driver driver;

    @OneToMany(mappedBy = "currentDriving", fetch = FetchType.LAZY)
    private List<Passenger> passengers = new ArrayList<>();
}
