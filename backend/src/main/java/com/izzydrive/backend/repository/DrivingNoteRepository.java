package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.DrivingNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrivingNoteRepository extends JpaRepository<DrivingNote, Long> {
}
