package com.training.vehicleservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.vehicleservice.entity.Fleet;
import com.training.vehicleservice.entity.Vehicle;
import com.training.vehicleservice.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest {

    @MockBean
    private VehicleService mockVehicleService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testForGetMethodCall() throws Exception {
        List<Vehicle> listOfExpectedVehicle=new ArrayList<>();
        listOfExpectedVehicle.add(Vehicle.builder().id(1).model("testVehicleModel1")
                .style("testStyle1").fleet(Fleet.builder().id(1).count(10).route("testRoute1").build())
                .build());
        listOfExpectedVehicle.add(Vehicle.builder().id(2).model("testVehicleModel2")
                .style("testStyle2").fleet(Fleet.builder().id(2).count(10).route("testRoute2").build())
                .build());

        when(mockVehicleService.getAllVehicles()).thenReturn(listOfExpectedVehicle);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        List<Vehicle> actualVehicleList = Arrays.asList(new ObjectMapper().readValue(contentAsString, Vehicle[].class));

        assertEquals(actualVehicleList.size(),listOfExpectedVehicle.size());


    }

    @Test
    public void testForGetVehicleById() throws Exception {
        Vehicle expectedVehicle=setUp();
        when(mockVehicleService.getVehicleById(1)).thenReturn(expectedVehicle);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/vehicles/{id}",expectedVehicle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        Vehicle actualVehicleList = new ObjectMapper().readValue(contentAsString, Vehicle.class);
        assertEquals(actualVehicleList.getRegistrationNumber(),expectedVehicle.getRegistrationNumber());

    }

    @Test
    public void testPostMethodCall() throws Exception {
        Vehicle vehicle=setUp();
      //  when(mockVehicleService.addNewVehicle(vehicle)).thenReturn(vehicle);
        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(vehicle)))
                .andExpect(status().isCreated());


    }

    private static Vehicle setUp() {
        Vehicle vehicle = Vehicle.builder().id(1).model("testVehicleModel")
                .style("testStyle").fleet(Fleet.builder().id(1).count(10).route("testRoute").build())
                .build();

        return vehicle;
    }
}
