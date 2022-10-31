package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.AdminNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminNoteRepository extends JpaRepository<AdminNote, Long> {
}
