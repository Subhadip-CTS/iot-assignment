package com.training.vehicleservice.service;

import com.training.vehicleservice.Exception.DriverNotFoundException;
import com.training.vehicleservice.Exception.ErrorCode;
import com.training.vehicleservice.entity.Driver;
import com.training.vehicleservice.mapstruct.DriverMapper;
import com.training.vehicleservice.pojo.DriverRequest;
import com.training.vehicleservice.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private DriverMapper driverMapper;

    public Driver addNewDriver(DriverRequest driverRequest)
    {
        Driver driver = driverMapper.driverRequestToDriverEntity(driverRequest);
        return driverRepository.save(driver);
    }

    public List<DriverRequest> getAllDrivers()
    {
        List<Driver> allDrivers = driverRepository.findAll();

        return driverMapper.driverEntityToDriverRequest(allDrivers);
    }

    public DriverRequest getDriverById(int id)
    {
        Optional<Driver> driver=driverRepository.findById(id);
        return driver.map((d)-> driverMapper.driverEntityToDriverRequest(d)).orElseThrow(() -> new DriverNotFoundException("Driver with id: "+id+" not found", ErrorCode.DRIVER_NOT_FOUND));
    }

    public boolean deleteDriverById(int id)
    {
        if(driverRepository.existsById(id)){
            driverRepository.deleteById(id);
            return true;
        }
        else
            throw  new DriverNotFoundException("Driver with id: "+id+" not found",ErrorCode.DRIVER_NOT_FOUND);
    }

    public Driver editDriverDetails(Driver updatedDriver)
    {
        if(driverRepository.existsById(updatedDriver.getId()))
        {
           return driverRepository.save(updatedDriver);

        }
        throw  new DriverNotFoundException("Driver with id: "+updatedDriver.getId()+" not found",ErrorCode.DRIVER_NOT_FOUND);
    }
}
