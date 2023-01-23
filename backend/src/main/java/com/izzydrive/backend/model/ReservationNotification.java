package com.izzydrive.backend.model;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReservationNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long drivingId;

    @Column
    private boolean firstNotification;

    @Column
    private boolean secondNotification;

    @Column
    private boolean thirdNotification;

    public ReservationNotification(Long drivingId, boolean firstNotification, boolean secondNotification, boolean thirdNotification){
        this.drivingId = drivingId;
        this.firstNotification = firstNotification;
        this.secondNotification = secondNotification;
        this.thirdNotification = thirdNotification;
    }
}
