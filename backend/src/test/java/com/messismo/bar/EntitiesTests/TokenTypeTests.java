package com.messismo.bar.EntitiesTests;

import com.messismo.bar.Entities.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TokenTypeTests {

    @Test
    public void testTokenType() {
        TokenType[] tokenTypes = TokenType.values();

        assertEquals("BEARER", TokenType.BEARER.name());
        assertEquals(TokenType.BEARER, TokenType.valueOf("BEARER"));
        assertTrue(tokenTypes.length > 0);
        assertEquals(TokenType.BEARER, tokenTypes[0]);
    }
}
