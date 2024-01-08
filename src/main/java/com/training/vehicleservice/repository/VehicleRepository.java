package com.training.vehicleservice.repository;

import com.training.vehicleservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {

    public Optional<Vehicle> findByRegistrationNumber(@Param("registrationNumber") String registrationNumber);

    Optional<List<Vehicle>> findByFleetId(@Param("fleetId") int id);

}
