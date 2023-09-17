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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jWtService;

    private final AuthenticationManager authenticationManager;

//    public Object register(RegisterRequestDTO registerRequestDTO) {
//        if(registerRequestDTO.getEmail() == null ||registerRequestDTO.getPassword() == null ||registerRequestDTO.getUsername() == null ){
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data for user registration");
//        }
//        Optional<User> foundByUsername = userRepository.findByUsername(registerRequestDTO.getUsername());
//        Optional<User> foundByEmail = userRepository.findByEmail(registerRequestDTO.getEmail());
//        if(foundByUsername.isPresent() || foundByEmail.isPresent()){
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or email already in use");
//        }
//        else {
//            User newUser = new User();
//            newUser.setUsername(registerRequestDTO.getUsername());
//            newUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
//            newUser.setRole(Role.EMPLOYEE);
//            userRepository.save(newUser);
//            String jwtToken = jWtService.generateToken(newUser);
//            return AuthenticationResponse.builder().token(jwtToken).build();
//        }
//    }

//    public AuthenticationResponse authenticate(AuthenticationRequestDTO authenticationRequestDTO) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getUsername(), authenticationRequestDTO.getPassword()));
//        User user = userRepository.findByUsername(authenticationRequestDTO.getUsername()).orElseThrow();
//        String jwtToken = jWtService.generateToken(user);
//        return AuthenticationResponse.builder().token(jwtToken).build();
//    }

    public ResponseEntity<?> loginUser(AuthenticationRequestDTO authenticationRequestDTO) {
        try {
            if(authenticationRequestDTO.getUsername()==null || authenticationRequestDTO.getPassword()==null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing data for user login");
            }
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getUsername(), authenticationRequestDTO.getPassword()));
            User user = userRepository.findByUsername(authenticationRequestDTO.getUsername()).orElseThrow();
            String jwtToken = jWtService.generateToken(user);
            return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user credentials");
        }
    }


    public ResponseEntity<?> registerEmployee(RegisterRequestDTO registerRequestDTO) {

        try {
            if(registerRequestDTO.getEmail() == null ||registerRequestDTO.getPassword() == null ||registerRequestDTO.getUsername() == null ){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data for user registration");
            }
            Optional<User> employeeByUsername = userRepository.findByUsername(registerRequestDTO.getUsername());
            Optional<User> employeeByMail = userRepository.findByEmail(registerRequestDTO.getEmail());
            if (employeeByUsername.isPresent() || employeeByMail.isPresent()) { // USER ALREADY EXISTS
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The user already exists");
            } else {    // CREATE EMPLOYEE
                User newEmployee = new User();
                newEmployee.setUsername(registerRequestDTO.getUsername());
                newEmployee.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
                newEmployee.setEmail(registerRequestDTO.getEmail());
                newEmployee.setRole(Role.EMPLOYEE);
                userRepository.save(newEmployee);
                String jwtToken = jWtService.generateToken(newEmployee);
                return ResponseEntity.status(HttpStatus.CREATED).body(jwtToken);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during registration");
        }
    }


}
