package com.messismo.bar.EntitiesTests;

import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    @Test
    public void testUserGettersAndSetters() {

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("messi");
        user1.setEmail("messi@gmail.com");
        user1.setPassword("password123");
        user1.setRole(Role.EMPLOYEE);
        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("messi");
        user2.setEmail("messi@gmail.com");
        user2.setPassword("password123");
        user2.setRole(Role.EMPLOYEE);
        User user3 = new User();
        user3.setId(2L);
        user3.setUsername("user3");
        user3.setUsername("user3@gmail.com");
        user3.setPassword("password456");
        user3.setRole(Role.ADMIN);

        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getUsername(), user2.getUsername());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getRole(), user2.getRole());
        assertNotEquals(user1.getId(), user3.getId());
        assertNotEquals(user1.getUsername(), user3.getUsername());
        assertNotEquals(user1.getEmail(), user3.getEmail());
        assertNotEquals(user1.getPassword(), user3.getPassword());
        assertNotEquals(user1.getRole(), user3.getRole());
    }

    @Test
    public void testUserEquals() {

        User user1 = new User(1L, "messi", "messi@gmail.com", "password123", Role.EMPLOYEE,new ArrayList<>());
        User user2 = new User(1L, "messi", "messi@gmail.com", "password123", Role.EMPLOYEE,new ArrayList<>());
        User user3 = new User(2L, "messi","user3@gmail.com", "password456", Role.ADMIN,new ArrayList<>());

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    public void testUserHashCode() {
        User user1 = new User(1L, "messi", "messi@gmail.com", "password123", Role.EMPLOYEE,new ArrayList<>());
        User user2 = new User(1L, "messi", "messi@gmail.com", "password123", Role.EMPLOYEE,new ArrayList<>());

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testUserGetAuthorities() {
        User user1 = new User(1L, "username", "messi@gmail.com", "password", Role.EMPLOYEE,new ArrayList<>());
        Collection<? extends GrantedAuthority> authorities = user1.getAuthorities();
        List<GrantedAuthority> expectedAuthorities = List.of(new SimpleGrantedAuthority("EMPLOYEE"));

        assertEquals(expectedAuthorities, authorities);
    }

    @Test
    public void testUserDetailsProperties() {
        User user1 = new User(1L, "messi", "messi@gmail.com", "password123", Role.EMPLOYEE,new ArrayList<>());

        assertTrue(user1.isAccountNonExpired());
        assertTrue(user1.isAccountNonLocked());
        assertTrue(user1.isCredentialsNonExpired());
        assertTrue(user1.isEnabled());
    }

}
