package com.training.vehicleservice.service;

import com.training.vehicleservice.config.JwtService;
import com.training.vehicleservice.entity.Roles;
import com.training.vehicleservice.entity.User;
import com.training.vehicleservice.pojo.AuthenticationRequest;
import com.training.vehicleservice.pojo.AuthenticationResponse;
import com.training.vehicleservice.pojo.RegisterRequest;
import com.training.vehicleservice.repository.RoleRepository;
import com.training.vehicleservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user=User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Roles.USER)
                .build();

        userRepository.save(user);
        var jwtToken= jwtService.generateToken(user);


        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.
                authenticate(new
                        UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        var user=userRepository.findByEmail(request.getEmail()).orElseThrow(()->new UsernameNotFoundException("USer not found"));
        var jwtToken= jwtService.generateToken(user);


        return AuthenticationResponse.builder().token(jwtToken).build();


    }
}
