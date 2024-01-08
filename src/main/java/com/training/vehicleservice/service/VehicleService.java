package com.training.vehicleservice.service;

import com.training.vehicleservice.Exception.ErrorCode;
import com.training.vehicleservice.Exception.GeneralizedException;
import com.training.vehicleservice.Exception.VehicleNotFoundException;
import com.training.vehicleservice.entity.Fleet;
import com.training.vehicleservice.entity.Vehicle;
import com.training.vehicleservice.mapstruct.VehicleMapper;
import com.training.vehicleservice.pojo.VehicleRequest;
import com.training.vehicleservice.repository.FleetRepository;
import com.training.vehicleservice.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private FleetRepository fleetRepository;
    
    @Autowired
    private VehicleMapper vehicleMapper;

    @Transactional
    public Vehicle addNewVehicle(Vehicle entityVehicle)
    {

        //Vehicle entityVehicle = vehicleMapper.vehicleRequestToVehicle(vehicleRequest);
        isUniqueRegistrationNumber(entityVehicle.getRegistrationNumber());
        Fleet fleet= entityVehicle.getFleet();
        String route= fleet.getRoute();
        Optional<Fleet> fleetByRoute=fleetRepository.findByRoute(route);

        if(!fleetByRoute.isPresent())
        {
            fleet.setCount(1);
        }
        else
        {
            fleet=fleetByRoute.get();
            fleet.setCount(fleet.getCount()+1);
        }

        entityVehicle.setFleet(fleet);

        return vehicleRepository.save(entityVehicle);
    }

    public List<Vehicle> getAllVehicles()
    {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(int id)
    {
        Optional<Vehicle> vehicle=vehicleRepository.findById(id);
        return vehicle.orElseThrow(() -> new VehicleNotFoundException("Vehicle with id: "+id+"not found", ErrorCode.VEHICLE_NOT_FOUND));
    }

    @Transactional
    public boolean deleteById(int id)
    {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);

        if(vehicle.isPresent())
        {
            if(vehicle.get().getFleet()!=null) {
                Integer fleetId = vehicle.get().getFleet().getId();
                decrementAndUpdateFleetCount(fleetId);
            }

            vehicleRepository.deleteById(id);
            return true;
        }
        return false;

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Vehicle editVehicleDetails(Vehicle updatedVehicleInfo,int id)
    {
        //Vehicle updatedVehicleInfo = vehicleMapper.vehicleRequestToVehicle(updatedVehicleRequest);
        Optional<Vehicle> savedVehicleInfo = vehicleRepository.findById(id);
        if(savedVehicleInfo.isPresent())
        {
            if(savedVehicleInfo.get().getFleet().getRoute().equals(updatedVehicleInfo.getFleet().getRoute()))
            {
                updatedVehicleInfo.setFleet(savedVehicleInfo.get().getFleet());
            }
            else
            {
                Fleet updatedFleetInfo = updateFleetDetails(updatedVehicleInfo.getFleet(), savedVehicleInfo.get().getFleet());
                updatedVehicleInfo.setFleet(updatedFleetInfo);
            }
            return vehicleRepository.save(updatedVehicleInfo);
        }
        throw new VehicleNotFoundException("Vehicle with id: "+id+" not found",ErrorCode.VEHICLE_NOT_FOUND);

    }

    private Fleet updateFleetDetails(Fleet newFleet,Fleet oldFleet)
    {

        Optional<Fleet> newFleetByRoute = fleetRepository.findByRoute(newFleet.getRoute());
        decrementAndUpdateFleetCount(oldFleet.getId());

        if (newFleetByRoute.isPresent()) {
            newFleetByRoute.get().setCount(newFleetByRoute.get().getCount()+1);
            return newFleetByRoute.get();
        }
        else
        {
            newFleet.setCount(1);
            return newFleet;

        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void decrementAndUpdateFleetCount(int id)
    {
        Optional<Fleet> fleet = fleetRepository.findById(id);
        if(fleet.isPresent())
        {
            fleet.get().setCount(fleet.get().getCount()-1);
            fleetRepository.save(fleet.get());
        }

    }

    private void isUniqueRegistrationNumber(String registrationNumber)

    {
        Optional<Vehicle> savedVehicle = vehicleRepository.findByRegistrationNumber(registrationNumber);

        if(savedVehicle.isPresent())
            throw new GeneralizedException(ErrorCode.DUPLICATE_REGISTRATION_NUMBER.getReasonPhrase(),
                    ErrorCode.DUPLICATE_REGISTRATION_NUMBER, HttpStatus.BAD_REQUEST);
    }


}
