package com.messismo.bar.Entities;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete"),
    VALIDATEDEMPLOYEE_READ("validatedemployee:read"),
    VALIDATEDEMPLOYEE_UPDATE("validatedemployee:update"),
    VALIDATEDEMPLOYEE_CREATE("validatedemployee:create"),
    VALIDATEDEMPLOYEE_DELETE("validatedemployee:delete"),
    EMPLOYEE_READ("employee:read"),
    EMPLOYEE_UPDATE("employee:update"),
    EMPLOYEE_CREATE("employee:create"),
    EMPLOYEE_DELETE("employee:delete")
    ;

    private final String permission;
}