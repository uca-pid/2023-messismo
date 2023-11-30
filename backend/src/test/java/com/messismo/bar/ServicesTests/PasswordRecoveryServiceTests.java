package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.CategoryRequestDTO;
import com.messismo.bar.DTOs.PasswordRecoveryDTO;
import com.messismo.bar.Entities.Category;
import com.messismo.bar.Entities.PasswordRecovery;
import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Exceptions.NoPinCreatedForUserException;
import com.messismo.bar.Exceptions.PinExpiredException;
import com.messismo.bar.Exceptions.UserNotFoundException;
import com.messismo.bar.Repositories.PasswordRecoveryRepository;
import com.messismo.bar.Repositories.UserRepository;
import com.messismo.bar.Services.PasswordRecoveryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
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
    public void testPasswordRecoveryServiceForgotPassword() throws Exception {

        String email = "user@example.com";
        User user = User.builder().id(1L).username("user").email("user@example.com").password("Password1").role(Role.EMPLOYEE).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordRecoveryRepository.findByUser(user)).thenReturn(Optional.empty());
        String response = passwordRecoveryService.forgotPassword(email);

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordRecoveryRepository, times(1)).findByUser(user);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        Assertions.assertEquals("Email sent!", response);

    }
    @Test
    public void testPasswordRecoveryServiceForgotPassword_Exception() {

        String email = "user@example.com";
        User user = User.builder().id(1L).username("user").email("user@example.com").password("Password1").role(Role.EMPLOYEE).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordRecoveryRepository.findByUser(user)).thenReturn(Optional.empty());

        doThrow(new DataIntegrityViolationException("Error saving")).when(passwordRecoveryRepository).save(any(PasswordRecovery.class));
        Exception exception = assertThrows(Exception.class, () -> {
            passwordRecoveryService.forgotPassword(email);
        });
        Assertions.assertEquals("CANNOT recover the password at the moment", exception.getMessage());

    }

    @Test
    public void testPasswordRecoveryServiceForgotPassword_WithNoExistentEmail() {

        String email = "user@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            passwordRecoveryService.forgotPassword(email);
        });
        Assertions.assertEquals("No user has that email", exception.getMessage());

    }

    @Test
    public void testPasswordRecoveryServiceChangeForgottenPassword_Success() throws Exception {

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
        String response = passwordRecoveryService.changeForgottenPassword(passwordRecoveryDTO);
        Assertions.assertEquals("Password changed successfully", response);

    }

    @Test
    public void testPasswordRecoveryServiceChangeForgottenPassword_PinExpired() {

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
        PinExpiredException exception = assertThrows(PinExpiredException.class, () -> {
            passwordRecoveryService.changeForgottenPassword(passwordRecoveryDTO);
        });
        Assertions.assertEquals("PIN expired!", exception.getMessage());

    }

    @Test
    public void testPasswordRecoveryServiceChangeForgottenPassword_WithWrongEmail() {

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
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            passwordRecoveryService.changeForgottenPassword(passwordRecoveryDTO);
        });
        Assertions.assertEquals("No user has that email", exception.getMessage());

    }

    @Test
    public void testPasswordRecoveryServiceChangeForgottenPassword_Exception() {

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
        doThrow(new RuntimeException("Runtime Exception")).when(userRepository).save(any());
        Exception exception = assertThrows(Exception.class, () -> {
            passwordRecoveryService.changeForgottenPassword(passwordRecoveryDTO);
        });
        Assertions.assertEquals("CANNOT change the new password at the moment", exception.getMessage());

    }

    @Test
    public void testChangeForgottenPassword_NoPinCreatedForUser() throws Exception {

        PasswordRecoveryDTO passwordRecoveryDTO = new PasswordRecoveryDTO();
        passwordRecoveryDTO.setEmail("test@example.com");
        passwordRecoveryDTO.setPin("123456");
        passwordRecoveryDTO.setNewPassword("newPassword");
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordRecoveryRepository.findByUser(user)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoPinCreatedForUserException.class, () -> {
            passwordRecoveryService.changeForgottenPassword(passwordRecoveryDTO);
        });
        Assertions.assertEquals("The user has no PINs created", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(passwordRecoveryRepository, times(1)).findByUser(user);
        verify(userRepository, never()).save(any());
        verify(passwordRecoveryRepository, never()).delete(any());

    }
}
