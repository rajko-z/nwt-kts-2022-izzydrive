package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.dto.driving.DrivingDTO;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.repository.DrivingRepository;
import com.izzydrive.backend.service.DrivingService;
import com.izzydrive.backend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DrivingServiceImpl implements DrivingService {

    private final DrivingRepository drivingRepository;

    @Override
    public List<DrivingDTO> findAllByDriverId(Long driverId) {
        return drivingRepository.findAllByDriverId(driverId)
                .stream().map(DrivingDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<DrivingDTO> findAllByPassengerId(Long passengerId) {
        List<Driving> drivingDTOS = drivingRepository.findAllByPassengerId(passengerId);
        return drivingDTOS.stream().map(DrivingDTO::new).collect(Collectors.toList());
    }

    @Override
    public void save(Driving driving) {
        this.drivingRepository.save(driving);
    }

    @Override
    public boolean passengerApprovedToPayDriving(Driving driving, String passengerEmail) {
        if (driving.getPaymentApprovalIds() == null) {
            return false;
        }
        if (!driving.getPaymentApprovalIds().contains(";")) {
            return driving.getPaymentApprovalIds().equals(passengerEmail);
        }

        String[] approvals = driving.getPaymentApprovalIds().split(";");
        for (String approval : approvals) {
            if (approval.equals(passengerEmail)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean drivingExpiredForPayment(Driving driving) {
        return ChronoUnit.MINUTES.between(driving.getCreationDate(), LocalDateTime.now())
                >= Constants.MAX_NUMBER_OF_MINUTES_TO_COMPLETE_PAYMENT;
    }

    @Override
    public Driving getDrivingByIdWithDriverRouteAndPassengers(Long id) {
        return this.drivingRepository.getDrivingByIdWithDriverRouteAndPassengers(id);
    }

    @Override
    public Driving getDrivingWithLocations(Long id) {
        return this.drivingRepository.getDrivingWithLocations(id);
    }
}
