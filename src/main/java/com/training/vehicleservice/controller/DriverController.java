package com.training.vehicleservice.controller;

import com.training.vehicleservice.entity.Driver;
import com.training.vehicleservice.pojo.DriverRequest;
import com.training.vehicleservice.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class DriverController {

    @Autowired
    private DriverService driverService;


    @GetMapping("/drivers")
    public CollectionModel<EntityModel<DriverRequest>> getAllDrivers()
    {
        List<DriverRequest> allDriverRequests=driverService.getAllDrivers();

        WebMvcLinkBuilder selfLink = linkTo(methodOn(this.getClass()).getAllDrivers());
        Link createLink = linkTo(methodOn(this.getClass())).slash("/api/drivers").withRel("saveLink").withType("POST");

        List<EntityModel<DriverRequest>> entityModelList=new ArrayList<>();

        for(DriverRequest dr:allDriverRequests)
        {
            WebMvcLinkBuilder editLink=linkTo(methodOn(this.getClass()).getDriverId(dr.getId()));
            WebMvcLinkBuilder deleteLink=linkTo(methodOn(this.getClass()).deleteById(dr.getId()));
            entityModelList.add(EntityModel.of(dr,editLink.withRel("editLink").withType("PUT"),deleteLink.withRel("deleteLink").withType("DELETE")));


        }
        CollectionModel<EntityModel<DriverRequest>> model= CollectionModel.of(entityModelList);
        model.add(selfLink.withSelfRel().withType("GET"),createLink);


        return model;
    }

    @GetMapping("/drivers/{id}")
    public EntityModel<DriverRequest> getDriverId(@PathVariable int id)
    {
        DriverRequest driverById = driverService.getDriverById(id);
        Link editlink=linkTo(DriverController.class).slash("/api/drivers").withRel("editLink").withType("PUT");
        EntityModel<DriverRequest> entityModel=EntityModel.of(driverById,editlink);

        return entityModel;

    }

    @PostMapping("/drivers")
    public ResponseEntity<Driver> saveNewVehicle(@RequestBody DriverRequest driverRequest)
    {
        Driver savedDriver = driverService.addNewDriver(driverRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedDriver.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/driver/{id}")
    public ResponseEntity deleteById(@PathVariable("id") int id)
    {
        return ResponseEntity.ok(driverService.deleteDriverById(id));
    }

    @PatchMapping("/drivers")
    public EntityModel<Driver> editDriverDetails(@RequestBody Driver driver)
    {
        Driver driverDetails = driverService.editDriverDetails(driver);

        Link getLink=linkTo(methodOn(this.getClass()).getDriverId(driverDetails.getId())).withRel("getDriverLink").withType("GET");
        EntityModel<Driver> entityModel=EntityModel.of(driverDetails,getLink);
        return entityModel;
    }
}
