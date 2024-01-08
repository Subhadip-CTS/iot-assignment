package com.training.vehicleservice.service;

import com.training.vehicleservice.entity.Fleet;
import com.training.vehicleservice.entity.Vehicle;
import com.training.vehicleservice.repository.FleetRepository;
import com.training.vehicleservice.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FleetService {

    @Autowired
    FleetRepository fleetRepository;

    @Autowired
    VehicleRepository vehicleRepository;
    public List<Fleet> getFleetList() {

        return fleetRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deleteFleet(String route)
    {
        Optional<Fleet> fleet = fleetRepository.findByRoute(route);

        if(fleet.isPresent())
        {
            updateFleetAssignedVehicles(fleet.get());
            fleetRepository.deleteById(fleet.get().getId());
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void updateFleetAssignedVehicles(Fleet fleet)
    {
        if(fleet.getCount()!=0)
        {
            //Optional<List<Vehicle>> vehicles = vehicleRepository.findByFleetId(fleet.getId());
            List<Vehicle> vehicles=fleet.getVehicles();
            List<Vehicle> updatedVehiclesList = vehicles.stream()
                    .map(vehicle -> new Vehicle(vehicle.getId(), vehicle.getRegistrationNumber(), vehicle.getModel(), vehicle.getStyle(), null))
                    .collect(Collectors.toList());
            vehicleRepository.saveAll(updatedVehiclesList);

        }

    }

    public Fleet saveFleet(Fleet fleet){
        return fleetRepository.save(fleet);
    }
}
