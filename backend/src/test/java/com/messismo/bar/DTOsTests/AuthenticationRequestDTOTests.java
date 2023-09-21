package com.messismo.bar.DTOsTests;

import com.messismo.bar.DTOs.AuthenticationRequestDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationRequestDTOTests {

    @Test
    public void testAuthenticationRequestDTOGettersAndSetters() {

        AuthenticationRequestDTO authenticationRequestDTO1 = new AuthenticationRequestDTO();
        authenticationRequestDTO1.setEmail("example@example.com");
        authenticationRequestDTO1.setPassword("password1");
        AuthenticationRequestDTO authenticationRequestDTO2 = new AuthenticationRequestDTO();
        authenticationRequestDTO2.setEmail("example@example.com");
        authenticationRequestDTO2.setPassword("password1");
        AuthenticationRequestDTO authenticationRequestDTO3 = new AuthenticationRequestDTO();
        authenticationRequestDTO3.setEmail("example@example.com.ar");
        authenticationRequestDTO3.setPassword("password3");

        assertEquals(authenticationRequestDTO1.getEmail(), authenticationRequestDTO2.getEmail());
        assertEquals(authenticationRequestDTO1.getPassword(), authenticationRequestDTO2.getPassword());
        assertNotEquals(authenticationRequestDTO1.getEmail(), authenticationRequestDTO3.getEmail());
        assertNotEquals(authenticationRequestDTO1.getPassword(), authenticationRequestDTO3.getPassword());
    }

    @Test
    public void testAuthenticationRequestDTOEquals() {

        AuthenticationRequestDTO authenticationRequestDTO1 = new AuthenticationRequestDTO("example@example.com", "password1");
        AuthenticationRequestDTO authenticationRequestDTO2 = new AuthenticationRequestDTO("example@example.com", "password1");
        AuthenticationRequestDTO authenticationRequestDTO3 = new AuthenticationRequestDTO("example@example.com.ar", "password3");

        assertEquals(authenticationRequestDTO1, authenticationRequestDTO2);
        assertNotEquals(authenticationRequestDTO1, authenticationRequestDTO3);
    }

    @Test
    public void testAuthenticationRequestDTOHashCode() {

        AuthenticationRequestDTO authenticationRequestDTO1 = new AuthenticationRequestDTO("example@example.com", "password1");
        AuthenticationRequestDTO authenticationRequestDTO2 = new AuthenticationRequestDTO("example@example.com", "password1");
        AuthenticationRequestDTO authenticationRequestDTO3 = new AuthenticationRequestDTO("example1@example.com", "password3");

        assertEquals(authenticationRequestDTO1.hashCode(), authenticationRequestDTO2.hashCode());
        assertNotEquals(authenticationRequestDTO1.hashCode(), authenticationRequestDTO3.hashCode());
    }

    @Test
    public void testAuthenticationRequestDTOBuilder() {
        AuthenticationRequestDTO requestDTO = AuthenticationRequestDTO.builder().email("example@example.com").password("password123").build();

        assertEquals("example@example.com", requestDTO.getEmail());
        assertEquals("password123", requestDTO.getPassword());
    }

    @Test
    public void testAuthenticationRequestDTOBuilderWithNoEmail() {
        AuthenticationRequestDTO requestDTO = AuthenticationRequestDTO.builder().password("password123").build();

        assertNull(requestDTO.getEmail());
        assertEquals("password123", requestDTO.getPassword());
    }

    @Test
    public void testAuthenticationRequestDTOBuilderWithNoPassword() {
        AuthenticationRequestDTO requestDTO = AuthenticationRequestDTO.builder().email("example@example.com").build();

        assertEquals("example@example.com", requestDTO.getEmail());
        assertNull(requestDTO.getPassword());
    }

    @Test
    public void testAuthenticationRequestDTOBuilderWithNoEmailNorPassword() {
        AuthenticationRequestDTO requestDTO = AuthenticationRequestDTO.builder().build();

        assertNull(requestDTO.getEmail());
        assertNull(requestDTO.getPassword());
    }

}
