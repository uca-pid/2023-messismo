package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.AuthenticationRequestDTO;
import com.messismo.bar.DTOs.RegisterRequestDTO;
import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.UserRepository;
import com.messismo.bar.Services.AuthenticationService;
import com.messismo.bar.Services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTests {
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jWtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        User newEmployee = new User();
        newEmployee.setUsername("martincito");
        newEmployee.setPassword(passwordEncoder.encode("password"));
        newEmployee.setRole(Role.EMPLOYEE);
        when(userRepository.findByEmail("ramon@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("ramon2@gmail.com")).thenReturn(Optional.ofNullable(newEmployee));
        when(userRepository.findByUsername("martincito")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("martincito2")).thenReturn(Optional.ofNullable(newEmployee));
        when(jWtService.generateToken(newEmployee)).thenReturn("SomeJWT");
    }


    @Test
    public void testAuthenticationServiceRegisterEmployee() {
        RegisterRequestDTO newRegisterRequest = new RegisterRequestDTO("martincito", "ramon@gmail.com", "password");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED).body("SomeJWT");
        assertEquals(response, authenticationService.registerEmployee(newRegisterRequest));
    }

    @Test
    public void testAuthenticationServiceRegisterEmployee_NullUsername() {
        RegisterRequestDTO newRegisterRequest = new RegisterRequestDTO(null, "ramon@gmail.com", "password");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data for user registration");
        assertEquals(response, authenticationService.registerEmployee(newRegisterRequest));
    }

    @Test
    public void testAuthenticationServiceRegisterEmployee_NulEmail() {
        RegisterRequestDTO newRegisterRequest = new RegisterRequestDTO("martincito", null, "password");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data for user registration");
        assertEquals(response, authenticationService.registerEmployee(newRegisterRequest));
    }

    @Test
    public void testAuthenticationServiceRegisterEmployee_NullPassword() {
        RegisterRequestDTO newRegisterRequest = new RegisterRequestDTO("martincito", "ramon@gmail.com", null);

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data for user registration");
        assertEquals(response, authenticationService.registerEmployee(newRegisterRequest));
    }

    @Test
    public void testAuthenticationServiceRegisterEmployee_DuplicatedUsername() {
        RegisterRequestDTO newRegisterRequest = new RegisterRequestDTO("martincito2", "ramon@gmail.com", "password");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("The user already exists");
        assertEquals(response, authenticationService.registerEmployee(newRegisterRequest));
    }

    @Test
    public void testAuthenticationServiceRegisterEmployee_DuplicatedEmail() {
        RegisterRequestDTO newRegisterRequest = new RegisterRequestDTO("martincito", "ramon2@gmail.com", "password");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("The user already exists");
        assertEquals(response, authenticationService.registerEmployee(newRegisterRequest));
    }

    @Test
    public void testAuthenticationServiceLoginUser() {
        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setUsername("exampleUser");
        authenticationRequestDTO.setPassword("examplePassword");
        User user = new User();
        user.setUsername("exampleUser");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userRepository.findByUsername(authenticationRequestDTO.getUsername())).thenReturn(Optional.of(user));
        when(jWtService.generateToken(user)).thenReturn("exampleToken");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).body("exampleToken");
        assertEquals(response, authenticationService.loginUser(authenticationRequestDTO));
    }

    @Test
    public void testAuthenticationServiceLoginUser_InvalidCredentials() {
        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setUsername("invalidUser");
        authenticationRequestDTO.setPassword("invalidPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Invalid credentials") {});

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user credentials");
        assertEquals(response, authenticationService.loginUser(authenticationRequestDTO));
    }
    @Test
    public void testAuthenticationServiceLoginUser_NullUsername() {
        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setUsername(null);
        authenticationRequestDTO.setPassword("invalidPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Invalid credentials") {});

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing data for user login");
        assertEquals(response, authenticationService.loginUser(authenticationRequestDTO));
    }

    @Test
    public void testAuthenticationServiceLoginUser_NullPassword() {
        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setUsername("validUsername");
        authenticationRequestDTO.setPassword(null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Invalid credentials") {});

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing data for user login");
        assertEquals(response, authenticationService.loginUser(authenticationRequestDTO));
    }
}

