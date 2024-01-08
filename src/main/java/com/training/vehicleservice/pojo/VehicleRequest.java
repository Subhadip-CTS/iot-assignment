package com.training.vehicleservice.pojo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {
    private int id;
    private String registrationNumber;
    private String model;
    private String style;
    private FleetRequest fleetRequest;
}
