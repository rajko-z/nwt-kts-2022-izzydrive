package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.DrivingNoteDTO;
import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingNote;
import com.izzydrive.backend.model.users.Admin;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.DrivingNoteRepository;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.repository.users.AdminRepository;
import com.izzydrive.backend.repository.users.UserRepository;
import com.izzydrive.backend.service.DrivingNoteService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DrivingNoteServiceImpl implements DrivingNoteService {

    private final DrivingNoteRepository drivingNoteRepository;
    private final UserRepository userRepository;
    private final DrivingRepository drivingRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AdminRepository adminRepository;

    @Override
    public void rejectDriving(DrivingNoteDTO drivingNoteDTO) {
        Optional<User> user = userRepository.findById(drivingNoteDTO.getUserId());
        //Optional<Driving> driving = drivingRepository.findById(drivingNoteDTO.getDrivingId());
//        if(user.isPresent() && driving.isPresent()){
        if(user.isPresent()){
            //DrivingNote drivingNote = new DrivingNote(drivingNoteDTO, user.get(), driving.get());
            //drivingNoteRepository.save(drivingNote);
            List<Admin> admin = adminRepository.findAll();
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Voznja je otkazana");
            notificationDTO.setUserEmail(admin.get(0).getEmail());
            this.simpMessagingTemplate.convertAndSend("/notification/init", notificationDTO);
        }
    }
}
