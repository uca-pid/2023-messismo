package com.messismo.bar.Controllers;

import com.messismo.bar.DTOs.*;
import com.messismo.bar.Exceptions.*;
import com.messismo.bar.Services.CategoryService;
import com.messismo.bar.Services.OrderService;
import com.messismo.bar.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/validatedEmployee")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'VALIDATEDEMPLOYEE')")
public class ValidatedEmployeeController {

    private final ProductService productService;

    private final CategoryService categoryService;

    private final OrderService orderService;

    @PostMapping("/product/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO) {
        if (productDTO.getCategory() == null || productDTO.getName() == null || productDTO.getName().isEmpty() || productDTO.getUnitPrice() == null || productDTO.getDescription() == null || productDTO.getStock() == null || productDTO.getUnitCost() == null || productDTO.getNewCategory() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a product");
        }
        if (productDTO.getUnitCost() <= 0 || productDTO.getStock() < 0 || productDTO.getUnitPrice() < 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Some values cannot be less than zero. Please check");
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(productDTO));
        } catch (CategoryNotFoundException | ExistingProductFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @PostMapping("/filterProducts")
    public ResponseEntity<?> filterProducts(@RequestBody FilterProductDTO filterProductDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.filterProducts(filterProductDTO));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories());
    }

    @PostMapping("/addNewOrder")
    public ResponseEntity<?> addNewOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addNewOrder(orderRequestDTO));
        } catch (UserNotFoundException | ProductQuantityBelowAvailableStock e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/closeOrder")
    public ResponseEntity<?> closeOrder(@RequestBody OrderIdDTO orderIdDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.closeOrder(orderIdDTO));
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/modifyOrder")
    public ResponseEntity<?> modifyOrder(@RequestBody ModifyOrderDTO modifyOrderDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.modifyOrder(modifyOrderDTO));
        } catch (ProductQuantityBelowAvailableStock | OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("orders/getAllOrders")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }

}
