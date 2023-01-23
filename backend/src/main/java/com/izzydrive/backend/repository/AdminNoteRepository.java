package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.AdminNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminNoteRepository extends JpaRepository<AdminNote, Long> {
    @Query("SELECT a FROM AdminNote a WHERE a.admin.id = ?2 AND a.userId = ?1")
    List<AdminNote> findByUserIdAndAdmin(Long userId, Long id);
}
