package com.izzydrive.backend.model;

import com.izzydrive.backend.model.users.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="admin_notes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String text;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    private User admin;

    @Column
    private Long userId;
}
