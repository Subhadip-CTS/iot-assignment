package com.training.vehicleservice.Exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DriverNotFoundException extends RuntimeException{

    private ErrorCode errorCode;
    public DriverNotFoundException(String message)
    {
        super(message);
    }

    public DriverNotFoundException(String message,ErrorCode errorCode)
    {
        super(message);
        this.errorCode=errorCode;
    }
}
