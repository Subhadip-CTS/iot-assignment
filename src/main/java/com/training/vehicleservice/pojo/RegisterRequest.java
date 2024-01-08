package com.training.vehicleservice.pojo;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    private String firstName;
     private String lastName;
     private String email;
     private String password;
}
