package com.messismo.bar.Controllers;

import com.messismo.bar.DTOs.*;
import com.messismo.bar.Exceptions.*;
import com.messismo.bar.Services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class ManagerController {

    private final ProductService productService;

    private final UserService userService;

    private final CategoryService categoryService;

    private final DashboardService dashboardService;

    private final GoalService goalService;

    @PutMapping("/product/updatePrice")
    public ResponseEntity<String> updateProductPrice(@RequestBody ProductPriceDTO productPriceDTO) {
        if (productPriceDTO.getUnitPrice() == null || productPriceDTO.getProductId() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Missing data to modify product price");
        }
        if (productPriceDTO.getUnitPrice() <= 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product price CANNOT be less than 0.");
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.modifyProductPrice(productPriceDTO));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/product/updateCost")
    public ResponseEntity<String> updateProductCost(@RequestBody ProductPriceDTO productPriceDTO) {
        if (productPriceDTO.getUnitPrice() == null || productPriceDTO.getProductId() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Missing data to modify product cost");
        }
        if (productPriceDTO.getUnitPrice() <= 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product cost CANNOT be less than 0.");
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.modifyProductCost(productPriceDTO));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/product/modifyProductStock")
    public ResponseEntity<String> modifyProductStock(@RequestBody ProductStockDTO productStockDTO) {
        if (productStockDTO.getModifyStock() == null || productStockDTO.getProductId() == null || productStockDTO.getOperation() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data to add product stock");
        }
        if (productStockDTO.getModifyStock() < 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Stock quantity cannot be less than 0");
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.modifyProductStock(productStockDTO));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @DeleteMapping("/product/deleteProduct/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.deleteProduct(productId));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getAllEmployees")
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllEmployees());
    }

    @PutMapping("/validateEmployee")
    public ResponseEntity<String> validateEmployee(@RequestBody UserIdDTO userIdDTO) {
        if (userIdDTO.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing userId to upgrade to validated employee");
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.validateEmployee(userIdDTO));
        } catch (UsernameNotFoundException | CannotUpgradeToValidatedEmployee e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/category/addCategory")
    public ResponseEntity<String> addCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        if (categoryRequestDTO.getCategoryName() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a category");
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(categoryRequestDTO));
        } catch (ExistingCategoryFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category name already exists");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Category NOT created");
        }
    }

    @DeleteMapping("/category/deleteCategory")
    public ResponseEntity<String> deleteCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        if (categoryRequestDTO.getCategoryName() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to delete a category");
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.deleteCategory(categoryRequestDTO));
        } catch (CategoryNotFoundException | CategoryHasAtLeastOneProductAssociated e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Category NOT deleted");
        }
    }

    @PostMapping("/dashboard/getDashboard")
    public ResponseEntity<?> getDashboardInformation(@RequestBody DashboardRequestDTO dashboardRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getDashboardInformation(dashboardRequestDTO));
        } catch (InvalidDashboardRequestedDate e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/goals/addGoal")
    public ResponseEntity<String> addGoal(@RequestBody GoalDTO goalDTO) {
        if (Objects.equals(goalDTO.getName(), "") || goalDTO.getName() == null || goalDTO.getStartingDate() == null || goalDTO.getEndingDate() == null || Objects.equals(goalDTO.getObjectType(), "") || goalDTO.getObjectType() == null || goalDTO.getGoalObjective() <= 0.00 || goalDTO.getGoalObjective() == null || ((goalDTO.getObjectType().equals("Product") || goalDTO.getObjectType().equals("Category")) && (Objects.equals(goalDTO.getGoalObject(), "") || goalDTO.getGoalObject() == null))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a goal");
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(goalService.addGoal(goalDTO));
        } catch (ProvidedDatesMustNotCollideWithOtherDatesException | ProductNotFoundException | CategoryNotFoundException | EndingDateMustBeAfterStartingDateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/goals/deleteGoal")
    public ResponseEntity<String> deleteGoal(@RequestBody GoalDeleteDTO goalDeleteDTO) {
        if (goalDeleteDTO.getGoalId() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to delete a goal");
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(goalService.deleteGoal(goalDeleteDTO));
        } catch (GoalIdNotFoundException | GoalInProcessCannotBeDeletedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/goals/modifyGoal")
    public ResponseEntity<String> modifyGoal(@RequestBody GoalModifyDTO goalModifyDTO) {
        if (goalModifyDTO.getGoalId() == null || goalModifyDTO.getNewGoalObjective() <= 0.00) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to modify a goal");
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(goalService.modifyGoal(goalModifyDTO));
        } catch (ProvidedDatesMustNotCollideWithOtherDatesException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/goals/getGoals")
    public ResponseEntity<?> getGoals(@RequestBody GoalFilterRequestDTO goalFilterRequestDTO) {
        if (goalFilterRequestDTO.getAchieved() == null || goalFilterRequestDTO.getStatus() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Lists must not be null");
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(goalService.getGoals(goalFilterRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
