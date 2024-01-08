package com.training.vehicleservice.Exception;

public enum ErrorCode {

    VEHICLE_NOT_FOUND(1001, "Vehicle Not Found"),
    DRIVER_NOT_FOUND(1002, "Driver Not Found"),
    DUPLICATE_REGISTRATION_NUMBER(1003, "Vehicle with same registration number already exists"),
    DUPLICATE_LICENCE_NUMBER(1004, "Driver with same licence number already exists"),
    SOMETHING_WENT_WRONG(1005, "Something went wrong"),
    FLEET_WITH_GIVEN_ROUTE_NOT_FOUND(1006,"Fleet with route not found");

    ErrorCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    private final int value;

    private final String reasonPhrase;

    public int getValue() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
}
