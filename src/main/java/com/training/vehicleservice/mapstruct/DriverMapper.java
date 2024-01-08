package com.training.vehicleservice.mapstruct;

import com.training.vehicleservice.entity.Driver;
import com.training.vehicleservice.pojo.DriverRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    @Mapping(target="driverLicenceNumber",source = "driverRequest.licenceNumber")
    Driver driverRequestToDriverEntity(DriverRequest driverRequest);

    // Custom logic , custom setter and getter
    //@Mapping(target = "licenceNumber",source = "driver.driverLicenceNumber",qualifiedByName = "decoratedRegNo")
    @Mapping(target = "licenceNumber",source = "driver.driverLicenceNumber")
    DriverRequest driverEntityToDriverRequest(Driver driver);

    @Mapping(target = "licenceNumber",source = "driver.driverLicenceNumber")
    List<DriverRequest> driverEntityToDriverRequest(List<Driver> drivers);

    @Named("decoratedRegNo")
    default String decoratedRegNo(String regNo)
    {
        return regNo+"DECORATED";
    }
}
