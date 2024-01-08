package com.training.vehicleservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "registration_number")
    private String registrationNumber;
    private String model;
    private String style;

    /*@JsonIgnore
    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.REMOVE)
    private List<Telemetry> telemetries;*/

    @ManyToOne
    @JoinColumn(name = "fleet_id")
    @JsonIgnoreProperties("vehicles")
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.MERGE})
    private Fleet fleet;
}
