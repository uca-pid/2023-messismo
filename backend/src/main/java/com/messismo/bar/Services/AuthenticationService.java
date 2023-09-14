package com.messismo.bar.Services;

import com.messismo.bar.DTOs.AuthenticationRequestDTO;
import com.messismo.bar.DTOs.RegisterRequestDTO;
import com.messismo.bar.Entities.AuthenticationResponse;
import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWtService jWtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequestDTO registerRequestDTO) {
        User newUser = new User();
        newUser.setUsername(registerRequestDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        newUser.setRole(Role.EMPLOYEE);
        userRepository.save(newUser);
        String jwtToken = jWtService.generateToken(newUser);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequestDTO authenticationRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getUsername(), authenticationRequestDTO.getPassword()));
        User user = userRepository.findByUsername(authenticationRequestDTO.getUsername()).orElseThrow();
        String jwtToken = jWtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public ResponseEntity<?> loginUser(AuthenticationRequestDTO authenticationRequestDTO) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getUsername(), authenticationRequestDTO.getPassword()));
            User user = userRepository.findByUsername(authenticationRequestDTO.getUsername()).orElseThrow();
            String jwtToken = jWtService.generateToken(user);
            return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
//            return ResponseEntity.status(HttpStatus.OK).body(loginResponseDTO);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user credentials");
        }
    }


    public ResponseEntity<?> registerEmployee(RegisterRequestDTO registerRequestDTO) {
        try {
            Optional<User> employee = userRepository.findByUsername(registerRequestDTO.getUsername());
            if (employee.isPresent()) { // USER ALREADY EXISTS
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The user already exists");
            } else {    // CREATE EMPLOYEE
                String encodedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());
                User newEmployee = new User();
                newEmployee.setUsername(registerRequestDTO.getUsername());
                newEmployee.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
                newEmployee.setRole(Role.EMPLOYEE);
                userRepository.save(newEmployee);
                String jwtToken = jWtService.generateToken(newEmployee);
                return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during registration");
        }
    }


}
