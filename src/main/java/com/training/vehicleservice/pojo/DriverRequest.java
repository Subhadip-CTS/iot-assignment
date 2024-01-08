package com.training.vehicleservice.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DriverRequest {
    private int id;
    private String name;
    private String address;
    private String licenceNumber;
    private String phoneNumber;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int score;

    public DriverRequest(String name, String address, String lNumber, String pNumber) {
        this.name=name;
        this.licenceNumber=lNumber;
        this.phoneNumber=pNumber;
        this.address=address;
    }

    public DriverRequest(String name, String address, String lNumber, String pNumber, int i) {
        this.name=name;
        this.licenceNumber=lNumber;
        this.phoneNumber=pNumber;
        this.address=address;
        this.score=i;

    }
}
