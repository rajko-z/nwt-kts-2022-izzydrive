package com.izzydrive.backend.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double price;

    @Column
    private LocalDateTime reservationDate;

    @Column
    private String message;

    @Column
    private double duration;

    @Column
    private double distance;

    @Column
    private String startLocation;

    @Column
    private String endLocation;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    @Column
    private String userEmail;

    @Column
    private LocalDateTime creationDate;

    @Column
    private String driverEmail;

    @Column
    private Long drivingId;

    @Column
    private String drivarStr;

    @Column
    private String carStr;
}
