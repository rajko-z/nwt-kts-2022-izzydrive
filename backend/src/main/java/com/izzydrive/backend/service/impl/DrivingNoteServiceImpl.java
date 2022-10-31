package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.repository.DrivingNoteRepository;
import com.izzydrive.backend.service.DrivingNoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DrivingNoteServiceImpl implements DrivingNoteService {

    private final DrivingNoteRepository drivingNoteRepository;
}
