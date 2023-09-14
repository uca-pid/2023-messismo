package com.messismo.bar.Controllers;

import com.messismo.bar.DTOs.ProductDTO;
import com.messismo.bar.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/validatedEmployee")
@CrossOrigin("*")
public class ValidatedEmployeeControlller {

    private final ProductService productService;

    @PostMapping("/product/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }
}
