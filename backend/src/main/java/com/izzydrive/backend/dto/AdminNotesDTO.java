package com.izzydrive.backend.dto;


import com.izzydrive.backend.model.AdminNote;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminNotesDTO {
    private String text;
    private LocalDateTime timestamp;
    private Long userId;

    public AdminNotesDTO(AdminNote adminNote){
        this.text = adminNote.getText();
        this.timestamp = adminNote.getTimestamp();
        this.userId = adminNote.getUserId();
    }
}
