package com.training.vehicleservice.controller;

import com.training.vehicleservice.Exception.ErrorCode;
import com.training.vehicleservice.Exception.VehicleNotFoundException;
import com.training.vehicleservice.entity.Vehicle;
import com.training.vehicleservice.pojo.VehicleRequest;
import com.training.vehicleservice.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@Slf4j
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;


    @GetMapping("/vehicles")
    public List<Vehicle> getAllVehicles()
    {
        for(int i=0;i<100;i++)
        {
            log.info("VEHICLE SERVICE RUNNING");
            log.warn("WARNING VEHICLE SERVICE RUNNING");
            log.error("ERROR VEHICLE SERVICE RUNNING");
        }

        return vehicleService.getAllVehicles();
    }

    @GetMapping("/vehicles/{id}")
    @PreAuthorize("hasAuthority(ADMIN)")
    public EntityModel<Vehicle> getAllVehicles(@PathVariable int id)
    {
        Vehicle vehicleById = vehicleService.getVehicleById(id);

        if(vehicleById!=null)
        {
            EntityModel<Vehicle> entityModel=EntityModel.of(vehicleById);
            WebMvcLinkBuilder link1=linkTo(methodOn(this.getClass()).getAllVehicles());
            WebMvcLinkBuilder link2=linkTo(methodOn(this.getClass()).getAllVehicles());
            entityModel.add(link1.withRel("all_vehicles"),link2.withRel("all_vehicles"));

            return entityModel;
        }

        throw new VehicleNotFoundException("Vehicle with id: "+id+" not found",ErrorCode.VEHICLE_NOT_FOUND);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file)
    {
        if(file.isEmpty())
        {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }
        try {
            vehicleService.processUploadFile(file);
            return ResponseEntity.ok().body("Process Successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/vehicles")
    public ResponseEntity<VehicleRequest> saveNewVehicle(@RequestBody Vehicle vehicle)
    {
        Vehicle savedVehicle = vehicleService.addNewVehicle(vehicle);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedVehicle.getId())
                .toUri();

        return ResponseEntity.created(location).build();


        //return new ResponseEntity(vehicleService.addNewVehicle(vehicle), HttpStatus.CREATED);
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity deleteVehicleById(@PathVariable ("id") int id)
    {
        if(vehicleService.deleteById(id))
            return ResponseEntity.ok("Vehicle with id: "+id+" deleted successfully");
        else
            throw new VehicleNotFoundException("Vehicle with id: "+id+" not found", ErrorCode.VEHICLE_NOT_FOUND);
    }

    @PutMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> editVehicleById(@RequestBody Vehicle vehicle,@PathVariable("id") int id)
    {
        return ResponseEntity.ok(vehicleService.editVehicleDetails(vehicle,id));
    }
}
