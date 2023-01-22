package com.izzydrive.backend.service.users.admin;

import com.izzydrive.backend.repository.users.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
}
