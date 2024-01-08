package com.training.vehicleservice.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Getter
public class GeneralizedException extends  RuntimeException{

    private ErrorCode errorCode;
    private LocalDateTime timestamp;
    private HttpStatus httpStatus;

    public GeneralizedException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp = LocalDateTime.now();
    }
    public GeneralizedException(String message)
    {
        super(message);
    }
}
