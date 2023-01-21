package com.izzydrive.backend.dto;

import com.izzydrive.backend.model.Notification;
import com.izzydrive.backend.model.NotificationStatus;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDTO {
    private Long id;
    @NotBlank
    private String userEmail;
    @NotBlank
    private String message;
    private String startLocation;
    private String endLocation;
    private List<String> intermediateLocations;
    private double duration;
    private double price;
    private LocalDateTime reservationTime;
    @NotBlank
    private LocalDateTime creationDate;
    private NotificationStatus notificationStatus;
    private String driverEmail;
    private Long drivingId;

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.userEmail = notification.getUserEmail();
        this.message = notification.getMessage();
        this.endLocation = notification.getEndLocation();
        this.startLocation = notification.getStartLocation();
        this.duration = notification.getDuration();
        this.price = notification.getPrice();
        this.reservationTime = notification.getReservationDate();
        this.creationDate = notification.getCreationDate();
        this.notificationStatus = notification.getNotificationStatus();
        this.driverEmail = notification.getDriverEmail();
        this.drivingId = notification.getDrivingId();
    }
}
