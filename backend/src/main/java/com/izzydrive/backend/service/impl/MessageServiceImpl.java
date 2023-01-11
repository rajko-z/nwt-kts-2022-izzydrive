package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.model.Message;
import com.izzydrive.backend.repository.MessageRepository;
import com.izzydrive.backend.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Page<Message> findAllByChannel(String channel, Pageable pageable) {
        return messageRepository.findAllByChannel(channel, pageable);
    }

    @Override
    @Transactional
    public void sendReadReceipt(String channel, String username) {
        messageRepository.sendReadReceipt(channel, username);
    }
}
