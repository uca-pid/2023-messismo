package com.messismo.bar.Controllers;

import com.messismo.bar.DTOs.ProductPriceDTO;
import com.messismo.bar.Services.ProductService;
import com.messismo.bar.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin("*")
public class AdminController {

    private final ProductService productService;

    private final UserService userService;

    @PutMapping("/product/updatePrice/{productId}")
    public ResponseEntity<?> updateProductPrice(@PathVariable Long productId, @RequestBody ProductPriceDTO body) {
        return productService.modifyProductPrice(productId, body.getPrice());
    }

    @DeleteMapping("/product/deleteProduct/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }

    @GetMapping("/getAllEmployees")
    public ResponseEntity<?> getAllEmployees() {
        return userService.getAllEmployees();
    }

    @PutMapping("/employee/validateEmployee/{userId}")
    public ResponseEntity<?> validateEmployee(@PathVariable Long userId) {
        return userService.validateEmployee(userId);
    }


}
