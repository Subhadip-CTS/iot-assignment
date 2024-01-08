package com.training.vehicleservice.entity;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponse {

    private int statusCode;
    private String description;
    private int errorCode;
    private String errorMessage;
    private LocalDateTime localDateTime;
}
