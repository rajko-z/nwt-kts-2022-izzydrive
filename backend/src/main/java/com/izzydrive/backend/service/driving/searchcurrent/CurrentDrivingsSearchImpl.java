package com.izzydrive.backend.service.driving.searchcurrent;

import com.izzydrive.backend.converters.DrivingConverter;
import com.izzydrive.backend.dto.driving.DrivingDTOWithLocations;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.driver.Driver;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.driving.DrivingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CurrentDrivingsSearchImpl implements CurrentDrivingsSearch {

    private final DrivingService drivingService;

    @Override
    public List<DrivingDTOWithLocations> getAllCurrentDrivings() {
        List<Driving> currentDrivings = drivingService.findAllCurrentDrivings();
        return currentDrivings.stream().map(DrivingConverter::convertBasicWithDriver).collect(Collectors.toList());
    }

    @Override
    public List<DrivingDTOWithLocations> getAllCurrentDrivingsBySearchTerm(String text) {
        if (text.isBlank()) {
            return getAllCurrentDrivings();
        }
        text = text.toLowerCase();

        List<Driving> currentDrivings = drivingService.findAllCurrentDrivings();
        List<DrivingDTOWithLocations> retVal = new ArrayList<>();

        for (Driving d : currentDrivings) {
            if (driverMatchTerm(d.getDriver(), text) || anyPassengerMatchTerm(d.getPassengers(), text)) {
                retVal.add(DrivingConverter.convertBasicWithDriver(d));
            }
        }
        return retVal;
    }

    private boolean driverMatchTerm(Driver driver, String term) {
        String email = driver.getEmail().toLowerCase();
        String name = driver.getFirstName().toLowerCase();
        String surname = driver.getLastName().toLowerCase();

        return email.contains(term) || name.contains(term) || surname.contains(term);
    }

    private boolean anyPassengerMatchTerm(Collection<Passenger> passengers, String term) {
        for (Passenger p : passengers) {
            String email = p.getEmail().toLowerCase();
            String name = p.getFirstName().toLowerCase();
            String surname = p.getLastName().toLowerCase();

            if (email.contains(term) || name.contains(term) || surname.contains(term)) {
                return true;
            }
        }
        return false;
    }
}


