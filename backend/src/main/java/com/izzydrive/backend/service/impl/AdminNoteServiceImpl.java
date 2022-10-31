package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.repository.AdminNoteRepository;
import com.izzydrive.backend.service.AdminNoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminNoteServiceImpl implements AdminNoteService {

    private final AdminNoteRepository adminNoteRepository;
}
