package com.training.vehicleservice.Exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VehicleNotFoundException extends RuntimeException{

    private ErrorCode errorCode;
    public VehicleNotFoundException(String message)
    {
        super(message);
    }

    public VehicleNotFoundException(String message,ErrorCode errorCode)
    {
        super(message);
        this.errorCode=errorCode;
    }

}
