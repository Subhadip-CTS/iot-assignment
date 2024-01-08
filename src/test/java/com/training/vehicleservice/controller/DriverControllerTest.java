package com.training.vehicleservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.vehicleservice.controller.DriverController;
import com.training.vehicleservice.entity.Driver;
import com.training.vehicleservice.pojo.DriverRequest;
import com.training.vehicleservice.service.DriverService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DriverController.class)
@ExtendWith(MockitoExtension.class)
/*@SpringBootTest
@AutoConfigureMockMvc*/
public class DriverControllerTest {

    @MockBean
    private DriverService mockDriverService;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    HttpServletRequest httpServletRequest;



    @Test
    public void testPostMethodCall() throws Exception {
        DriverRequest driverRequest = new DriverRequest("Abc", "Address", "LNumber", "PNumber");
        Driver response=new Driver(1,"Abc", "Address", "LNumber", "PNumber" );

        String baseUrl= httpServletRequest.getRequestURL().toString();
        String exceptedLocation=baseUrl+"/api/drivers/1";

        when(mockDriverService.addNewDriver(any(DriverRequest.class))).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(driverRequest)))
                .andExpect(status().isCreated());

       /* String responseAsString = resultActions.andReturn().getResponse().getContentAsString();
        Driver driverResponse = new ObjectMapper().readValue(responseAsString, Driver.class);*/

        String actualLocation = resultActions.andReturn().getResponse().getHeader("location");
        assertEquals(exceptedLocation,actualLocation);
        verify(mockDriverService,times(1)).addNewDriver(any(DriverRequest.class));
       // assertNotNull(valueOf(driverResponse.getId()));


    }

    @Test
    public void testGetMethodCall() throws Exception {

        List<Driver> driverList=new ArrayList<>();
        driverList.add(new Driver(1, "testName1", "testAddress1", "testLNumber1", "testPNumber1"));
        driverList.add(new Driver(2, "testName2", "testAddress2", "testLNumber2", "testPNumber2"));

       // when(mockDriverService.getAllDrivers()).thenReturn(driverList);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/drivers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        List<Driver> driver1 = Arrays.asList(new ObjectMapper().readValue(contentAsString, Driver[].class));

        assertEquals(driver1.size(),driverList.size());


    }

    @Test
    public void testGetMethodCallForParticularId() throws Exception {

        Driver expectedDriver = new Driver(10, "testName", "testAddress", "testLNumber", "testPNumber");
       // when(mockDriverService.getDriverById(10)).thenReturn(expectedDriver);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/drivers/{id}",expectedDriver.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        Driver actualDriver = new ObjectMapper().readValue(contentAsString, Driver.class);

        assertEquals(actualDriver.getName(),expectedDriver.getName());


    }





}
