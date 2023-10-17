package com.messismo.bar.Controllers;

import com.messismo.bar.DTOs.AuthenticationRequestDTO;
import com.messismo.bar.DTOs.DashboardRequestDTO;
import com.messismo.bar.DTOs.PasswordRecoveryDTO;
import com.messismo.bar.DTOs.RegisterRequestDTO;
import com.messismo.bar.Services.AuthenticationService;
import com.messismo.bar.Services.DashboardService;
import com.messismo.bar.Services.PasswordRecoveryService;
import lombok.RequiredArgsConstructor;
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
        return authenticationService.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {
        return authenticationService.authenticate(request);
    }

    // @PostMapping("/refresh-token")
    // public void refreshToken(HttpServletRequest request, HttpServletResponse
    // response) throws IOException {
    // authenticationService.refreshToken(request, response);
    // }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        return passwordRecoveryService.forgotPassword(email);
    }

    @PostMapping("/changeForgottenPassword")
    public ResponseEntity<String> changeForgottenPassword(@RequestBody PasswordRecoveryDTO passwordRecoveryDTO) {
        return passwordRecoveryService.changeForgottenPassword(passwordRecoveryDTO);
    }


}