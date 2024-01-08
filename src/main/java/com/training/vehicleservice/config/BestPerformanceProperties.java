package com.training.vehicleservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "telemetry")
@Getter
@Setter
public class BestPerformanceProperties {

    private List<String> booleanFields;
    private List<String> scaleUpToTenFields;
    private List<String> calculativeFields;
    private List<String> negativeFields;
}
