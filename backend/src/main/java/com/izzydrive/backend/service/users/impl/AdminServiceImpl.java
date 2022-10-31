package com.izzydrive.backend.service.users.impl;

import com.izzydrive.backend.repository.users.AdminRepository;
import com.izzydrive.backend.service.users.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
}
