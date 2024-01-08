package com.training.vehicleservice.mapstruct;

import com.training.vehicleservice.entity.Vehicle;
import com.training.vehicleservice.pojo.VehicleRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    Vehicle vehicleRequestToVehicle(VehicleRequest vehicleRequest);
}
