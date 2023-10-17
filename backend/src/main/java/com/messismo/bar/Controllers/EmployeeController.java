package com.messismo.bar.Controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE', 'VALIDATEDEMPLOYEE')")
@CrossOrigin("*")
public class EmployeeController {
}
