package com.training.vehicleservice.repository;

import com.training.vehicleservice.entity.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TelemetryRepository extends JpaRepository<Telemetry,Integer> {

    public List<Telemetry> findAllByTimeBetween(LocalDateTime startLocalDate, LocalDateTime endDate);

}
