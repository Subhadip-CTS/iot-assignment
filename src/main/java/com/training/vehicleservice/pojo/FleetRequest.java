package com.training.vehicleservice.pojo;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FleetRequest {
    private int id;
    private String route;
    private int count;
}
