package com.izzydrive.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.izzydrive.backend.model.Notification;
import com.izzydrive.backend.model.NotificationStatus;
import lombok.*;

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

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime reservationTime;
    @NotBlank
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creationDate;
    
    private NotificationStatus notificationStatus;
    private String driverEmail;
    private Long drivingId;

    private UserDTO driverData;

    private CarDTO carData;

    private String driverStr;

    private String carStr;

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
        this.carStr = notification.getCarStr();
        this.driverStr = notification.getDrivarStr();
    }
}
