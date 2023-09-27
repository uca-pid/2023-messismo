package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.AuthenticationRequestDTO;
import com.messismo.bar.DTOs.RegisterRequestDTO;
import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.Token;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.TokenRepository;
import com.messismo.bar.Repositories.UserRepository;
import com.messismo.bar.Services.AuthenticationService;
import com.messismo.bar.Services.JwtService;
import org.junit.jupiter.api.Assertions;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
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
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenRepository tokenRepository;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        User newEmployee = new User();
        newEmployee.setUsername("martincito");
        newEmployee.setPassword(passwordEncoder.encode("password"));
        newEmployee.setRole(Role.EMPLOYEE);
        when(userRepository.findByEmail("ramon@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("ramon2@gmail.com")).thenReturn(Optional.ofNullable(newEmployee));
        when(userRepository.findByUsername("martincito")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("martincito2")).thenReturn(Optional.ofNullable(newEmployee));
        when(jwtService.generateToken(newEmployee)).thenReturn("SomeJWT");
    }


    @Test
    public void testAuthenticationServiceRegisterEmployee() {
        RegisterRequestDTO newRegisterRequest = new RegisterRequestDTO("martincito", "ramon@gmail.com", "password");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED).body("SomeJWT");
        assertEquals(response.getStatusCode(), authenticationService.register(newRegisterRequest).getStatusCode());
    }

    @Test
    public void testAuthenticationServiceRegisterEmployee_NullUsername() {
        RegisterRequestDTO newRegisterRequest = new RegisterRequestDTO(null, "ramon@gmail.com", "password");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data for user registration");
        assertEquals(response.getStatusCode(), authenticationService.register(newRegisterRequest).getStatusCode());
    }

    @Test
    public void testAuthenticationServiceRegisterEmployee_NulEmail() {
        RegisterRequestDTO newRegisterRequest = new RegisterRequestDTO("martincito", null, "password");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data for user registration");
        assertEquals(response.getStatusCode(), authenticationService.register(newRegisterRequest).getStatusCode());
    }

    @Test
    public void testAuthenticationServiceRegisterEmployee_NullPassword() {
        RegisterRequestDTO newRegisterRequest = new RegisterRequestDTO("martincito", "ramon@gmail.com", null);

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data for user registration");
        assertEquals(response.getStatusCode(), authenticationService.register(newRegisterRequest).getStatusCode());
    }

    @Test
    public void testAuthenticationServiceRegisterEmployee_DuplicatedUsername() {
        RegisterRequestDTO newRegisterRequest = new RegisterRequestDTO("martincito2", "ramon@gmail.com", "password");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("The user already exists");
        assertEquals(response.getStatusCode(), authenticationService.register(newRegisterRequest).getStatusCode());
    }

    @Test
    public void testAuthenticationServiceRegisterEmployee_DuplicatedEmail() {
        RegisterRequestDTO newRegisterRequest = new RegisterRequestDTO("martincito", "ramon2@gmail.com", "password");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("The user already exists");
        assertEquals(response.getStatusCode(), authenticationService.register(newRegisterRequest).getStatusCode());
    }

    @Test
    public void testAuthenticationServiceLoginUser() {
        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setEmail("exampleUser");
        authenticationRequestDTO.setPassword("examplePassword");
        User user = new User();
        user.setEmail("exampleUser");
        user.setPassword("examplePassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(authenticationRequestDTO.getEmail())).thenReturn(Optional.ofNullable(user));
        when(jwtService.generateToken(user)).thenReturn("exampleToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("exampleToken");

        ResponseEntity<?> response = authenticationService.authenticate(authenticationRequestDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());


    }

    @Test
    public void testAuthenticationServiceLoginUser_InvalidCredentials() {
        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setEmail("invalidUser");
        authenticationRequestDTO.setPassword("invalidPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Invalid credentials") {
        });

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user credentials");
        assertEquals(response.getStatusCode(), authenticationService.authenticate(authenticationRequestDTO).getStatusCode());
    }

    @Test
    public void testAuthenticationServiceLoginUser_NullUsername() {
        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setEmail(null);
        authenticationRequestDTO.setPassword("invalidPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Invalid credentials") {
        });

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing data for user login");
        assertEquals(response.getStatusCode(), authenticationService.authenticate(authenticationRequestDTO).getStatusCode());
    }

    @Test
    public void testAuthenticationServiceLoginUser_NullPassword() {
        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setEmail("validUsername");
        authenticationRequestDTO.setPassword(null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Invalid credentials") {
        });

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing data for user login");
        assertEquals(response.getStatusCode(), authenticationService.authenticate(authenticationRequestDTO).getStatusCode());
    }

    @Test
    public void testRevokeAllUserTokens() {
        TokenRepository tokenRepository = mock(TokenRepository.class);
        User user = new User();
        List<Token> validTokens = new ArrayList<>();
        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(validTokens);
        authenticationService.revokeAllUserTokens(user);
        for (Token token : validTokens) {
            Assertions.assertTrue(token.isRevoked());
            Assertions.assertTrue(token.isExpired());
        }
    }
}

