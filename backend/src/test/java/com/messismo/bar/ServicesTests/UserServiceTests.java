package com.messismo.bar.ServicesTests;

import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.UserRepository;
import com.messismo.bar.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UserSerivceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

//    @BeforeEach
//    public void setUp() {
//        // Inicializar los mocks y el servicio
//        MockitoAnnotations.initMocks(this);
//
//        // Configurar el comportamiento de los mocks según sea necesario
//        // Por ejemplo, cuando se llama a myRepository.findBySomeValue(1L), devolver un resultado específico
//        when(myRepository.findBySomeValue(1L)).thenReturn("MockedValue");
//    }

    @Test
    public void testUserServiceLoadUserByUsername_NotFound() {

        UserDetails result = userService.loadUserByUsername("hola");
        System.out.println(result);
        // Verificar el resultado esperado
        assertEquals("ExpectedValue", result);

        // Verificar que se llamó al método del repositorio según sea necesario
        verify(userRepository, times(1)).findByUsername("hola");
    }
}

