package com.messismo.bar.Entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.messismo.bar.Entities.Permission.*;

@RequiredArgsConstructor
public enum Role {

    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE,
                    VALIDATEDEMPLOYEE_READ,
                    VALIDATEDEMPLOYEE_UPDATE,
                    VALIDATEDEMPLOYEE_DELETE,
                    VALIDATEDEMPLOYEE_CREATE,
                    EMPLOYEE_READ,
                    EMPLOYEE_UPDATE,
                    EMPLOYEE_DELETE,
                    EMPLOYEE_CREATE
            )
    ),
    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE,
                    VALIDATEDEMPLOYEE_READ,
                    VALIDATEDEMPLOYEE_UPDATE,
                    VALIDATEDEMPLOYEE_DELETE,
                    VALIDATEDEMPLOYEE_CREATE,
                    EMPLOYEE_READ,
                    EMPLOYEE_UPDATE,
                    EMPLOYEE_DELETE,
                    EMPLOYEE_CREATE
            )
    ),
    VALIDATEDEMPLOYEE(
            Set.of(
                    VALIDATEDEMPLOYEE_READ,
                    VALIDATEDEMPLOYEE_UPDATE,
                    VALIDATEDEMPLOYEE_DELETE,
                    VALIDATEDEMPLOYEE_CREATE,
                    EMPLOYEE_READ,
                    EMPLOYEE_UPDATE,
                    EMPLOYEE_DELETE,
                    EMPLOYEE_CREATE
            )
    ),
    EMPLOYEE(
            Set.of(
                    EMPLOYEE_READ,
                    EMPLOYEE_UPDATE,
                    EMPLOYEE_DELETE,
                    EMPLOYEE_CREATE
            )
    )

    ;

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}