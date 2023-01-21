package com.izzydrive.backend.dto.driving;

import com.izzydrive.backend.dto.AddressDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.DrivingState;
import com.izzydrive.backend.model.users.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DrivingDTO {
    private Long id;
    private double price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> passengers = new ArrayList<>();
    private AddressDTO start;
    private AddressDTO end;
    private boolean isFavoriteRoute;
    private boolean evaluationAvailable;
    private DrivingState drivingState;
    private LocalDateTime reservationDate;
    private String driverEmail;

    public DrivingDTO(Driving driving) {
        this.id = driving.getId();
        this.price = driving.getPrice();
        this.startDate = driving.getStartDate();
        this.endDate = driving.getEndDate();
        this.passengers = driving.getPassengers().stream().map(User::getEmail).collect(Collectors.toList());
        this.start = new AddressDTO(driving.getRoute().getStart());
        this.end = new AddressDTO(driving.getRoute().getEnd());
        this.drivingState = driving.getDrivingState();
        this.reservationDate = driving.getReservationDate();
        this.driverEmail = driving.getDriver().getEmail();
    }

    public DrivingDTO(String driverEmail) {
        this.driverEmail = driverEmail;
    }
}
