package com.messismo.bar.Controllers;

import com.messismo.bar.DTOs.UserIdDTO;
import com.messismo.bar.Exceptions.CannotUpgradeToManager;
import com.messismo.bar.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    @PutMapping("/validateAdmin")
    public ResponseEntity<?> validateAdmin(@RequestBody UserIdDTO userIdDTO) {
        if (userIdDTO.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing userId to upgrade to manager");
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.validateEmployee(userIdDTO));
        } catch (UsernameNotFoundException | CannotUpgradeToManager e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
