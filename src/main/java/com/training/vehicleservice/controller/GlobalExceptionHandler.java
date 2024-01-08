package com.training.vehicleservice.controller;

import com.training.vehicleservice.Exception.DriverNotFoundException;
import com.training.vehicleservice.Exception.GeneralizedException;
import com.training.vehicleservice.Exception.VehicleNotFoundException;
import com.training.vehicleservice.entity.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVehicleNotFoundException(VehicleNotFoundException vehicleNotFoundException)
    {

        ErrorResponse build = ErrorResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .description(vehicleNotFoundException.getMessage())
                .errorCode(vehicleNotFoundException.getErrorCode().getValue())
                .errorMessage(vehicleNotFoundException.getErrorCode().getReasonPhrase())
                .localDateTime(LocalDateTime.now()).build();

        return new ResponseEntity<ErrorResponse>(build,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDriverNotFoundException(DriverNotFoundException driverNotFoundException)
    {

        ErrorResponse build = ErrorResponse.builder()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .description(driverNotFoundException.getMessage())
            .errorCode(driverNotFoundException.getErrorCode().getValue())
            .errorMessage(driverNotFoundException.getErrorCode().getReasonPhrase())
            .localDateTime(LocalDateTime.now()).build();

        return new ResponseEntity<ErrorResponse>(build,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GeneralizedException.class)
    public final ResponseEntity<ErrorResponse> handleGeneralizedException(GeneralizedException ex) throws Exception {

        ErrorResponse errorDetails = ErrorResponse.builder()
                .statusCode(ex.getHttpStatus().value())
                .errorCode(ex.getErrorCode().getValue())
                .errorMessage(ex.getErrorCode().getReasonPhrase())
                .description(ex.getMessage())
                .localDateTime(LocalDateTime.now()).build();

        return new ResponseEntity<ErrorResponse>(errorDetails, ex.getHttpStatus());

    }
}
