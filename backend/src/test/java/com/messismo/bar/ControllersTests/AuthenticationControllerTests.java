package com.messismo.bar.ControllersTests;

import com.messismo.bar.Controllers.AuthenticationController;
import com.messismo.bar.DTOs.*;
import com.messismo.bar.Entities.Role;
import com.messismo.bar.Exceptions.*;
import com.messismo.bar.Services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthenticationControllerTests {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PasswordRecoveryService passwordRecoveryService;




    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testRegister_Success() throws Exception {

        RegisterRequestDTO request = new RegisterRequestDTO("test@example.com", "email", "password");
        AuthenticationResponseDTO mockResponse = new AuthenticationResponseDTO("mockJwtToken", "mockRefreshToken", "mockEmail", Role.EMPLOYEE);
        when(authenticationService.register(any(RegisterRequestDTO.class))).thenReturn(mockResponse);
        ResponseEntity<?> response = authenticationController.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());

    }

    @Test
    public void testRegister_ConflictMissingData() {

        RegisterRequestDTO request = new RegisterRequestDTO(null, null, null);
        ResponseEntity<?> response = authenticationController.register(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing data for user registration", response.getBody());

    }

    @Test
    public void testRegister_ConflictUserAlreadyExistsException() throws Exception {

        RegisterRequestDTO request = new RegisterRequestDTO("existing@example.com", "password", "username");
        when(authenticationService.register(any(RegisterRequestDTO.class))).thenThrow(new UserAlreadyExistsException("User already exists"));
        ResponseEntity<?> response = authenticationController.register(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody());

    }

    @Test
    public void testAuthenticateSuccess() throws Exception {

        AuthenticationRequestDTO mockRequest = new AuthenticationRequestDTO("test@example.com", "testPassword");
        AuthenticationResponseDTO mockResponse = new AuthenticationResponseDTO("mockJwtToken", "mockRefreshToken", "test@example.com", Role.EMPLOYEE);
        when(authenticationService.authenticate(any(AuthenticationRequestDTO.class))).thenReturn(mockResponse);
        ResponseEntity<?> response = authenticationController.authenticate(mockRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), mockResponse);

    }

    @Test
    public void testAuthenticateFailure() throws Exception {

        AuthenticationRequestDTO mockRequest = new AuthenticationRequestDTO("test@example.com", "testPassword");
        when(authenticationService.authenticate(any(AuthenticationRequestDTO.class))).thenThrow(new Exception("Invalid user credentials"));
        ResponseEntity<?> response = authenticationController.authenticate(mockRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid user credentials", response.getBody());

    }

    @Test
    public void testAuthenticateMissingData() {

        AuthenticationRequestDTO mockRequest = new AuthenticationRequestDTO(null, "testPassword");
        ResponseEntity<?> response = authenticationController.authenticate(mockRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof String);
        assertEquals("Missing data for user login", response.getBody());

    }

    @Test
    public void testForgotPasswordSuccess() throws Exception {

        String mockEmail = "test@example.com";
        when(passwordRecoveryService.forgotPassword(any(String.class))).thenReturn("Email sent!");
        ResponseEntity<String> response = authenticationController.forgotPassword(mockEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email sent!", response.getBody());

    }

    @Test
    public void testForgotPasswordUserNotFound() throws Exception {

        String mockEmail = "nonexistent@example.com";
        when(passwordRecoveryService.forgotPassword(any(String.class))).thenThrow(new UserNotFoundException("No user has that email"));
        ResponseEntity<String> response = authenticationController.forgotPassword(mockEmail);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("No user has that email", response.getBody());

    }

    @Test
    public void testForgotPasswordInternalServerError() throws Exception {

        String mockEmail = "test@example.com";
        when(passwordRecoveryService.forgotPassword(any(String.class))).thenThrow(new Exception("Internal error"));
        ResponseEntity<String> response = authenticationController.forgotPassword(mockEmail);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal error", response.getBody());

    }

    @Test
    public void testChangeForgottenPasswordSuccess() throws Exception {

        PasswordRecoveryDTO mockDTO = new PasswordRecoveryDTO("test@example.com", "1234", "newPassword");
        when(passwordRecoveryService.changeForgottenPassword(any(PasswordRecoveryDTO.class))).thenReturn("Password changed successfully");
        ResponseEntity<String> response = authenticationController.changeForgottenPassword(mockDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully", response.getBody());

    }

    @Test
    public void testChangeForgottenPasswordUserNotFound() throws Exception {

        PasswordRecoveryDTO mockDTO = new PasswordRecoveryDTO("nonexistent@example.com", "1234", "newPassword");
        when(passwordRecoveryService.changeForgottenPassword(any(PasswordRecoveryDTO.class))).thenThrow(new UserNotFoundException("No user has that email"));
        ResponseEntity<String> response = authenticationController.changeForgottenPassword(mockDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("No user has that email", response.getBody());

    }

    @Test
    public void testChangeForgottenPasswordNoPinCreated() throws Exception {

        PasswordRecoveryDTO mockDTO = new PasswordRecoveryDTO("test@example.com", "1234", "newPassword");
        when(passwordRecoveryService.changeForgottenPassword(any(PasswordRecoveryDTO.class))).thenThrow(new NoPinCreatedForUserException("The user has no PINs created"));
        ResponseEntity<String> response = authenticationController.changeForgottenPassword(mockDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("The user has no PINs created", response.getBody());

    }

    @Test
    public void testChangeForgottenPasswordPinExpired() throws Exception {

        PasswordRecoveryDTO mockDTO = new PasswordRecoveryDTO("test@example.com", "1234", "newPassword");
        when(passwordRecoveryService.changeForgottenPassword(any(PasswordRecoveryDTO.class))).thenThrow(new PinExpiredException("PIN expired!"));
        ResponseEntity<String> response = authenticationController.changeForgottenPassword(mockDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("PIN expired!", response.getBody());

    }

    @Test
    public void testChangeForgottenPasswordInternalServerError() throws Exception {

        PasswordRecoveryDTO mockDTO = new PasswordRecoveryDTO("test@example.com", "1234", "newPassword");
        when(passwordRecoveryService.changeForgottenPassword(any(PasswordRecoveryDTO.class))).thenThrow(new Exception("Internal error"));
        ResponseEntity<String> response = authenticationController.changeForgottenPassword(mockDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal error", response.getBody());

    }

    @Test
    public void testHealth() {
        ResponseEntity<String> response = authenticationController.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Server is up!", response.getBody());
    }

}
