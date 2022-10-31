package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.repository.MessageRepository;
import com.izzydrive.backend.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
}
