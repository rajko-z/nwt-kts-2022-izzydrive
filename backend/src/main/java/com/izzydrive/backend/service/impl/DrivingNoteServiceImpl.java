package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.DrivingNoteDTO;
import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.exception.BadRequestException;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingNote;
import com.izzydrive.backend.model.users.Admin;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.DriverStatus;
import com.izzydrive.backend.repository.DrivingNoteRepository;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.repository.users.AdminRepository;
import com.izzydrive.backend.service.DrivingNoteService;
import com.izzydrive.backend.service.NotificationService;
import com.izzydrive.backend.service.users.driver.DriverService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class DrivingNoteServiceImpl implements DrivingNoteService {

    private final DrivingNoteRepository drivingNoteRepository;

    private final DriverService driverService;

    private final DrivingRepository drivingRepository;

    private final AdminRepository adminRepository;

    private final NotificationService notificationService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    @Override
    public void rejectDriving(DrivingNoteDTO drivingNoteDTO) {
        Driver driver = driverService.findByEmailWithAllDrivings(drivingNoteDTO.getDriverEmail())
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.userWithEmailDoesNotExist(drivingNoteDTO.getDriverEmail())));

        Driving driving = drivingRepository.findById(drivingNoteDTO.getDrivingId())
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.DRIVING_DOESNT_EXIST));

        driving.setRejected(true);
        drivingRepository.save(driving);

        DrivingNote drivingNote = new DrivingNote(drivingNoteDTO, driver, driving);
        drivingNoteRepository.save(drivingNote);

        List<Admin> admin = adminRepository.findAll();
        this.notificationService.sendNotificationRejectDrivingFromDriver(admin.get(0).getEmail(), driving, driver.getEmail(), drivingNoteDTO.getText());
        updateDriverDrivings(driver, driving);

    }

    private void updateDriverDrivings(Driver driver, Driving driving) {
        if (Objects.equals(driver.getCurrentDriving().getId(), driving.getId())) {
            if (driver.getNextDriving() != null) {
                driver.setCurrentDriving(driver.getNextDriving());
                driver.setNextDriving(null);
                driver.setDriverStatus(DriverStatus.TAKEN);
                this.driverService.save(driver);

                this.simpMessagingTemplate.convertAndSend("/driving/loadCurrentDriving", new DrivingDTO(driver.getCurrentDriving()));
                this.simpMessagingTemplate.convertAndSend("/driving/loadNextDriving", new DrivingDTO(driver.getEmail()));
            } else {
                driver.setCurrentDriving(null);
                driver.setDriverStatus(DriverStatus.FREE);
                this.driverService.save(driver);

                this.simpMessagingTemplate.convertAndSend("/driving/loadCurrentDriving", new DrivingDTO(driver.getEmail()));
            }
        } else {
            if (driver.getCurrentDriving().getStartDate() != null) {
                driver.setDriverStatus(DriverStatus.ACTIVE);
            } else {
                driver.setDriverStatus(DriverStatus.TAKEN);
            }
            driver.setNextDriving(null);
            this.driverService.save(driver);

            this.simpMessagingTemplate.convertAndSend("/driving/loadNextDriving", new DrivingDTO(driver.getEmail()));
        }
    }
}
