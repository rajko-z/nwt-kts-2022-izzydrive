package com.izzydrive.backend.service.drivingprocessing.shared.helper;

import com.izzydrive.backend.dto.driving.DrivingFinderRequestDTO;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.Route;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.service.AddressService;
import com.izzydrive.backend.service.users.passenger.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProcessingDrivingHelperImpl implements ProcessingDrivingHelper{

    private final AddressService addressService;

    private final PassengerService passengerService;

    @Override
    public Route getRouteFromRequest(DrivingFinderRequestDTO request) {
        Route route = new Route();
        route.setStart(addressService.getAddressFromAddressOnMap(request.getStartLocation()));
        route.setEnd(addressService.getAddressFromAddressOnMap(request.getEndLocation()));
        route.setIntermediateStations(getListOfAddressesFromAddressesOnMap(request.getIntermediateLocations()));
        return route;
    }

    private List<Address> getListOfAddressesFromAddressesOnMap(List<AddressOnMapDTO> addresses) {
        List<Address> retVal = new ArrayList<>();
        for (AddressOnMapDTO a : addresses) {
            retVal.add(addressService.getAddressFromAddressOnMap(a));
        }
        return retVal;
    }

    @Override
    public List<Passenger> getPassengersWithDrivingsFromEmails(Set<String> passengerEmails) {
        List<Passenger> retVal = new ArrayList<>();
        for (String email : passengerEmails) {
            Optional<Passenger> passenger = passengerService.findByEmailWithDrivings(email);
            passenger.ifPresent(retVal::add);
        }
        return retVal;
    }

    @Override
    public List<Passenger> getPassengersWithCurrentDrivingFromEmails(Set<String> passengerEmails) {
        List<Passenger> retVal = new ArrayList<>();
        for (String email : passengerEmails) {
            Passenger passenger = passengerService.findByEmailWithCurrentDriving(email);
            retVal.add(passenger);
        }
        return retVal;
    }
}
