package com.messismo.bar.RepositoriesTests;

import com.messismo.bar.Entities.Token;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.TokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class TokenRepositoryTests {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindAllValidTokenByUser() {
        User user = User.builder().username("testUser").email("test@example.com").password("password").build();
        entityManager.persist(user);
        entityManager.flush();
        Token validToken1 = Token.builder().token("token1").user(user).expired(false).revoked(false).build();
        Token validToken2 = Token.builder().token("token2").user(user).expired(false).revoked(false).build();
        Token revokedToken = Token.builder().token("revokedToken").user(user).expired(true).revoked(true).build();
        entityManager.persist(validToken1);
        entityManager.persist(validToken2);
        entityManager.persist(revokedToken);
        entityManager.flush();
        List<Token> validTokens = tokenRepository.findAllValidTokenByUser(user.getId()); //EXPIRED Y REVOKED EN TRUE

        assertEquals(2, validTokens.size());
        assertEquals("token1", validTokens.get(0).getToken());
        assertEquals("token2", validTokens.get(1).getToken());
    }
}