package com.izzydrive.backend.utils;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.enumerations.IntermediateStationsOrderType;
import com.izzydrive.backend.enumerations.OptimalDrivingType;
import com.izzydrive.backend.model.car.CarAccommodation;

import java.util.ArrayList;
import java.util.HashSet;

public class DrivingFinderRequestUtil {

    public static DrivingFinderRequestDTO getSimpleRequest() {
        DrivingFinderRequestDTO d = new DrivingFinderRequestDTO();
        d.setStartLocation(AddressesUtil.getBanijskaAddress());
        d.setEndLocation(AddressesUtil.getZeleznickaAddress());
        d.setOptimalDrivingType(OptimalDrivingType.NO_PREFERENCE);
        d.setCarAccommodation(new CarAccommodation(true, true, true, true));
        d.setIntermediateLocations(new ArrayList<>());
        d.setReservation(false);
        d.setLinkedPassengersEmails(new HashSet<>());
        d.setScheduleTime(null);
        d.setIntermediateStationsOrderType(IntermediateStationsOrderType.IN_ORDER);
        return d;
    }

}
