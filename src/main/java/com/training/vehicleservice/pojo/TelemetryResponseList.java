package com.training.vehicleservice.pojo;

import com.training.vehicleservice.entity.Telemetry;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TelemetryResponseList {
    private List<Telemetry> telemetry;
    private PageDetails pageDetails;
}
