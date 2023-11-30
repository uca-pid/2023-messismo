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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
public class JwtServiceTests {

    private JwtService jwtService;
    private Key signingKey;

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();
        byte[] keyBytes = Decoders.BASE64.decode("ee2ecef84938596217bd811c807a4eb225d510874e922e5e2e692d8a6e4464bf");
        signingKey = Keys.hmacShaKeyFor(keyBytes);

    }

    @Test
    public void testExtractUsername() {

        String token = generateSampleToken("testUser");
        String username = jwtService.extractUsername(token);

        Assertions.assertEquals("testUser", username);

    }

    @Test
    public void testGenerateToken() {

        User user1 = new User(1L, "admin", "admin@mail.com", "password1", Role.ADMIN);
        String token = jwtService.generateToken(user1);

        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isEmpty());

    }

    @Test
    public void testGenerateTokenWithExtraClaims() {

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("key1", "value1");
        extraClaims.put("key2", 123);
        User user1 = new User(1L, "admin", "admin@mail.com", "password1", Role.ADMIN);
        String token = jwtService.generateToken(extraClaims, user1);

        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isEmpty());

    }

    @Test
    public void testExtractClaim() {

        String token = generateSampleToken("testUser");
        String subject = jwtService.extractClaim(token, Claims::getSubject);

        Assertions.assertEquals("testUser", subject);

    }

    @Test
    public void testIsTokenValid() {

        String token = generateSampleToken("testUser");
        User user1 = new User(1L, "admin", "admin@mail.com", "password1", Role.ADMIN);
        boolean isValid = jwtService.isTokenValid(token, user1);

        Assertions.assertFalse(isValid);

    }

    @Test
    public void testIsTokenValid_ExpiredToken() {

        String token = generateSampleTokenWithExpiration("testUser", new Date(System.currentTimeMillis() - 360000000));
        User user1 = new User(1L, "admin", "admin@mail.com", "password1", Role.ADMIN);

        assertThrows(ExpiredJwtException.class, () -> {
            jwtService.isTokenValid(token, user1);
        });

    }

    @Test
    public void testGenerateAndVerifyToken() {

        User user1 = new User(1L, "admin", "admin@mail.com", "password1", Role.ADMIN);
        String token = jwtService.generateToken(user1);
        boolean isValid = jwtService.isTokenValid(token, user1);

        Assertions.assertTrue(isValid);

    }

    private String generateSampleToken(String username) {
        return Jwts.builder().setSubject(username).signWith(signingKey, SignatureAlgorithm.HS256).compact();
    }

    private String generateSampleTokenWithExpiration(String username, Date expiration) {
        return Jwts.builder().setSubject(username).setExpiration(expiration).signWith(signingKey, SignatureAlgorithm.HS256).compact();
    }

        @Test
        public void testGenerateRefreshToken() {

            User user1 = new User(1L, "admin", "admin@mail.com", "password1", Role.ADMIN);
            String refreshToken = jwtService.generateRefreshToken(user1);

            Assertions.assertNotNull(refreshToken);
            Assertions.assertFalse(refreshToken.isEmpty());

    }

}
