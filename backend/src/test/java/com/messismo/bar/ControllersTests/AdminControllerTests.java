package com.messismo.bar.ControllersTests;

import com.messismo.bar.Controllers.AdminController;
import com.messismo.bar.DTOs.UserIdDTO;
import com.messismo.bar.Exceptions.CannotUpgradeToManager;
import com.messismo.bar.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AdminControllerTests {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testValidateAdmin_Success() throws Exception {

        UserIdDTO userIdDTO = new UserIdDTO(1L);
        when(userService.validateEmployee(any(UserIdDTO.class))).thenReturn("Validation successful");
        ResponseEntity<?> response = adminController.validateAdmin(userIdDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Validation successful", response.getBody());

    }

    @Test
    public void testValidateAdmin_ConflictMissingUserId() {

        UserIdDTO userIdDTO = new UserIdDTO(null);
        ResponseEntity<?> response = adminController.validateAdmin(userIdDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing userId to upgrade to manager", response.getBody());

    }

    @Test
    public void testValidateAdmin_ConflictUsernameNotFoundException() throws Exception {
        UserIdDTO userIdDTO = new UserIdDTO(1L);
        when(userService.validateEmployee(any(UserIdDTO.class))).thenThrow(new UsernameNotFoundException("User not found"));
        ResponseEntity<?> response = adminController.validateAdmin(userIdDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User not found", response.getBody());

    }

    @Test
    public void testValidateAdmin_ConflictCannotUpgradeToManagerException() throws Exception {

        UserIdDTO userIdDTO = new UserIdDTO(1L);
        when(userService.validateEmployee(any(UserIdDTO.class))).thenThrow(new CannotUpgradeToManager("Cannot upgrade to manager"));
        ResponseEntity<?> response = adminController.validateAdmin(userIdDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Cannot upgrade to manager", response.getBody());

    }

    @Test
    public void testValidateAdmin_InternalServerError() throws Exception {

        UserIdDTO userIdDTO = new UserIdDTO(1L);
        when(userService.validateEmployee(any(UserIdDTO.class))).thenThrow(new Exception("Some internal error"));
        ResponseEntity<?> response = adminController.validateAdmin(userIdDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Some internal error", response.getBody());

    }
}
