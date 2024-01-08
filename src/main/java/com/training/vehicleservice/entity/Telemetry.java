package com.training.vehicleservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "driverId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Driver driver;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "vehicleId")
    private Vehicle vehicle;

    private String parameter;

    @Column(name = "param_value")
    private Integer paramValue;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "UTC")
    private LocalDateTime time;

    @PrePersist
    protected void onCreate()
    {
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyy-MM-dd'T'HH:mm:ss").createDateTimeFormatter();
        time= LocalDateTime.parse(LocalDateTime.now().format(dateTimeFormatter));
    }
}
