package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.DrivingNoteDTO;
import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingNote;
import com.izzydrive.backend.model.users.Admin;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.DrivingNoteRepository;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.repository.users.AdminRepository;
import com.izzydrive.backend.repository.users.UserRepository;
import com.izzydrive.backend.service.DrivingNoteService;
import com.izzydrive.backend.service.NotificationService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DrivingNoteServiceImpl implements DrivingNoteService {

    private final DrivingNoteRepository drivingNoteRepository;
    private final UserRepository userRepository;
    private final DrivingRepository drivingRepository;
    private final AdminRepository adminRepository;
    private final NotificationService notificationService;

    @Transactional
    @Override
    public void rejectDriving(DrivingNoteDTO drivingNoteDTO) {
        User user = userRepository.findById(drivingNoteDTO.getUserId())
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.USER_DOESNT_EXISTS));
        Driving driving = drivingRepository.findById(drivingNoteDTO.getDrivingId())
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST));

        driving.setRejected(true);
        drivingRepository.save(driving);

        DrivingNote drivingNote = new DrivingNote(drivingNoteDTO, user, driving);
        drivingNoteRepository.save(drivingNote);

        List<Admin> admin = adminRepository.findAll();
        this.notificationService.sendNotificationRejectDrivingFromDriver(admin.get(0).getEmail());
    }
}
