package com.messismo.bar.DTOsTests;

import com.messismo.bar.DTOs.AuthenticationRequestDTO;
import com.messismo.bar.DTOs.ProductDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AuthenticationRequestDTOTests {

    @Test
    public void testAuthenticationRequestDTOGettersAndSetters() {

        AuthenticationRequestDTO authenticationRequestDTO1 = new AuthenticationRequestDTO();
        authenticationRequestDTO1.setUsername("username1");
        authenticationRequestDTO1.setPassword("password1");
        AuthenticationRequestDTO authenticationRequestDTO2 = new AuthenticationRequestDTO();
        authenticationRequestDTO2.setUsername("username1");
        authenticationRequestDTO2.setPassword("password1");
        AuthenticationRequestDTO authenticationRequestDTO3 = new AuthenticationRequestDTO();
        authenticationRequestDTO3.setUsername("username3");
        authenticationRequestDTO3.setPassword("password3");

        assertEquals(authenticationRequestDTO1.getUsername(), authenticationRequestDTO2.getUsername());
        assertEquals(authenticationRequestDTO1.getPassword(), authenticationRequestDTO2.getPassword());
        assertNotEquals(authenticationRequestDTO1.getUsername(), authenticationRequestDTO3.getUsername());
        assertNotEquals(authenticationRequestDTO1.getPassword(), authenticationRequestDTO3.getPassword());
    }

    @Test
    public void testAuthenticationRequestDTOEquals() {

        AuthenticationRequestDTO authenticationRequestDTO1 = new AuthenticationRequestDTO("username1","password1");
        AuthenticationRequestDTO authenticationRequestDTO2 = new AuthenticationRequestDTO("username1","password1");
        AuthenticationRequestDTO authenticationRequestDTO3 = new AuthenticationRequestDTO("username3","password3");

        assertEquals(authenticationRequestDTO1, authenticationRequestDTO2);
        assertNotEquals(authenticationRequestDTO1, authenticationRequestDTO3);
    }

    @Test
    public void testAuthenticationRequestDTOHashCode() {

        AuthenticationRequestDTO authenticationRequestDTO1 = new AuthenticationRequestDTO("username1","password1");
        AuthenticationRequestDTO authenticationRequestDTO2 = new AuthenticationRequestDTO("username1","password1");
        AuthenticationRequestDTO authenticationRequestDTO3 = new AuthenticationRequestDTO("username3","password3");

        assertEquals(authenticationRequestDTO1.hashCode(), authenticationRequestDTO2.hashCode());
        assertNotEquals(authenticationRequestDTO1.hashCode(), authenticationRequestDTO3.hashCode());
    }


}
