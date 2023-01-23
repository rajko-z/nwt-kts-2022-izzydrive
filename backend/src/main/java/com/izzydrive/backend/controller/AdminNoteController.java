package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.AdminNotesDTO;
import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.service.AdminNoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin-notes")
@AllArgsConstructor
public class AdminNoteController {

    private final AdminNoteService adminNoteService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<AdminNotesDTO>> getAdminNotes(@PathVariable Long userId) {
        List<AdminNotesDTO> notesDTOS = adminNoteService.getAdminNotesByUser(userId);
        return new ResponseEntity<>(notesDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<TextResponse> getAdminNotes(@RequestBody AdminNotesDTO adminNotesDTO) {
        adminNoteService.writeNewAdminNote(adminNotesDTO);
        return new ResponseEntity<>(new TextResponse("Successfully add new note"), HttpStatus.OK);
    }
}
