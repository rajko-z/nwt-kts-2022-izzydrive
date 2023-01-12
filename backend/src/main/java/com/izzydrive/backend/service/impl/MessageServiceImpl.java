package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.MessageDTO;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Message;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.MessageRepository;
import com.izzydrive.backend.repository.users.UserRepository;
import com.izzydrive.backend.service.MessageService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    @Override
    public Page<Message> findAllByChannel(String channel, Pageable pageable) {
        return messageRepository.findAllByChannel(channel, pageable);
    }

    @Override
    public Message saveMessage(MessageDTO messageDTO) {
        User user = userRepository.findByEmail(messageDTO.getSender())
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(messageDTO.getSender())));
        Message message = new Message();
        message.setSender(user);
        message.setText(messageDTO.getText());
        message.setChannel(messageDTO.getChannel());

        User recipient = userRepository.findByRole("ROLE_ADMIN").get(0);
        message.setRecipient(recipient);
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    @Override
    @Transactional
    public void sendReadReceipt(String channel, String username) {
        messageRepository.sendReadReceipt(channel, username);
    }
}
