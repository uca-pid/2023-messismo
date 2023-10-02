package com.messismo.bar.Controllers;

import com.messismo.bar.DTOs.UserIdDTO;
import com.messismo.bar.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminController {

    private final UserService userService;


   @PutMapping("/validateAdmin")
    public ResponseEntity<?> validateEmployee(@RequestBody UserIdDTO body) {
        return userService.validateAdmin(body.getUserId());
    }
}
