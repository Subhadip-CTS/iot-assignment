package com.training.vehicleservice.service;

import com.training.vehicleservice.Exception.ErrorCode;
import com.training.vehicleservice.Exception.GeneralizedException;
import com.training.vehicleservice.config.BestPerformanceProperties;
import com.training.vehicleservice.entity.Driver;
import com.training.vehicleservice.entity.Telemetry;
import com.training.vehicleservice.entity.Vehicle;
import com.training.vehicleservice.mapstruct.DriverMapper;
import com.training.vehicleservice.pojo.DriverRequest;
import com.training.vehicleservice.pojo.PageDetails;
import com.training.vehicleservice.pojo.TelemetryResponseList;
import com.training.vehicleservice.repository.DriverRepository;
import com.training.vehicleservice.repository.TelemetryRepository;
import com.training.vehicleservice.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TelemetryService {
    @Autowired
    private TelemetryRepository telemetryRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DriverMapper driverMapper;

    @Autowired
    BestPerformanceProperties bestPerformanceProperties;

    public Telemetry addNewTelemetryRecord(Telemetry telemetry)
    {
       Driver driver=telemetry.getDriver();
       Vehicle vehicle=telemetry.getVehicle();
       if(!driverRepository.existsById(driver.getId()))
       {
          throw new GeneralizedException("Driver with id: "+driver.getId()+" does not exists", ErrorCode.DRIVER_NOT_FOUND, HttpStatus.BAD_REQUEST);
       }
       if(!vehicleRepository.existsById(vehicle.getId()))
       {
           throw new GeneralizedException("Vehicle with id: "+vehicle.getId()+" does not exists", ErrorCode.VEHICLE_NOT_FOUND, HttpStatus.BAD_REQUEST);

       }

        return telemetryRepository.save(telemetry);
    }


    public List<Telemetry> getAllTelemetryBetweenDates(String startDate, String endDate)
    {
        Pair<LocalDateTime, LocalDateTime> localDateTimePair = convertDateToDateTime(startDate, endDate);

        Optional<List<Telemetry>> allTelemetryBetweenDateTime = getAllTelemetryBetweenDateTime(localDateTimePair.getFirst(), localDateTimePair.getSecond());

        return allTelemetryBetweenDateTime.orElse(List.of());
    }

    public List<Telemetry> getAllTelemetryByParameter(String startDateTime , String endDateTime, String parameterName)
    {
        LocalDateTime from=LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime to=LocalDateTime.parse(endDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Optional<List<Telemetry>> allTelemetryBetweenDateTime = getAllTelemetryBetweenDateTime(from, to);


        return allTelemetryBetweenDateTime
                .map(telemetries -> telemetries.stream().filter(t->t.getParameter().equals(parameterName)).collect(Collectors.toList()))
                .orElse(List.of());
    }


    private Optional<List<Telemetry>> getAllTelemetryBetweenDateTime(LocalDateTime startDateTime,LocalDateTime endDateTime)
    {
        return Optional.of(telemetryRepository.findAllByTimeBetween(startDateTime,endDateTime));
    }

    public Optional<List<DriverRequest>> getBestPerformerBetweenDates(String startDate, String endDate)
    {
        Pair<LocalDateTime,LocalDateTime> dateTimeRange = convertDateToDateTime(startDate, endDate);


        List<Telemetry> allByTimeBetween = telemetryRepository.findAllByTimeBetween(dateTimeRange.getFirst(), dateTimeRange.getSecond());

        List<DriverRequest> bestDriverAndScore = getBestDriverIdAndScore(allByTimeBetween);
        if(bestDriverAndScore==null)
        {
            return Optional.empty();
        }

        return Optional.of(bestDriverAndScore);

    }


    private List<DriverRequest> getBestDriverIdAndScore(List<Telemetry> allByTimeBetween) {
        List<Pair<Integer, Integer>> pairOfDriverIdAndScore = getPairOfDriverIdAndScore(allByTimeBetween);
        int maximumScore = pairOfDriverIdAndScore.stream().map(Pair::getSecond).mapToInt(Integer::intValue).max().orElseThrow(RuntimeException::new);

        List<DriverRequest> bestDriversByScore = pairOfDriverIdAndScore.stream().filter(p -> p.getSecond() == maximumScore)
                .map(p -> Pair.of(driverRepository.findById(p.getFirst()).get(), p.getSecond()))
                .map(p -> DriverRequest.builder().id(p.getFirst().getId())
                        .name(p.getFirst().getName())
                        .address(p.getFirst().getAddress())
                        .licenceNumber(p.getFirst().getDriverLicenceNumber())
                        .phoneNumber(p.getFirst().getPhoneNumber())
                        .score(p.getSecond()).build()).toList();


        return bestDriversByScore;
    }

    private List<Pair<Integer, Integer>> getPairOfDriverIdAndScore(List<Telemetry> allByTimeBetween) {
        Map<Integer, List<Telemetry>> groupTelemetryByDriverId = allByTimeBetween.stream().collect(Collectors.groupingBy(t-> t.getDriver().getId()));
        List<Pair<Integer, Integer>> pairOfDriverIdAndScore = groupTelemetryByDriverId.entrySet().stream().map(e -> Pair.of(e.getKey(), calculateScore(e.getValue()))).toList();
        return pairOfDriverIdAndScore;
    }

    private static Pair<LocalDateTime, LocalDateTime> convertDateToDateTime(String startDate, String endDate) {
        LocalDate from = LocalDate.parse(startDate, new DateTimeFormatterFactory("yyyy-MM-dd").createDateTimeFormatter());
        LocalDate to = LocalDate.parse(endDate, new DateTimeFormatterFactory("yyyy-MM-dd").createDateTimeFormatter());

        LocalDateTime startLocalDateTime = LocalDateTime.of(from, LocalTime.of(00, 00, 00));
        LocalDateTime endLocalDateTime = LocalDateTime.of(to, LocalTime.of(23, 59, 59));
        return Pair.of(startLocalDateTime,endLocalDateTime);
    }


    private int calculateScore(List<Telemetry> list)
    {
        int positiveScore=0;
        int negativeScore=0;
        List<String> booleanFields = bestPerformanceProperties.getBooleanFields();
        List<String> negativeFields = bestPerformanceProperties.getNegativeFields();
        List<String> calculativeFields = bestPerformanceProperties.getCalculativeFields();
        List<String> scaleUpToTenFields = bestPerformanceProperties.getScaleUpToTenFields();

        for(Telemetry t:list)
        {
            if(booleanFields.contains(t.getParameter()))
            {
                if(negativeFields.contains(t.getParameter()))
                {
                    negativeScore+=calculateScoreForBooleanFields(t.getParamValue());
                }
                else
                {
                    positiveScore+=calculateScoreForBooleanFields(t.getParamValue());
                }
            } else if (calculativeFields.contains(t.getParameter())) {
                positiveScore+=getScoreForCalculativeFields(t.getParamValue(),t.getParameter());
            } else if (scaleUpToTenFields.contains(t.getParameter())) {
                if(negativeFields.contains(t.getParameter()))
                    negativeScore+=t.getParamValue();
                else
                    positiveScore+=t.getParamValue();
            }
        }
        return positiveScore-negativeScore;
    }

    private int calculateScoreForBooleanFields(int paramValue)
    {
        return paramValue==0 ? 0 : 10;
    }

    private int getScoreForCalculativeFields(int paramValue,String paramName)
    {
        if(paramName.equals("distance"))
        {
            return getDistanceScore(paramValue);
        }
        return 0;
    }

    private int getDistanceScore(int distance)
    {
        if(distance<=25)
            return 2;
        else if (distance>25 && distance<=50) {
            return 5;
        } else if (distance>50 && distance<=75) {
            return 7;
        } else if (distance>75 && distance<=100) {
            return 10;
        }
        else {
            return 10;
        }
    }
    
    public TelemetryResponseList getAllTelemetryByPage(Integer page)
    {
        Pageable pageable = PageRequest.of(page == null ? 0 : page, 3);

        Page<Telemetry> telemetryByPage=telemetryRepository.findAll(pageable);

        List<Telemetry> pageContent = telemetryByPage.getContent();

        PageDetails pageDetails = PageDetails.builder().currentPage(telemetryByPage.getNumber())
                .totalNumberOfPage(telemetryByPage.getTotalPages())
                .totalNumberOfRecord(telemetryByPage.getTotalElements()).build();

       return TelemetryResponseList.builder().telemetry(pageContent).pageDetails(pageDetails).build();
    }



}
