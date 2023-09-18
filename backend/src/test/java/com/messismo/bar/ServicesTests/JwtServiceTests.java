package com.messismo.bar.ServicesTests;


import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
public class JwtServiceTests {

//    private JwtService jwtService;
//    private Key signingKey;
//
//    @BeforeEach
//    public void setUp() {
//        jwtService = new JwtService();
//        byte[] keyBytes = Decoders.BASE64.decode("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
//        signingKey = Keys.hmacShaKeyFor(keyBytes);
//
//    }
//
//    @Test
//    public void testExtractUsername() {
//        String token = generateSampleToken("testUser");
//        String username = jwtService.extractUsername(token);
//        assertEquals("testUser", username);
//    }
//
//    @Test
//    public void testGenerateToken() {
//        User userDetails = new User(1L, "testUser", "testEmail", "password", Role.EMPLOYEE,new ArrayList<>());
//        String token = jwtService.generateToken(userDetails);
//
//        assertNotNull(token);
//        assertTrue(token.length() > 0);
//    }
//
//    @Test
//    public void testGenerateTokenWithExtraClaims() {
//        Map<String, Object> extraClaims = new HashMap<>();
//        extraClaims.put("key1", "value1");
//        extraClaims.put("key2", 123);
//
//        User userDetails = new User(1L, "testUser", "testEmail", "password", Role.EMPLOYEE,new ArrayList<>());
//        String token = jwtService.generateToken(extraClaims, userDetails);
//
//        assertNotNull(token);
//        assertTrue(token.length() > 0);
//    }
//
//    @Test
//    public void testExtractClaim() {
//        String token = generateSampleToken("testUser");
//        String subject = jwtService.extractClaim(token, Claims::getSubject);
//        assertEquals("testUser", subject);
//    }
//
//    @Test
//    public void testIsTokenValid() {
//        String token = generateSampleToken("testUser");
//
//        User userDetails = new User(1L, "testUser", "testEmail", "password", Role.EMPLOYEE,new ArrayList<>());
//        boolean isValid = jwtService.isTokenValid(token, userDetails);
//        //CHECK
//        assertFalse(isValid);
//    }
//
//    @Test
//    public void testIsTokenValid_ExpiredToken() {
//        String token = generateSampleTokenWithExpiration("testUser", new Date(System.currentTimeMillis() - 360000000));
//        User userDetails = new User(1L, "testUser", "testEmail", "password", Role.EMPLOYEE,new ArrayList<>());
//
//        assertThrows(ExpiredJwtException.class, () -> {
//            jwtService.isTokenValid(token, userDetails);
//        });
//    }
//
//    @Test
//    public void testGenerateAndVerifyToken() {
//        User userDetails = new User(1L, "testUser", "testEmail", "password", Role.EMPLOYEE,new ArrayList<>());
//        String token = jwtService.generateToken(userDetails);
//        boolean isValid = jwtService.isTokenValid(token, userDetails);
//        //CHECK
//        assertFalse(isValid);
//    }
//
//    private String generateSampleToken(String username) {
//        return Jwts.builder().setSubject(username).signWith(signingKey, SignatureAlgorithm.HS256).compact();
//    }
//
//    private String generateSampleTokenWithExpiration(String username, Date expiration) {
//        return Jwts.builder().setSubject(username).setExpiration(expiration).signWith(signingKey, SignatureAlgorithm.HS256).compact();
//    }

}
