package com.training.vehicleservice.service;

import com.training.vehicleservice.Exception.ErrorCode;
import com.training.vehicleservice.Exception.GeneralizedException;
import com.training.vehicleservice.Exception.VehicleNotFoundException;
import com.training.vehicleservice.entity.Fleet;
import com.training.vehicleservice.entity.Vehicle;
import com.training.vehicleservice.mapstruct.VehicleMapper;
import com.training.vehicleservice.repository.FleetRepository;
import com.training.vehicleservice.repository.VehicleRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private FleetRepository fleetRepository;
    
    @Autowired
    private VehicleMapper vehicleMapper;

    @Transactional
    public Vehicle addNewVehicle(Vehicle entityVehicle)
    {

        //Vehicle entityVehicle = vehicleMapper.vehicleRequestToVehicle(vehicleRequest);
        isUniqueRegistrationNumber(entityVehicle.getRegistrationNumber());
        Fleet fleet= entityVehicle.getFleet();
        String route= fleet.getRoute();
        Optional<Fleet> fleetByRoute=fleetRepository.findByRoute(route);

        if(!fleetByRoute.isPresent())
        {
            fleet.setCount(1);
        }
        else
        {
            fleet=fleetByRoute.get();
            fleet.setCount(fleet.getCount()+1);
        }

        entityVehicle.setFleet(fleet);

        return vehicleRepository.save(entityVehicle);
    }

    public List<Vehicle> getAllVehicles()
    {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(int id)
    {
        Optional<Vehicle> vehicle=vehicleRepository.findById(id);
        return vehicle.orElseThrow(() -> new VehicleNotFoundException("Vehicle with id: "+id+"not found", ErrorCode.VEHICLE_NOT_FOUND));
    }

    @Transactional
    public boolean deleteById(int id)
    {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);

        if(vehicle.isPresent())
        {
            if(vehicle.get().getFleet()!=null) {
                Integer fleetId = vehicle.get().getFleet().getId();
                decrementAndUpdateFleetCount(fleetId);
            }

            vehicleRepository.deleteById(id);
            return true;
        }
        return false;

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Vehicle editVehicleDetails(Vehicle updatedVehicleInfo,int id)
    {
        //Vehicle updatedVehicleInfo = vehicleMapper.vehicleRequestToVehicle(updatedVehicleRequest);
        Optional<Vehicle> savedVehicleInfo = vehicleRepository.findById(id);
        if(savedVehicleInfo.isPresent())
        {
            if(savedVehicleInfo.get().getFleet().getRoute().equals(updatedVehicleInfo.getFleet().getRoute()))
            {
                updatedVehicleInfo.setFleet(savedVehicleInfo.get().getFleet());
            }
            else
            {
                Fleet updatedFleetInfo = updateFleetDetails(updatedVehicleInfo.getFleet(), savedVehicleInfo.get().getFleet());
                updatedVehicleInfo.setFleet(updatedFleetInfo);
            }
            return vehicleRepository.save(updatedVehicleInfo);
        }
        throw new VehicleNotFoundException("Vehicle with id: "+id+" not found",ErrorCode.VEHICLE_NOT_FOUND);

    }

    private Fleet updateFleetDetails(Fleet newFleet,Fleet oldFleet)
    {

        Optional<Fleet> newFleetByRoute = fleetRepository.findByRoute(newFleet.getRoute());
        decrementAndUpdateFleetCount(oldFleet.getId());

        if (newFleetByRoute.isPresent()) {
            newFleetByRoute.get().setCount(newFleetByRoute.get().getCount()+1);
            return newFleetByRoute.get();
        }
        else
        {
            newFleet.setCount(1);
            return newFleet;

        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void decrementAndUpdateFleetCount(int id)
    {
        Optional<Fleet> fleet = fleetRepository.findById(id);
        if(fleet.isPresent())
        {
            fleet.get().setCount(fleet.get().getCount()-1);
            fleetRepository.save(fleet.get());
        }

    }

    private void isUniqueRegistrationNumber(String registrationNumber)

    {
        Optional<Vehicle> savedVehicle = vehicleRepository.findByRegistrationNumber(registrationNumber);

        if(savedVehicle.isPresent())
            throw new GeneralizedException(ErrorCode.DUPLICATE_REGISTRATION_NUMBER.getReasonPhrase(),
                    ErrorCode.DUPLICATE_REGISTRATION_NUMBER, HttpStatus.BAD_REQUEST);
    }


    public void processUploadFile(MultipartFile file) throws IOException {
        try(Workbook workbook= WorkbookFactory.create(file.getInputStream())){
            //Process the Excel workbook
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while(sheetIterator.hasNext())
            {
                Sheet sheet=sheetIterator.next();
                processSheet(sheet);
            }


        }

    }

    private void processSheet(Sheet sheet)
    {
        switch (sheet.getSheetName())
        {
            case "create_vehicle":
                processCreateSheet(sheet);
        }
    }

    private List<Vehicle> processCreateSheet(Sheet sheet)
    {
        Iterator<Row> rowIterator = sheet.rowIterator();
        rowIterator.next();

        List<Vehicle> allVehicles=new ArrayList<>();

        while(rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            allVehicles.add(processCreateSheetCell(row));

        }

        Map<String, List<Vehicle>> collect = allVehicles.stream().collect(Collectors.groupingBy(v -> v.getFleet().getRoute()));

        for(Map.Entry<String,List<Vehicle>> entry: collect.entrySet())
        {
            Optional<Fleet> byRoute = fleetRepository.findByRoute(entry.getKey());
            if(byRoute.isPresent())
            {
                byRoute.get().setCount(byRoute.get().getCount()+entry.getValue().size());
                //set byRoute to every vehicle and save all
                setRouteToAllVehicleList(entry.getValue(),byRoute.get());
            }
        }

        return allVehicles;
    }

    private void setRouteToAllVehicleList(List<Vehicle> value, Fleet byRoute) {

        //value.stream().map(v -> v.setFleet(byRoute))
        for(Vehicle v:value)
        {
            v.setFleet(byRoute);
        }
        vehicleRepository.saveAll(value);
    }

    private Vehicle processCreateSheetCell(Row row)
    {
        Iterator<Cell> cellIterator = row.cellIterator();
        Vehicle vehicle=new Vehicle();

        while (cellIterator.hasNext())
        {
            Cell cell=cellIterator.next();
            int columnIndex=cell.getColumnIndex();
            switch (columnIndex)
            {
                case 0:
                    vehicle.setModel(cell.getStringCellValue());
                    break;
                case 1:
                    vehicle.setRegistrationNumber(cell.getStringCellValue());
                    break;
                case 2:
                    vehicle.setStyle(cell.getStringCellValue());
                    break;
                case 3:
                    Fleet fleet=new Fleet();
                    fleet.setRoute(cell.getStringCellValue());
                    vehicle.setFleet(fleet);
                    break;

            }
        }
        return  vehicle;

    }

}
