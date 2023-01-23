package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.AdminNotesDTO;

import java.util.List;

public interface AdminNoteService {
    List<AdminNotesDTO> getAdminNotesByUser(Long userId);

    void writeNewAdminNote(AdminNotesDTO adminNotesDTO);
}
