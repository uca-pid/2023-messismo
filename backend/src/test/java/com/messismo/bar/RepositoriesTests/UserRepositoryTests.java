package com.messismo.bar.RepositoriesTests;

import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserRepositoryFindByUsername() {

        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@gmail.com");
        userRepository.save(user1);
        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@gmail.com");
        userRepository.save(user2);
        User user3 = new User();
        user3.setUsername("user3");
        user3.setEmail("user3@gmail.com");
        userRepository.save(user3);
        Optional<User> userByUsername1 = userRepository.findByUsername("user1");
        Optional<User> userByUsername2 = userRepository.findByUsername("user2");
        Optional<User> userByUsername3 = userRepository.findByUsername("user3");
        Optional<User> userByUsernameRandom = userRepository.findByUsername("userRandom");

        assertTrue(userByUsername1.isPresent());
        assertEquals("user1", userByUsername1.get().getNoFunctionalityUsername());
        assertEquals(1L, userByUsername1.get().getId());
        assertTrue(userByUsername2.isPresent());
        assertEquals("user2", userByUsername2.get().getNoFunctionalityUsername());
        assertEquals(2L, userByUsername2.get().getId());
        assertTrue(userByUsername3.isPresent());
        assertEquals("user3", userByUsername3.get().getNoFunctionalityUsername());
        assertEquals(3L, userByUsername3.get().getId());
        assertTrue(userByUsernameRandom.isEmpty());
    }

    @Test
    public void testUserRepositoryFindByUsername_NotFound() {

        assertThrows(NoSuchElementException.class, () -> {
            userRepository.findByUsername("user").orElseThrow(() -> new NoSuchElementException("User not found"));
        });
    }

    @Test
    public void testUserRepositoryFindByUsername_NullNotFound() {

        assertThrows(NoSuchElementException.class, () -> {
            userRepository.findByUsername(null).orElseThrow(() -> new NoSuchElementException("Null User not found"));
        });
    }
}
