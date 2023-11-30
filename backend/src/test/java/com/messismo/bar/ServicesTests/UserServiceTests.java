package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.UserDTO;
import com.messismo.bar.DTOs.UserIdDTO;
import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Exceptions.CannotUpgradeToManager;
import com.messismo.bar.Exceptions.CannotUpgradeToValidatedEmployee;
import com.messismo.bar.Repositories.UserRepository;
import com.messismo.bar.Services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        User user1 = new User(1L, "admin", "admin@mail.com", "password1", Role.ADMIN);
        User user2 = new User(2L, "messi2", "messi2@gmail.com", "password123", Role.EMPLOYEE);
        User user3 = new User(3L, "messi3", "messi3@gmail.com", "password123", Role.EMPLOYEE);
        User user4 = new User(4L, "messi4", "messi4@gmail.com", "password123", Role.VALIDATEDEMPLOYEE);
        User user5 = new User(5L, "messi4", "messi4@gmail.com", "password123", Role.EMPLOYEE);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        when(userRepository.findByEmail("admin@mail.com")).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("noExistente")).thenReturn(Optional.empty());
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.findById(4L)).thenReturn(Optional.of(user4));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user5));
        when(userRepository.findById(100L)).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(users);
    }

    @Test
    public void testUserServiceLoadUserByUsername_UserFound() {

        UserDetails result = userService.loadUserByUsername("admin@mail.com");

        Assertions.assertEquals("password1", result.getPassword());
        verify(userRepository, times(1)).findByEmail("admin@mail.com");

    }

    @Test
    public void testUserServiceLoadUserByUsername_UserNotFound() {

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("noExistente");
        });
        verify(userRepository, times(1)).findByEmail("noExistente");

    }

    @Test
    public void testUserServiceLoadUserByUsername_NullUser() {

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(null);
        });
        verify(userRepository, times(1)).findByEmail(null);

    }

    @Test
    public void testUserServiceGetAllEmployees() {

        UserDTO user1 = UserDTO.builder().id(1L).email("admin@mail.com").username("admin").role(Role.ADMIN).build();
        UserDTO user2 = UserDTO.builder().id(2L).email("messi2@gmail.com").username("messi2").role(Role.EMPLOYEE).build();
        UserDTO user3 = UserDTO.builder().id(3L).email("messi3@gmail.com").username("messi3").role(Role.EMPLOYEE).build();
        List<UserDTO> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        Assertions.assertEquals(users, userService.getAllEmployees());
        verify(userRepository, times(1)).findAll();

    }

    @Test
    public void testUserServiceValidateEmployee() throws Exception {

        UserIdDTO userIdDTO = new UserIdDTO(2L);

        Assertions.assertEquals("User IS NOW a VALIDATED_EMPLOYEE", userService.validateEmployee(userIdDTO));
    }

    @Test
    public void testUserServiceValidateEmployee_NotFound() {

        UserIdDTO userIdDTO = new UserIdDTO(100L);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.validateEmployee(userIdDTO);
        });
        Assertions.assertEquals("User DOES NOT exist", exception.getMessage());

    }


    @Test
    public void testUserServiceValidateEmployee_AlreadyAValidatedEmployee() {

        UserIdDTO userIdDTO = new UserIdDTO(4L);

        CannotUpgradeToValidatedEmployee exception = assertThrows(CannotUpgradeToValidatedEmployee.class, () -> {
            userService.validateEmployee(userIdDTO);
        });
        Assertions.assertEquals("User IS already a VALIDATED_EMPLOYEE OR SUPERIOR", exception.getMessage());

    }

    @Test
    public void testUserServiceValidateEmployee_Exception() {

        UserIdDTO userIdDTO = new UserIdDTO(2L);
        doThrow(new RuntimeException("Runtime Exception")).when(userRepository).save(any());

        Exception exception = assertThrows(Exception.class, () -> {
            userService.validateEmployee(userIdDTO);
        });
        Assertions.assertEquals("Cannot upgrade to validated employee", exception.getMessage());

    }

    @Test
    public void testUserServiceValidateManager() throws Exception {

        Assertions.assertEquals("User IS NOW a MANAGER", userService.validateManager(new UserIdDTO(4L)));

    }

    @Test
    public void testUserServiceValidateManager_NotFound() throws Exception {

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.validateManager(new UserIdDTO(100L));
        });
        Assertions.assertEquals("User DOES NOT exist", exception.getMessage());

    }


    @Test
    public void testUserServiceValidateManager_IsNotAValidatedEmployee() {

        CannotUpgradeToManager exception = assertThrows(CannotUpgradeToManager.class, () -> {
            userService.validateManager(new UserIdDTO(5L));
        });
        Assertions.assertEquals("User MUST be first a VALIDATED_EMPLOYEE", exception.getMessage());
    }

    @Test
    public void testUserServiceValidateManager_Exception() {

        UserIdDTO userIdDTO = new UserIdDTO(4L);
        doThrow(new RuntimeException("Runtime Exception")).when(userRepository).save(any());

        Exception exception = assertThrows(Exception.class, () -> {
            userService.validateManager(userIdDTO);
        });
        Assertions.assertEquals("Cannot upgrade to manager", exception.getMessage());

    }
}


