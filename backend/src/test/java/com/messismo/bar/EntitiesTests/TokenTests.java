package com.messismo.bar.EntitiesTests;

import com.messismo.bar.Entities.Token;
import com.messismo.bar.Entities.TokenType;
import com.messismo.bar.Entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class TokenTests {

    @Test
    public void testTokenCreation() {
        Token testToken = Token.builder().id(1L).token("testToken").tokenType(TokenType.BEARER).revoked(false).expired(false).user(new User()).build();
        Long id = 1L;
        assertEquals(id, testToken.getId());
        assertEquals("testToken", testToken.getToken());
        assertEquals(TokenType.BEARER, testToken.getTokenType());
        assertFalse(testToken.isRevoked());
        assertFalse(testToken.isExpired());
        assertNotNull(testToken.getUser());
    }
}
