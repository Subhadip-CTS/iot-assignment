package com.training.vehicleservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestRoleBasedAuthenticationController {

    @GetMapping("/admin/hello")
    public ResponseEntity helloAdmin()
    {
        return ResponseEntity.ok().body("Hello Admin");
    }

    @GetMapping("/user/hello")
    public ResponseEntity helloUser()
    {
        return ResponseEntity.ok().body("Hello User");
    }
}
