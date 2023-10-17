package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.PasswordRecoveryDTO;
import com.messismo.bar.Entities.PasswordRecovery;
import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.PasswordRecoveryRepository;
import com.messismo.bar.Repositories.UserRepository;
import com.messismo.bar.Services.PasswordRecoveryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PasswordRecoveryServiceTests {

    @InjectMocks
    private PasswordRecoveryService passwordRecoveryService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordRecoveryRepository passwordRecoveryRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPasswordRecoveryServiceGenerateRandomPin() {

        String pin = PasswordRecoveryService.generateRandomPin();

        Assertions.assertEquals(6, pin.length());
        Assertions.assertFalse(pin.isEmpty());
        Assertions.assertTrue(pin.matches("\\d+"));
    }

    @Test
    public void testPasswordRecoveryServiceForgotPassword() {

        String email = "user@example.com";
        User user = User.builder().id(1L).username("user").email("user@example.com").password("Password1")
                .role(Role.EMPLOYEE).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordRecoveryRepository.findByUser(user)).thenReturn(Optional.empty());
        ResponseEntity<String> response = passwordRecoveryService.forgotPassword(email);

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordRecoveryRepository, times(1)).findByUser(user);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Email sent!", response.getBody());
    }

    @Test
    public void testPasswordRecoveryServiceForgotPassword_WithNoExistentEmail() {

        String email = "user@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        ResponseEntity<String> response = passwordRecoveryService.forgotPassword(email);

        verify(userRepository, times(1)).findByEmail(email);
        verify(javaMailSender, times(0)).send(any(SimpleMailMessage.class));
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("CANNOT recover the password at the moment"), response);
    }

    @Test
    public void testChangeForgottenPassword_Success() {

        PasswordRecoveryDTO passwordRecoveryDTO = new PasswordRecoveryDTO();
        passwordRecoveryDTO.setEmail("user@example.com");
        passwordRecoveryDTO.setPin("123456");
        passwordRecoveryDTO.setNewPassword("newPassword");
        User user = new User();
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setDateCreated(new Date());
        passwordRecovery.setPin("123456");

        when(userRepository.findByEmail(passwordRecoveryDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordRecoveryRepository.findByUser(user)).thenReturn(Optional.of(passwordRecovery));
        when(passwordEncoder.encode(passwordRecoveryDTO.getNewPassword())).thenReturn("encodedPassword");
        ResponseEntity<String> response = passwordRecoveryService.changeForgottenPassword(passwordRecoveryDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully", response.getBody());
    }

    @Test
    public void testChangeForgottenPassword_PinExpired() {

        PasswordRecoveryDTO passwordRecoveryDTO = new PasswordRecoveryDTO();
        passwordRecoveryDTO.setEmail("user@example.com");
        passwordRecoveryDTO.setPin("123456");
        passwordRecoveryDTO.setNewPassword("newPassword");
        User user = new User();
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -2);
        passwordRecovery.setDateCreated(calendar.getTime());
        passwordRecovery.setPin("123456");

        when(userRepository.findByEmail(passwordRecoveryDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordRecoveryRepository.findByUser(user)).thenReturn(Optional.of(passwordRecovery));
        ResponseEntity<String> response = passwordRecoveryService.changeForgottenPassword(passwordRecoveryDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("PIN expired!", response.getBody());
    }

    @Test
    public void testChangeForgottenPassword_WithWrongEmail() {

        PasswordRecoveryDTO passwordRecoveryDTO = new PasswordRecoveryDTO();
        passwordRecoveryDTO.setEmail("user@example.com");
        passwordRecoveryDTO.setPin("123456");
        passwordRecoveryDTO.setNewPassword("newPassword");
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -2);
        passwordRecovery.setDateCreated(calendar.getTime());
        passwordRecovery.setPin("123456");

        when(userRepository.findByEmail(passwordRecoveryDTO.getEmail())).thenReturn(Optional.empty());
        ResponseEntity<String> response = passwordRecoveryService.changeForgottenPassword(passwordRecoveryDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("CANNOT change the new password at the moment", response.getBody());
    }

}
