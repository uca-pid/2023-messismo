package com.messismo.bar.Controllers;

import com.messismo.bar.DTOs.ProductPriceDTO;
import com.messismo.bar.DTOs.UserIdDTO;
import com.messismo.bar.Services.ProductService;
import com.messismo.bar.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class ManagerController {

    private final ProductService productService;

    private final UserService userService;

    @PutMapping("/product/updatePrice")
    public ResponseEntity<?> updateProductPrice(@RequestBody ProductPriceDTO body) {
        return productService.modifyProductPrice(body);
    }

    @DeleteMapping("/product/deleteProduct/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }

    @GetMapping("/getAllEmployees")
    public ResponseEntity<?> getAllEmployees() {
        return userService.getAllEmployees();
    }

//    @PutMapping("/validateEmployee/{userId}")
//    public ResponseEntity<?> validateEmployee(@PathVariable Long userId) {
//        return userService.validateEmployee(userId);
//    }

    @PutMapping("/validateEmployee")
    public ResponseEntity<?> validateEmployee(@RequestBody UserIdDTO body) {
        return userService.validateEmployee(body.getUserId());
    }
}
