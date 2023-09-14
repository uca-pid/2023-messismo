package com.messismo.bar;

import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UsersTests {

    @Test
    public void testUserGettersAndSetters() {

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("messi");
        user1.setPassword("password123");
        user1.setRole(Role.EMPLOYEE);
        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("messi");
        user2.setPassword("password123");
        user2.setRole(Role.EMPLOYEE);
        User user3 = new User();
        user3.setId(2L);
        user3.setUsername("user3");
        user3.setPassword("password456");
        user3.setRole(Role.ADMIN);

        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getUsername(), user2.getUsername());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getRole(), user2.getRole());
        assertNotEquals(user1.getId(), user3.getId());
        assertNotEquals(user1.getUsername(), user3.getUsername());
        assertNotEquals(user1.getPassword(), user3.getPassword());
        assertNotEquals(user1.getRole(), user3.getRole());
    }

    @Test
    public void testUserEquals() {

        User user1 = new User(1L, "messi", "password123", Role.EMPLOYEE);
        User user2 = new User(1L, "messi", "password123", Role.EMPLOYEE);
        User user3 = new User(2L, "messi", "password456", Role.ADMIN);

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    public void testUserHashCode() {
        User user1 = new User(1L, "messi", "password123", Role.EMPLOYEE);
        User user2 = new User(1L, "messi", "password123", Role.EMPLOYEE);

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testUserGetAuthorities() {
        User user1 = new User(1L,"username", "password", Role.EMPLOYEE);
        Collection<? extends GrantedAuthority> authorities = user1.getAuthorities();
        List<GrantedAuthority> expectedAuthorities = List.of(new SimpleGrantedAuthority("EMPLOYEE"));

        assertEquals(expectedAuthorities, authorities);
    }

}
