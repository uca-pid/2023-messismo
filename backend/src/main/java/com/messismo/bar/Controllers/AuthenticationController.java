package com.messismo.bar.Controllers;

import com.messismo.bar.DTOs.AuthenticationRequestDTO;
import com.messismo.bar.DTOs.PasswordRecoveryDTO;
import com.messismo.bar.DTOs.RegisterRequestDTO;
import com.messismo.bar.Exceptions.NoPinCreatedForUserException;
import com.messismo.bar.Exceptions.PinExpiredException;
import com.messismo.bar.Exceptions.UserAlreadyExistsException;
import com.messismo.bar.Exceptions.UserNotFoundException;
import com.messismo.bar.Services.AuthenticationService;
import com.messismo.bar.Services.PasswordRecoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final PasswordRecoveryService passwordRecoveryService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        try {
            if (request.getEmail() == null || request.getPassword() == null || request.getUsername() == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data for user registration");
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(request));
            }
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {
        try {
            if (request.getEmail() == null || request.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing data for user login");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(request));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(passwordRecoveryService.forgotPassword(email));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/changeForgottenPassword")
    public ResponseEntity<String> changeForgottenPassword(@RequestBody PasswordRecoveryDTO passwordRecoveryDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(passwordRecoveryService.changeForgottenPassword(passwordRecoveryDTO));
        } catch (UserNotFoundException | NoPinCreatedForUserException | PinExpiredException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.status(HttpStatus.OK).body("Server is up!");
    }

    // @PostMapping("/refresh-token")
    // public void refreshToken(HttpServletRequest request, HttpServletResponse
    // response) throws IOException {
    // authenticationService.refreshToken(request, response);
    // }
}