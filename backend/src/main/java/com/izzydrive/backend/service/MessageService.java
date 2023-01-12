package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.MessageDTO;
import com.izzydrive.backend.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

    Page<Message> findAllByChannel(String channel, Pageable pageable);

    Message saveMessage(MessageDTO messageDTO);
    void sendReadReceipt(String channel, String username);
}
