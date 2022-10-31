package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.repository.WorkingIntervalRepository;
import com.izzydrive.backend.service.WorkingIntervalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WorkingIntervalServiceImpl implements WorkingIntervalService {

    private final WorkingIntervalRepository workingIntervalRepository;
}
