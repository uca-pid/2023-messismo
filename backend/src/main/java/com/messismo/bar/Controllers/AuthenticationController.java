package com.messismo.bar.Controllers;

import com.messismo.bar.DTOs.AuthenticationRequestDTO;
import com.messismo.bar.DTOs.RegisterRequestDTO;
import com.messismo.bar.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/registerEmployee")
    public ResponseEntity<?> registerEmployee(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return authenticationService.registerEmployee(registerRequestDTO);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> loginUser(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        return authenticationService.loginUser(authenticationRequestDTO);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.status(HttpStatus.OK).body("TEST");
    }
}
