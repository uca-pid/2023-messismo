package com.messismo.bar.EntitiesTests;

import com.messismo.bar.Entities.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.Assert.assertTrue;

public class RoleTests {
    @Test
    public void testGetAuthorities() {
        Role role = Role.ADMIN;
        var authorities = role.getAuthorities();

        Assertions.assertEquals(17, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
