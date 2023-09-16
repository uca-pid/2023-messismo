package com.messismo.bar.DTOsTests;

import com.messismo.bar.DTOs.RegisterRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RegisterRequestDTOTests {
    @Test
    public void testRegisterRequestDTOGettersAndSetters() {

        RegisterRequestDTO registerRequestDTO1 = new RegisterRequestDTO();
        registerRequestDTO1.setUsername("messi");
        registerRequestDTO1.setEmail("messi@gmail.com");
        registerRequestDTO1.setPassword("password123");
        RegisterRequestDTO registerRequestDTO2 = new RegisterRequestDTO();
        registerRequestDTO2.setUsername("messi");
        registerRequestDTO2.setEmail("messi@gmail.com");
        registerRequestDTO2.setPassword("password123");
        RegisterRequestDTO registerRequestDTO3 = new RegisterRequestDTO();
        registerRequestDTO3.setUsername("user3");
        registerRequestDTO3.setUsername("user3@gmail.com");
        registerRequestDTO3.setPassword("password456");

        assertEquals(registerRequestDTO1.getUsername(), registerRequestDTO2.getUsername());
        assertEquals(registerRequestDTO1.getEmail(), registerRequestDTO2.getEmail());
        assertEquals(registerRequestDTO1.getPassword(), registerRequestDTO2.getPassword());
        assertNotEquals(registerRequestDTO1.getUsername(), registerRequestDTO3.getUsername());
        assertNotEquals(registerRequestDTO1.getEmail(), registerRequestDTO3.getEmail());
        assertNotEquals(registerRequestDTO1.getPassword(), registerRequestDTO3.getPassword());

    }

    @Test
    public void testRegisterRequestDTOEquals() {

        RegisterRequestDTO registerRequestDTO1 = new RegisterRequestDTO("messi","messi@gmail.com","password123");
        RegisterRequestDTO registerRequestDTO2 = new RegisterRequestDTO("messi","messi@gmail.com","password123");
        RegisterRequestDTO registerRequestDTO3 = new RegisterRequestDTO("user3","user3@gmail.com","password456");

        assertEquals(registerRequestDTO1, registerRequestDTO2);
        assertNotEquals(registerRequestDTO1, registerRequestDTO3);
    }

    @Test
    public void testRegisterRequestDTOHashCode() {

        RegisterRequestDTO registerRequestDTO1 = new RegisterRequestDTO("messi","messi@gmail.com","password123");
        RegisterRequestDTO registerRequestDTO2 = new RegisterRequestDTO("messi","messi@gmail.com","password123");
        RegisterRequestDTO registerRequestDTO3 = new RegisterRequestDTO("user3","user3@gmail.com","password456");

        assertEquals(registerRequestDTO1.hashCode(), registerRequestDTO2.hashCode());
        assertNotEquals(registerRequestDTO1.hashCode(), registerRequestDTO3.hashCode());
    }

}
