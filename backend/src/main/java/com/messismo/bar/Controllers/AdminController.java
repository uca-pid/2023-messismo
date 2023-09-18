package com.messismo.bar.Controllers;

import com.messismo.bar.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin("*")
public class AdminController {

    private final UserService userService;

    @PutMapping("/validateAdmin/{employeeId}")
    public ResponseEntity<?> validateEmployee(@PathVariable Long employeeId) {
        return userService.validateAdmin(employeeId);
    }
}
