package com.izzydrive.backend.repository;

import com.izzydrive.backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
