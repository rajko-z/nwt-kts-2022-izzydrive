package com.izzydrive.backend.service;

import com.izzydrive.backend.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MessageService {

    Page<Message> findAllByChannel(String channel, Pageable pageable);

    @Modifying
    @Query(value = "update message set readDate = now() "
            + " where channel = ?1 and sender = ?2 and readDate is null", nativeQuery = true)
    void sendReadReceipt(String channel, String username);
}
