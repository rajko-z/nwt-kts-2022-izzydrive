package com.izzydrive.backend.service.users.admin;

import com.izzydrive.backend.model.users.Admin;
import com.izzydrive.backend.repository.users.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public Admin getAdmin() {
        return adminRepository.findAll().get(0);
    }
}
