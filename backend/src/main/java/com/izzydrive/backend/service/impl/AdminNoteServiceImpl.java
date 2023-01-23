package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.AdminNotesDTO;
import com.izzydrive.backend.model.AdminNote;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.AdminNoteRepository;
import com.izzydrive.backend.service.AdminNoteService;
import com.izzydrive.backend.service.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminNoteServiceImpl implements AdminNoteService {

    private final AdminNoteRepository adminNoteRepository;

    private final UserService userService;

    @Override
    public List<AdminNotesDTO> getAdminNotesByUser(Long userId) {
        User admin = userService.getCurrentlyLoggedUser();
        return adminNoteRepository.findByUserIdAndAdmin(userId, admin.getId())
                .stream().map(AdminNotesDTO::new).collect(Collectors.toList());
    }

    @Override
    public void writeNewAdminNote(AdminNotesDTO adminNotesDTO) {
        User admin = userService.getCurrentlyLoggedUser();
        AdminNote note = new AdminNote();
        note.setAdmin(admin);
        note.setText(adminNotesDTO.getText());
        note.setTimestamp(adminNotesDTO.getTimestamp());
        note.setUserId(adminNotesDTO.getUserId());
        adminNoteRepository.save(note);
    }
}
