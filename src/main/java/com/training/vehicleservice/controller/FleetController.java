package com.training.vehicleservice.controller;

import com.training.vehicleservice.Exception.ErrorCode;
import com.training.vehicleservice.Exception.GeneralizedException;
import com.training.vehicleservice.entity.Fleet;
import com.training.vehicleservice.service.FleetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class FleetController {
    @Autowired
    FleetService fleetServices;

    @GetMapping("/fleets")
    public ResponseEntity<List<Fleet>> getAllFleet() {

        List<Fleet> fleetList = fleetServices.getFleetList();
        return ResponseEntity.ok(fleetList);
    }

    @DeleteMapping("/fleets/{route}")
    public ResponseEntity deleteFleetByRoute(@PathVariable("route") String route)
    {
        if(fleetServices.deleteFleet(route))
            return ResponseEntity.ok().body("Fleet with route: "+route+" deleted successfully");
        throw new GeneralizedException("Fleet with route not found",  ErrorCode.FLEET_WITH_GIVEN_ROUTE_NOT_FOUND,HttpStatus.NOT_FOUND);
    }

    @PostMapping("/fleet")
    public ResponseEntity<Fleet> saveFleet(@RequestBody Fleet fleet) {
        return new ResponseEntity<Fleet>(fleetServices.saveFleet(fleet), HttpStatus.CREATED);
    }
}
