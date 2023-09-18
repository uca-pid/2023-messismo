package com.messismo.bar.ServicesTests;

import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.UserRepository;
import com.messismo.bar.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        User user1 = new User(1L, "admin", "hola", "password1", Role.ADMIN,new ArrayList<>());
        User user2 = new User(2L, "messi2", "messi2@gmail.com", "password123", Role.EMPLOYEE,new ArrayList<>());
        User user3 = new User(3L, "messi3", "messi3@gmail.com", "password123", Role.EMPLOYEE,new ArrayList<>());
        User user4 = new User(4L, "messi4", "messi4@gmail.com", "password123", Role.VALIDATEDEMPLOYEE,new ArrayList<>());
        User user5 = new User(5L, "messi4", "messi4@gmail.com", "password123", Role.MANAGER,new ArrayList<>());
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsername("noExistente")).thenReturn(Optional.empty());
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(user2));
        when(userRepository.findById(4L)).thenReturn(Optional.ofNullable(user4));
        when(userRepository.findById(5L)).thenReturn(Optional.ofNullable(user5));
        when(userRepository.findById(100L)).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(users);
    }

    @Test
    public void testUserServiceLoadUserByUsername_UserFound() {

        UserDetails result = userService.loadUserByUsername("admin");

        assertEquals("password1", result.getPassword());
        verify(userRepository, times(1)).findByUsername("admin");
    }

    @Test
    public void testUserServiceLoadUserByUsername_UserNotFound() {

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("noExistente");
        });
        verify(userRepository, times(1)).findByUsername("noExistente");
    }

    @Test
    public void testUserServiceLoadUserByUsername_NullUser() {

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(null);
        });
        verify(userRepository, times(1)).findByUsername(null);
    }

    @Test
    public void testUserServiceGetAllEmployees() {
        User user1 = new User(1L, "admin", "hola", "password1", Role.ADMIN,new ArrayList<>());
        User user2 = new User(2L, "messi2", "messi2@gmail.com", "password123", Role.EMPLOYEE,new ArrayList<>());
        User user3 = new User(3L, "messi3", "messi3@gmail.com", "password123", Role.EMPLOYEE,new ArrayList<>());
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        ResponseEntity<List<User>> response = ResponseEntity.status(HttpStatus.OK).body(users);

        assertEquals(response, userService.getAllEmployees());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testUserServiceValidateEmployee() {

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).body("User IS NOW a VALIDATED_EMPLOYEE");

        assertEquals(response, userService.validateEmployee(2L));
    }

    @Test
    public void testUserServiceValidateEmployee_NotFound() {

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User DOES NOT exist");

        assertEquals(response, userService.validateEmployee(100L));
    }

    @Test
    public void testUserServiceValidateEmployee_NullNotFound() {

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User DOES NOT exist");

        assertEquals(response, userService.validateEmployee(null));
    }

    @Test
    public void testUserServiceValidateEmployee_AlreadyAValidatedEmployee() {

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User IS already a VALIDATED_EMPLOYEE");

        assertEquals(response, userService.validateEmployee(4L));
    }

    @Test
    public void testUserServiceValidateAdmin() {

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).body("User IS NOW a MANAGER");

        assertEquals(response, userService.validateAdmin(4L));
    }

    @Test
    public void testUserServiceValidateAdmin_NotFound() {

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User DOES NOT exist");

        assertEquals(response, userService.validateAdmin(100L));
    }

    @Test
    public void testUserServiceValidateAdmin_NullNotFound() {

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User DOES NOT exist");

        assertEquals(response, userService.validateAdmin(null));
    }

    @Test
    public void testUserServiceValidateAdmin_AlreadyAValidatedAdmin() {

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User MUST be first a VALIDATED_EMPLOYEE");

        assertEquals(response, userService.validateAdmin(5L));
    }
}

