package com.training.vehicleservice.controller;

import com.training.vehicleservice.entity.Telemetry;
import com.training.vehicleservice.pojo.DriverRequest;
import com.training.vehicleservice.pojo.TelemetryResponseList;
import com.training.vehicleservice.repository.DriverRepository;
import com.training.vehicleservice.service.TelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TelemetryController {

    @Autowired
    private TelemetryService telemetryService;

    @Autowired
    private DriverRepository driverRepository;
    @PostMapping("/telemetries")
    public Telemetry addTelemetry(@RequestBody Telemetry telemetry)
    {
        return telemetryService.addNewTelemetryRecord(telemetry);
    }

    @GetMapping("/telemetries/{startdate}/{enddate}")
    public ResponseEntity<List<Telemetry>> getAllTelemetriesBetweenDateRange(@PathVariable("startdate") String startDate, @PathVariable("enddate") String endDate)
    {
        List<Telemetry> allTelemetryBetweenDates = telemetryService.getAllTelemetryBetweenDates(startDate, endDate);

        if(allTelemetryBetweenDates.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(allTelemetryBetweenDates);
    }

    @GetMapping("/telemetries/{parameter}/{startdate}/{enddate}")
    public ResponseEntity<List<Telemetry>> getAllTelemetryByParameter(@PathVariable("parameter") String parameter,@PathVariable("startdate") String startDate,@PathVariable("enddate") String endDate)
    {
        List<Telemetry> allTelemetryByParameter = telemetryService.getAllTelemetryByParameter(startDate, endDate, parameter);

        return allTelemetryByParameter.isEmpty() ? ResponseEntity.noContent().build() :ResponseEntity.ok(allTelemetryByParameter);

    }

    @GetMapping("/telemetries/best-performers/{startdate}/{enddate}")
    public ResponseEntity<List<DriverRequest>> getBestPerformerBetweenDateRange(@PathVariable("startdate") String startDate, @PathVariable("enddate") String endDate)
    {

        Optional<List<DriverRequest>> bestDriver= telemetryService.getBestPerformerBetweenDates(startDate,endDate);

        return bestDriver.map(driver -> ResponseEntity.ok(driver)).orElse(ResponseEntity.noContent().build());
    }


    @GetMapping("/telemetries/bypages")
    public ResponseEntity<TelemetryResponseList> getAllTelemetryBetweenDatesByPage(@RequestParam(name = "page",required = false) Integer page)
    {
        return ResponseEntity.ok(telemetryService.getAllTelemetryByPage(page));
    }
}
