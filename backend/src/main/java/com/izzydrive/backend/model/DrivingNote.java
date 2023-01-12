package com.izzydrive.backend.model;

import com.izzydrive.backend.dto.DrivingNoteDTO;
import com.izzydrive.backend.model.users.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="driving_notes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DrivingNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String text;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean fromPassenger;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(mappedBy = "drivingNote", fetch = FetchType.LAZY)
    private Driving driving;

    public DrivingNote(DrivingNoteDTO drivingNoteDTO, User user, Driving driving){
        this.text = drivingNoteDTO.getText();
        this.timestamp =  drivingNoteDTO.getTimestamp();
        this.fromPassenger = drivingNoteDTO.isFromPassenger();
        this.user = user;
        this.driving = driving;
    }
}
