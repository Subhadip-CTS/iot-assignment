package com.training.vehicleservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "driver")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name; // Jhon Bay  jhin Bay
    private String address;
    @Column(name = "licence_number")
    private String driverLicenceNumber;
    @Column(name = "phone_number")
    private String phoneNumber;


}
