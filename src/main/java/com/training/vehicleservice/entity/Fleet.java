package com.training.vehicleservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fleet")
public class Fleet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String route;
    private int count;

    @OneToMany(mappedBy = "fleet")
    @JsonIgnoreProperties("fleet")
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private List<Vehicle> vehicles;
}
