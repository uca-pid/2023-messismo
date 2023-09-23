package com.messismo.bar.Controllers;

import com.messismo.bar.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE', 'VALIDATEDEMPLOYEE')")
public class EmployeeController {

    private final ProductService productServiceService;

    @GetMapping("/getAllProducts")
    public ResponseEntity<?> getAllProducts() {
        return productServiceService.getAllProducts();
    }

}
