package com.messismo.bar.DTOsTests;

import com.messismo.bar.DTOs.AuthenticationResponseDTO;
import com.messismo.bar.Entities.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AuthenticationResponseDTOTests {

    @Test
    public void testAuthenticationResponseDTOGettersAndSetters() {

        AuthenticationResponseDTO authenticationResponseDTO1 = new AuthenticationResponseDTO();
        authenticationResponseDTO1.setAccessToken("AccessToken1");
        authenticationResponseDTO1.setRefreshToken("RefreshToken1");
        authenticationResponseDTO1.setEmail("example@example.com");
        authenticationResponseDTO1.setRole(Role.EMPLOYEE);
        AuthenticationResponseDTO authenticationResponseDTO2 = new AuthenticationResponseDTO();
        authenticationResponseDTO2.setAccessToken("AccessToken1");
        authenticationResponseDTO2.setRefreshToken("RefreshToken1");
        authenticationResponseDTO2.setEmail("example@example.com");
        authenticationResponseDTO2.setRole(Role.EMPLOYEE);
        AuthenticationResponseDTO authenticationResponseDTO3 = new AuthenticationResponseDTO();
        authenticationResponseDTO3.setAccessToken("AccessToken3");
        authenticationResponseDTO3.setRefreshToken("RefreshToken3");
        authenticationResponseDTO3.setEmail("example@example.com.ar");
        authenticationResponseDTO3.setRole(Role.ADMIN);

        Assertions.assertEquals(authenticationResponseDTO1.getAccessToken(), authenticationResponseDTO2.getAccessToken());
        Assertions.assertEquals(authenticationResponseDTO1.getRefreshToken(), authenticationResponseDTO2.getRefreshToken());
        Assertions.assertEquals(authenticationResponseDTO1.getEmail(), authenticationResponseDTO2.getEmail());
        Assertions.assertEquals(authenticationResponseDTO1.getRole(), authenticationResponseDTO2.getRole());
        assertNotEquals(authenticationResponseDTO1.getAccessToken(), authenticationResponseDTO3.getAccessToken());
        assertNotEquals(authenticationResponseDTO1.getRefreshToken(), authenticationResponseDTO3.getRefreshToken());
        assertNotEquals(authenticationResponseDTO1.getEmail(), authenticationResponseDTO3.getEmail());
        assertNotEquals(authenticationResponseDTO1.getRole(), authenticationResponseDTO3.getRole());
    }

    @Test
    public void testAuthenticationResponseDTOEquals() {

        AuthenticationResponseDTO authenticationResponseDTO1 = new AuthenticationResponseDTO("AccessToken1", "RegreshToken1", "example@example.com", Role.ADMIN);
        AuthenticationResponseDTO authenticationResponseDTO2 = new AuthenticationResponseDTO("AccessToken1", "RegreshToken1", "example@example.com", Role.ADMIN);
        AuthenticationResponseDTO authenticationResponseDTO3 = new AuthenticationResponseDTO("AccessToken1", "RegreshToken1", "example@example.com", Role.EMPLOYEE);

        Assertions.assertEquals(authenticationResponseDTO1, authenticationResponseDTO2);
        assertNotEquals(authenticationResponseDTO1, authenticationResponseDTO3);
    }

    @Test
    public void testAuthenticationResponseDTOHashCode() {

        AuthenticationResponseDTO authenticationResponseDTO1 = new AuthenticationResponseDTO("AccessToken1", "RefreshToken1", "example@example.com", Role.ADMIN);
        AuthenticationResponseDTO authenticationResponseDTO2 = new AuthenticationResponseDTO("AccessToken1", "RefreshToken1", "example@example.com", Role.ADMIN);
        AuthenticationResponseDTO authenticationResponseDTO3 = new AuthenticationResponseDTO("AccessToken1", "RefreshToken1", "example@example.com", Role.EMPLOYEE);

        Assertions.assertEquals(authenticationResponseDTO1.hashCode(), authenticationResponseDTO2.hashCode());
        assertNotEquals(authenticationResponseDTO1.hashCode(), authenticationResponseDTO3.hashCode());
    }


    @Test
    public void testAuthenticationResponseDTOWithBuilder() {
        AuthenticationResponseDTO responseDTO = AuthenticationResponseDTO.builder().accessToken("accessToken123").refreshToken("refreshToken456").email("example@example.com").role(Role.EMPLOYEE).build();

        assertEquals("accessToken123", responseDTO.getAccessToken());
        assertEquals("refreshToken456", responseDTO.getRefreshToken());
        assertEquals("example@example.com", responseDTO.getEmail());
        assertEquals(Role.EMPLOYEE, responseDTO.getRole());
    }

    @Test
    public void testAuthenticationResponseDTOWithBuilder_WithNoAccessToken() {
        AuthenticationResponseDTO responseDTO = AuthenticationResponseDTO.builder().refreshToken("refreshToken456").email("example@example.com").role(Role.EMPLOYEE).build();

        Assertions.assertNull(responseDTO.getAccessToken());
        assertEquals("refreshToken456", responseDTO.getRefreshToken());
        assertEquals("example@example.com", responseDTO.getEmail());
        assertEquals(Role.EMPLOYEE, responseDTO.getRole());
    }

    @Test
    public void testAuthenticationResponseDTOWithBuilder_WithNoRefreshToken() {
        AuthenticationResponseDTO responseDTO = AuthenticationResponseDTO.builder().accessToken("accessToken123").email("example@example.com").role(Role.EMPLOYEE).build();

        assertEquals("accessToken123", responseDTO.getAccessToken());
        assertNull(responseDTO.getRefreshToken());
        assertEquals("example@example.com", responseDTO.getEmail());
        assertEquals(Role.EMPLOYEE, responseDTO.getRole());
    }

    @Test
    public void testAuthenticationResponseDTOWithBuilder_WithNoEmail() {
        AuthenticationResponseDTO responseDTO = AuthenticationResponseDTO.builder().accessToken("accessToken123").refreshToken("refreshToken456").role(Role.EMPLOYEE).build();

        assertEquals("accessToken123", responseDTO.getAccessToken());
        assertEquals("refreshToken456", responseDTO.getRefreshToken());
        assertNull(responseDTO.getEmail());
        assertEquals(Role.EMPLOYEE, responseDTO.getRole());
    }

    @Test
    public void testAuthenticationResponseDTOWithBuilder_WithNoRole() {
        AuthenticationResponseDTO responseDTO = AuthenticationResponseDTO.builder().accessToken("accessToken123").refreshToken("refreshToken456").email("example@example.com").build();

        assertEquals("accessToken123", responseDTO.getAccessToken());
        assertEquals("refreshToken456", responseDTO.getRefreshToken());
        assertEquals("example@example.com", responseDTO.getEmail());
        assertNull(responseDTO.getRole());
    }

    @Test
    public void testAuthenticationResponseDTOWithEmptyBuilder() {
        AuthenticationResponseDTO responseDTO = AuthenticationResponseDTO.builder().build();

        assertNull(responseDTO.getAccessToken());
        assertNull(responseDTO.getRefreshToken());
        assertNull(responseDTO.getEmail());
        assertNull(responseDTO.getRole());
    }
}
