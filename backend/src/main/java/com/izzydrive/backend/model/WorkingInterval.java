package com.izzydrive.backend.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="working_intervals")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WorkingInterval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;
}
