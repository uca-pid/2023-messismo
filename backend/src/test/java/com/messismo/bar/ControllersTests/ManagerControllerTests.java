package com.messismo.bar.ControllersTests;

import com.messismo.bar.Controllers.ManagerController;
import com.messismo.bar.DTOs.*;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Exceptions.*;
import com.messismo.bar.Services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ManagerControllerTests {

    @InjectMocks
    private ManagerController managerController;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private DashboardService dashboardService;

    @Mock
    private GoalService goalService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testUpdateProductPrice_Success() throws Exception {

        ProductPriceDTO validProductPriceDTO = new ProductPriceDTO(1L, 10.0);
        when(productService.modifyProductPrice(validProductPriceDTO)).thenReturn("Product price modified successfully");
        ResponseEntity<String> response = managerController.updateProductPrice(validProductPriceDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product price modified successfully", response.getBody());
        verify(productService, times(1)).modifyProductPrice(validProductPriceDTO);

    }

    @Test
    public void testUpdateProductPrice_MissingData() throws Exception {

        ProductPriceDTO invalidProductPriceDTO = new ProductPriceDTO(null, 10.0);
        ResponseEntity<String> response = managerController.updateProductPrice(invalidProductPriceDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Missing data to modify product price", response.getBody());
        verify(productService, never()).modifyProductPrice(invalidProductPriceDTO);

    }

    @Test
    public void testUpdateProductPrice_InvalidPrice() throws Exception {

        ProductPriceDTO invalidProductPriceDTO = new ProductPriceDTO(1L, -5.0);
        ResponseEntity<String> response = managerController.updateProductPrice(invalidProductPriceDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Product price CANNOT be less than 0.", response.getBody());
        verify(productService, never()).modifyProductPrice(invalidProductPriceDTO);

    }

    @Test
    public void testUpdateProductPrice_ProductNotFound() throws Exception {
        ProductPriceDTO validProductPriceDTO = new ProductPriceDTO(1L, 10.0);
        when(productService.modifyProductPrice(validProductPriceDTO)).thenThrow(new ProductNotFoundException("Product not found"));
        ResponseEntity<String> response = managerController.updateProductPrice(validProductPriceDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
        verify(productService, times(1)).modifyProductPrice(validProductPriceDTO);

    }

    @Test
    public void testUpdateProductPrice_InternalServerError() throws Exception {

        ProductPriceDTO validProductPriceDTO = new ProductPriceDTO(1L, 10.0);
        when(productService.modifyProductPrice(validProductPriceDTO)).thenThrow(new Exception("Internal error"));
        ResponseEntity<String> response = managerController.updateProductPrice(validProductPriceDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal error", response.getBody());
        verify(productService, times(1)).modifyProductPrice(validProductPriceDTO);

    }

    @Test
    public void testUpdateProductCost_Success() throws Exception {

        ProductPriceDTO validProductPriceDTO = new ProductPriceDTO(1L, 10.0);
        when(productService.modifyProductCost(validProductPriceDTO)).thenReturn("Product cost modified successfully");
        ResponseEntity<String> response = managerController.updateProductCost(validProductPriceDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product cost modified successfully", response.getBody());
        verify(productService, times(1)).modifyProductCost(validProductPriceDTO);

    }

    @Test
    public void testUpdateProductCost_MissingData() throws Exception {

        ProductPriceDTO invalidProductPriceDTO = new ProductPriceDTO(null, 10.0);
        ResponseEntity<String> response = managerController.updateProductCost(invalidProductPriceDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Missing data to modify product cost", response.getBody());
        verify(productService, never()).modifyProductCost(invalidProductPriceDTO);

    }

    @Test
    public void testUpdateProductCost_InvalidCost() throws Exception {

        ProductPriceDTO invalidProductPriceDTO = new ProductPriceDTO(1L, -5.0);
        ResponseEntity<String> response = managerController.updateProductCost(invalidProductPriceDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Product cost CANNOT be less than 0.", response.getBody());
        verify(productService, never()).modifyProductCost(invalidProductPriceDTO);

    }

    @Test
    public void testUpdateProductCost_ProductNotFound() throws Exception {

        ProductPriceDTO validProductPriceDTO = new ProductPriceDTO(1L, 10.0);
        when(productService.modifyProductCost(validProductPriceDTO)).thenThrow(new ProductNotFoundException("Product not found"));
        ResponseEntity<String> response = managerController.updateProductCost(validProductPriceDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
        verify(productService, times(1)).modifyProductCost(validProductPriceDTO);

    }

    @Test
    public void testUpdateProductCost_InternalServerError() throws Exception {

        ProductPriceDTO validProductPriceDTO = new ProductPriceDTO(1L, 10.0);
        when(productService.modifyProductCost(validProductPriceDTO)).thenThrow(new Exception("Internal error"));
        ResponseEntity<String> response = managerController.updateProductCost(validProductPriceDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal error", response.getBody());
        verify(productService, times(1)).modifyProductCost(validProductPriceDTO);

    }

    @Test
    public void testModifyProductStock_Success() throws Exception {

        ProductStockDTO validProductStockDTO = new ProductStockDTO(1L, "add", 10);
        when(productService.modifyProductStock(validProductStockDTO)).thenReturn("Product stock modified successfully");
        ResponseEntity<String> response = managerController.modifyProductStock(validProductStockDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product stock modified successfully", response.getBody());
        verify(productService, times(1)).modifyProductStock(validProductStockDTO);

    }

    @Test
    public void testModifyProductStock_MissingData() throws Exception {

        ProductStockDTO invalidProductStockDTO = new ProductStockDTO(null, "add", 10);
        ResponseEntity<String> response = managerController.modifyProductStock(invalidProductStockDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing data to add product stock", response.getBody());
        verify(productService, never()).modifyProductStock(invalidProductStockDTO);

    }

    @Test
    public void testModifyProductStock_NegativeQuantity() throws Exception {

        ProductStockDTO invalidProductStockDTO = new ProductStockDTO(1L, "add", -10);
        ResponseEntity<String> response = managerController.modifyProductStock(invalidProductStockDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Stock quantity cannot be less than 0", response.getBody());
        verify(productService, never()).modifyProductStock(invalidProductStockDTO);

    }

    @Test
    public void testModifyProductStock_ProductNotFound() throws Exception {

        ProductStockDTO validProductStockDTO = new ProductStockDTO(1L, "add", 10);
        when(productService.modifyProductStock(validProductStockDTO)).thenThrow(new ProductNotFoundException("Product not found"));
        ResponseEntity<String> response = managerController.modifyProductStock(validProductStockDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
        verify(productService, times(1)).modifyProductStock(validProductStockDTO);

    }

    @Test
    public void testModifyProductStock_InternalServerError() throws Exception {

        ProductStockDTO validProductStockDTO = new ProductStockDTO(1L, "add", 10);
        when(productService.modifyProductStock(validProductStockDTO)).thenThrow(new Exception("Internal error"));
        ResponseEntity<String> response = managerController.modifyProductStock(validProductStockDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal error", response.getBody());
        verify(productService, times(1)).modifyProductStock(validProductStockDTO);

    }

    @Test
    public void testDeleteProduct_Success() throws Exception {

        Long validProductId = 1L;
        when(productService.deleteProduct(validProductId)).thenReturn("Product deleted successfully");
        ResponseEntity<String> response = managerController.deleteProduct(validProductId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product deleted successfully", response.getBody());
        verify(productService, times(1)).deleteProduct(validProductId);

    }

    @Test
    public void testDeleteProduct_ProductNotFound() throws Exception {

        Long validProductId = 1L;
        when(productService.deleteProduct(validProductId)).thenThrow(new ProductNotFoundException("Product not found"));
        ResponseEntity<String> response = managerController.deleteProduct(validProductId);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
        verify(productService, times(1)).deleteProduct(validProductId);

    }

    @Test
    public void testDeleteProduct_InternalServerError() throws Exception {

        Long validProductId = -1L;
        when(productService.deleteProduct(validProductId)).thenThrow(new Exception("Internal error"));
        ResponseEntity<String> response = managerController.deleteProduct(validProductId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal error", response.getBody());
        verify(productService, times(1)).deleteProduct(validProductId);

    }

    @Test
    public void testGetAllEmployees_Success() {

        User user1 = new User("employee1", "employee1@example.com", "password");
        User user2 = new User("employee2", "employee2@example.com", "password");
        List<UserDTO> mockUsersDTO = new ArrayList<>();
        List<User> mockUsers = Arrays.asList(user1, user2);
        for (User user : mockUsers) {
            UserDTO newUserDTO = UserDTO.builder().id(user.getId()).role(user.getRole()).username(user.getFunctionalUsername()).email(user.getEmail()).build();
            mockUsersDTO.add(newUserDTO);
        }
        when(userService.getAllEmployees()).thenReturn(mockUsersDTO);
        ResponseEntity<?> response = managerController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsersDTO, response.getBody());

    }

    @Test
    public void testValidateEmployee_Success() throws Exception {

        UserIdDTO userIdDTO = new UserIdDTO(123L);
        when(userService.validateEmployee(userIdDTO)).thenReturn("Validation successful");
        ResponseEntity<String> response = managerController.validateEmployee(userIdDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Validation successful", response.getBody());

    }

    @Test
    public void testValidateEmployee_MissingUserId() {

        UserIdDTO userIdDTO = new UserIdDTO(null);
        ResponseEntity<String> response = managerController.validateEmployee(userIdDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing userId to upgrade to validated employee", response.getBody());

    }

    @Test
    public void testValidateEmployee_UsernameNotFoundException() throws Exception {

        UserIdDTO userIdDTO = new UserIdDTO(123L);
        when(userService.validateEmployee(userIdDTO)).thenThrow(new UsernameNotFoundException("User not found"));
        ResponseEntity<String> response = managerController.validateEmployee(userIdDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService).validateEmployee(userIdDTO);

    }

    @Test
    public void testValidateEmployee_CannotUpgradeToValidatedEmployee() throws Exception {

        UserIdDTO userIdDTO = new UserIdDTO(123L);
        when(userService.validateEmployee(userIdDTO)).thenThrow(new CannotUpgradeToValidatedEmployee("Cannot upgrade to validated employee"));
        ResponseEntity<String> response = managerController.validateEmployee(userIdDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Cannot upgrade to validated employee", response.getBody());
        verify(userService).validateEmployee(userIdDTO);

    }

    @Test
    public void testValidateEmployee_InternalServerError() throws Exception {

        UserIdDTO userIdDTO = new UserIdDTO(123L);
        when(userService.validateEmployee(userIdDTO)).thenThrow(new Exception("Internal server error"));
        ResponseEntity<String> response = managerController.validateEmployee(userIdDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());
        verify(userService).validateEmployee(userIdDTO);

    }

    @Test
    public void testAddCategory_Success() throws Exception {

        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO("NewCategory");
        when(categoryService.addCategory(categoryRequestDTO)).thenReturn("Category created successfully");
        ResponseEntity<String> response = managerController.addCategory(categoryRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Category created successfully", response.getBody());
        verify(categoryService).addCategory(categoryRequestDTO);

    }

    @Test
    public void testAddCategory_ConflictExistingCategory() throws Exception {

        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO("ExistingCategory");
        when(categoryService.addCategory(categoryRequestDTO)).thenThrow(new ExistingCategoryFoundException("Category name already exists"));
        ResponseEntity<String> response = managerController.addCategory(categoryRequestDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Category name already exists", response.getBody());
        verify(categoryService).addCategory(categoryRequestDTO);

    }

    @Test
    public void testAddCategory_ConflictMissingInformation() {

        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO(null);
        ResponseEntity<String> response = managerController.addCategory(categoryRequestDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing information to create a category", response.getBody());
        verifyNoInteractions(categoryService);

    }

    @Test
    public void testAddCategory_InternalServerError() throws Exception {

        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO("InternalErrorCategory");
        when(categoryService.addCategory(categoryRequestDTO)).thenThrow(new Exception("Internal server error"));
        ResponseEntity<String> response = managerController.addCategory(categoryRequestDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Category NOT created", response.getBody());
        verify(categoryService).addCategory(categoryRequestDTO);

    }

    @Test
    public void testDeleteCategory_Success() throws Exception {

        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO("CategoryToDelete");
        when(categoryService.deleteCategory(categoryRequestDTO)).thenReturn("Category deleted successfully");
        ResponseEntity<String> response = managerController.deleteCategory(categoryRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category deleted successfully", response.getBody());
        verify(categoryService).deleteCategory(categoryRequestDTO);

    }

    @Test
    public void testDeleteCategory_ConflictCategoryNotFound() throws Exception {

        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO("NonExistingCategory");
        when(categoryService.deleteCategory(categoryRequestDTO)).thenThrow(new CategoryNotFoundException("Category not found"));
        ResponseEntity<String> response = managerController.deleteCategory(categoryRequestDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Category not found", response.getBody());
        verify(categoryService).deleteCategory(categoryRequestDTO);

    }

    @Test
    public void testDeleteCategory_ConflictMissingInformation() {

        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO(null);
        ResponseEntity<String> response = managerController.deleteCategory(categoryRequestDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing information to delete a category", response.getBody());
        verifyNoInteractions(categoryService);
    }

    @Test
    public void testDeleteCategory_ConflictCategoryHasProducts() throws Exception {

        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO("CategoryWithProducts");
        when(categoryService.deleteCategory(categoryRequestDTO)).thenThrow(new CategoryHasAtLeastOneProductAssociated("Category has at least one product associated"));
        ResponseEntity<String> response = managerController.deleteCategory(categoryRequestDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Category has at least one product associated", response.getBody());
        verify(categoryService).deleteCategory(categoryRequestDTO);

    }

    @Test
    public void testDeleteCategory_InternalServerError() throws Exception {

        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO("InternalErrorCategory");
        when(categoryService.deleteCategory(categoryRequestDTO)).thenThrow(new Exception("Internal server error"));
        ResponseEntity<String> response = managerController.deleteCategory(categoryRequestDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Category NOT deleted", response.getBody());
        verify(categoryService).deleteCategory(categoryRequestDTO);

    }

    //    @Test
//    public void testGetDashboardInformation_Success() throws Exception {
//
//        DashboardRequestDTO dashboardRequestDTO = new DashboardRequestDTO(/* Datos de prueba */);
//        DashboardInformationDTO expectedDashboardInfo = new DashboardInformationDTO(/* Datos esperados */);
//        when(dashboardService.getDashboardInformation(dashboardRequestDTO)).thenReturn(expectedDashboardInfo);
//        ResponseEntity<?> response = managerController.getDashboardInformation(dashboardRequestDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(expectedDashboardInfo, response.getBody());
//        verify(dashboardService).getDashboardInformation(dashboardRequestDTO);
//
//    }
//
//    @Test
//    public void testGetDashboardInformation_ConflictInvalidDate() throws Exception {
//
//        DashboardRequestDTO dashboardRequestDTO = new DashboardRequestDTO(/* Datos de prueba */);
//        when(dashboardService.getDashboardInformation(dashboardRequestDTO)).thenThrow(new InvalidDashboardRequestedDate("Invalid dashboard date"));
//        ResponseEntity<?> response = managerController.getDashboardInformation(dashboardRequestDTO);
//
//        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
//        assertEquals("Invalid dashboard date", response.getBody());
//        verify(dashboardService).getDashboardInformation(dashboardRequestDTO);
//
//    }
//
//    @Test
//    public void testGetDashboardInformation_InternalServerError() throws Exception {
//
//        DashboardRequestDTO dashboardRequestDTO = new DashboardRequestDTO(/* Datos de prueba */);
//        when(dashboardService.getDashboardInformation(dashboardRequestDTO)).thenThrow(new Exception("Internal server error"));
//        ResponseEntity<?> response = managerController.getDashboardInformation(dashboardRequestDTO);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Internal server error", response.getBody());
//        verify(dashboardService).getDashboardInformation(dashboardRequestDTO);
//    }
    @Test
    public void testAddGoal_Success() throws Exception {

        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setName("Test Goal");
        LocalDate today = LocalDate.now();
        Date startingDate = java.sql.Date.valueOf(today);
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        Date endingDate = java.sql.Date.valueOf(tomorrow);
        goalDTO.setStartingDate(startingDate);
        goalDTO.setEndingDate(endingDate);
        goalDTO.setObjectType("Product");
        goalDTO.setGoalObject("Product1");
        goalDTO.setGoalObjective(500.0);
        when(goalService.addGoal(goalDTO)).thenReturn("Goal created successfully");
        ResponseEntity<String> response = managerController.addGoal(goalDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Goal created successfully", response.getBody());

    }

    @Test
    public void testAddGoal_Conflict_ObjectTypeMissing() throws Exception {
        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setName("Test Goal");
        LocalDate today = LocalDate.now();
        Date startingDate = java.sql.Date.valueOf(today);
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        Date endingDate = java.sql.Date.valueOf(tomorrow);
        goalDTO.setStartingDate(startingDate);
        goalDTO.setEndingDate(endingDate);
        ResponseEntity<String> response = managerController.addGoal(goalDTO);

        verify(goalService, never()).addGoal(any());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing information to create a goal", response.getBody());

    }

    @Test
    public void testAddGoal_Conflict_InvalidObjectType() {

        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setName("Test Goal");
        LocalDate today = LocalDate.now();
        Date startingDate = java.sql.Date.valueOf(today);
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        Date endingDate = java.sql.Date.valueOf(tomorrow);
        goalDTO.setStartingDate(startingDate);
        goalDTO.setEndingDate(endingDate);
        goalDTO.setObjectType("InvalidType");
        goalDTO.setGoalObject("SomeObject");
        goalDTO.setGoalObjective(500.0);
        ResponseEntity<String> response = managerController.addGoal(goalDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing information to create a goal", response.getBody());

    }

    @Test
    public void testAddGoal_Conflict_MissingGoalObject() {

        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setName("Test Goal");
        LocalDate today = LocalDate.now();
        Date startingDate = java.sql.Date.valueOf(today);
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        Date endingDate = java.sql.Date.valueOf(tomorrow);
        goalDTO.setStartingDate(startingDate);
        goalDTO.setEndingDate(endingDate);
        goalDTO.setObjectType("Category");
        goalDTO.setGoalObjective(500.0);
        System.out.println("HOLA "+ goalDTO);
        ResponseEntity<String> response = managerController.addGoal(goalDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing information to create a goal", response.getBody());
    }

    @Test
    public void testAddGoal_Conflict_InvalidGoalObjective() throws Exception {

        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setName("Test Goal");
        LocalDate today = LocalDate.now();
        Date startingDate = java.sql.Date.valueOf(today);
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        Date endingDate = java.sql.Date.valueOf(tomorrow);
        goalDTO.setStartingDate(startingDate);
        goalDTO.setEndingDate(endingDate);
        goalDTO.setObjectType("Product");
        goalDTO.setGoalObject("SomeProduct");
        goalDTO.setGoalObjective(-100.0);

        ResponseEntity<String> response = managerController.addGoal(goalDTO);
        verify(goalService, never()).addGoal(any());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing information to create a goal", response.getBody());

    }


    @Test
    public void testAddGoal_Conflict_DatesCollisionException() throws Exception {

        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setName("Test Goal");
        goalDTO.setStartingDate(new Date());
        goalDTO.setEndingDate(new Date());
        goalDTO.setObjectType("Category");
        goalDTO.setGoalObject("TestCategory");
        goalDTO.setGoalObjective(500.0);
        when(goalService.addGoal(goalDTO)).thenThrow(new ProvidedDatesMustNotCollideWithOtherDatesException("Dates collision"));
        ResponseEntity<String> response = managerController.addGoal(goalDTO);

        verify(goalService).addGoal(goalDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Dates collision", response.getBody());
    }

    @Test
    public void testAddGoal_Conflict_ProductNotFoundException() throws Exception {

        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setName("Test Goal");
        goalDTO.setStartingDate(new Date());
        goalDTO.setEndingDate(new Date());
        goalDTO.setObjectType("Product");
        goalDTO.setGoalObject("NonExistingProduct");
        goalDTO.setGoalObjective(500.0);
        when(goalService.addGoal(goalDTO)).thenThrow(new ProductNotFoundException("Product not found"));
        ResponseEntity<String> response = managerController.addGoal(goalDTO);

        verify(goalService).addGoal(goalDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
    }

    @Test
    public void testAddGoal_Conflict_CategoryNotFoundException() throws Exception {

        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setName("Test Goal");
        goalDTO.setStartingDate(new Date());
        goalDTO.setEndingDate(new Date());
        goalDTO.setObjectType("Category");
        goalDTO.setGoalObject("NonExistingCategory");
        goalDTO.setGoalObjective(500.0);
        when(goalService.addGoal(goalDTO)).thenThrow(new CategoryNotFoundException("Category not found"));
        ResponseEntity<String> response = managerController.addGoal(goalDTO);

        verify(goalService).addGoal(goalDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Category not found", response.getBody());

    }

    @Test
    public void testAddGoal_Conflict_EndingDateMustBeAfterStartingDateException() throws Exception {

        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setName("Test Goal");
        LocalDate today = LocalDate.now();
        Date startingDate = java.sql.Date.valueOf(today);
        LocalDate yesterday = today.minus(1, ChronoUnit.DAYS);
        Date endingDate = java.sql.Date.valueOf(yesterday);
        goalDTO.setStartingDate(startingDate);
        goalDTO.setEndingDate(endingDate);
        goalDTO.setObjectType("Category");
        goalDTO.setGoalObject("TestCategory");
        goalDTO.setGoalObjective(500.0);
        when(goalService.addGoal(goalDTO)).thenThrow(new EndingDateMustBeAfterStartingDateException("Ending date must be after Starting date"));
        ResponseEntity<String> response = managerController.addGoal(goalDTO);

        verify(goalService).addGoal(goalDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Ending date must be after Starting date", response.getBody());

    }

    @Test
    public void testAddGoal_InternalServerError() throws Exception {

        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setName("Test Goal");
        goalDTO.setStartingDate(new Date());
        goalDTO.setEndingDate(new Date());
        goalDTO.setObjectType("Category");
        goalDTO.setGoalObject("TestCategory");
        goalDTO.setGoalObjective(500.0);
        when(goalService.addGoal(goalDTO)).thenThrow(new Exception("Internal error"));
        ResponseEntity<String> response = managerController.addGoal(goalDTO);

        verify(goalService).addGoal(goalDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal error", response.getBody());

    }

    @Test
    public void testDeleteGoal_Success() throws Exception {

        GoalDeleteDTO goalDeleteDTO = new GoalDeleteDTO();
        goalDeleteDTO.setGoalId(1L);
        when(goalService.deleteGoal(goalDeleteDTO)).thenReturn("Goal deleted successfully");
        ResponseEntity<String> response = managerController.deleteGoal(goalDeleteDTO);

        verify(goalService).deleteGoal(goalDeleteDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Goal deleted successfully", response.getBody());

    }

    @Test
    public void testDeleteGoal_Conflict_MissingGoalId() throws Exception {

        GoalDeleteDTO goalDeleteDTO = new GoalDeleteDTO();
        ResponseEntity<String> response = managerController.deleteGoal(goalDeleteDTO);

        verify(goalService, never()).deleteGoal(any());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing information to delete a goal", response.getBody());
    }

    @Test
    public void testDeleteGoal_Conflict_GoalIdNotFound() throws Exception {

        GoalDeleteDTO goalDeleteDTO = new GoalDeleteDTO();
        goalDeleteDTO.setGoalId(1L);
        when(goalService.deleteGoal(goalDeleteDTO)).thenThrow(new GoalIdNotFoundException("Goal ID not found"));
        ResponseEntity<String> response = managerController.deleteGoal(goalDeleteDTO);

        verify(goalService).deleteGoal(goalDeleteDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Goal ID not found", response.getBody());

    }

    @Test
    public void testDeleteGoal_Conflict_GoalInProcessCannotBeDeleted() throws Exception {

        GoalDeleteDTO goalDeleteDTO = new GoalDeleteDTO();
        goalDeleteDTO.setGoalId(1L);
        when(goalService.deleteGoal(goalDeleteDTO)).thenThrow(new GoalInProcessCannotBeDeletedException("Goal is in process and cannot be deleted"));
        ResponseEntity<String> response = managerController.deleteGoal(goalDeleteDTO);

        verify(goalService).deleteGoal(goalDeleteDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Goal is in process and cannot be deleted", response.getBody());

    }

    @Test
    public void testDeleteGoal_InternalServerError() throws Exception {

        GoalDeleteDTO goalDeleteDTO = new GoalDeleteDTO();
        goalDeleteDTO.setGoalId(1L);
        when(goalService.deleteGoal(goalDeleteDTO)).thenThrow(new Exception("Internal server error"));
        ResponseEntity<String> response = managerController.deleteGoal(goalDeleteDTO);

        verify(goalService).deleteGoal(goalDeleteDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());

    }

    @Test
    public void testModifyGoal_Success() throws Exception {

        GoalModifyDTO goalModifyDTO = new GoalModifyDTO();
        goalModifyDTO.setGoalId(1L);
        goalModifyDTO.setNewGoalObjective(1000.0);
        when(goalService.modifyGoal(goalModifyDTO)).thenReturn("Goal modified successfully");
        ResponseEntity<String> response = managerController.modifyGoal(goalModifyDTO);

        verify(goalService).modifyGoal(goalModifyDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Goal modified successfully", response.getBody());

    }

    @Test
    public void testModifyGoal_Conflict_MissingGoalId() throws Exception {

        GoalModifyDTO goalModifyDTO = new GoalModifyDTO();
        goalModifyDTO.setNewGoalObjective(1000.0);
        ResponseEntity<String> response = managerController.modifyGoal(goalModifyDTO);

        verify(goalService, never()).modifyGoal(any());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing information to modify a goal", response.getBody());

    }

    @Test
    public void testModifyGoal_Conflict_InvalidNewGoalObjective() throws Exception {

        GoalModifyDTO goalModifyDTO = new GoalModifyDTO();
        goalModifyDTO.setGoalId(1L);
        goalModifyDTO.setNewGoalObjective(-100.0);
        ResponseEntity<String> response = managerController.modifyGoal(goalModifyDTO);

        verify(goalService, never()).modifyGoal(any());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing information to modify a goal", response.getBody());

    }

    @Test
    public void testModifyGoal_Conflict_ProvidedDatesMustNotCollideWithOtherDates() throws Exception {

        GoalModifyDTO goalModifyDTO = new GoalModifyDTO();
        goalModifyDTO.setGoalId(1L);
        goalModifyDTO.setNewGoalObjective(1000.0);
        when(goalService.modifyGoal(goalModifyDTO)).thenThrow(new ProvidedDatesMustNotCollideWithOtherDatesException("Dates must not collide"));
        ResponseEntity<String> response = managerController.modifyGoal(goalModifyDTO);

        verify(goalService).modifyGoal(goalModifyDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Dates must not collide", response.getBody());

    }

    @Test
    public void testModifyGoal_InternalServerError() throws Exception {

        GoalModifyDTO goalModifyDTO = new GoalModifyDTO();
        goalModifyDTO.setGoalId(1L);
        goalModifyDTO.setNewGoalObjective(1000.0);
        when(goalService.modifyGoal(goalModifyDTO)).thenThrow(new Exception("Internal server error"));
        ResponseEntity<String> response = managerController.modifyGoal(goalModifyDTO);

        verify(goalService).modifyGoal(goalModifyDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());

    }


    @Test
    public void testGetGoals_Success() throws Exception {

        GoalFilterRequestDTO goalFilterRequestDTO = new GoalFilterRequestDTO();
        goalFilterRequestDTO.setAchieved(Arrays.asList("Achieved","Not Achieved"));
        goalFilterRequestDTO.setStatus(Arrays.asList("Upcoming","Expired"));
        when(goalService.getGoals(goalFilterRequestDTO)).thenReturn(new ArrayList<>());
        ResponseEntity<?> response = managerController.getGoals(goalFilterRequestDTO);

        verify(goalService).getGoals(goalFilterRequestDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ArrayList<>(), response.getBody());

    }

    @Test
    public void testGetGoals_Conflict_NullLists() throws Exception {

        GoalFilterRequestDTO goalFilterRequestDTO = new GoalFilterRequestDTO();
        ResponseEntity<?> response = managerController.getGoals(goalFilterRequestDTO);

        verify(goalService, never()).getGoals(any());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Lists must not be null", response.getBody());
    }

    @Test
    public void testGetGoals_InternalServerError() throws Exception {

        GoalFilterRequestDTO goalFilterRequestDTO = new GoalFilterRequestDTO();
        goalFilterRequestDTO.setAchieved(Arrays.asList("Achieved","Not Achieved"));
        goalFilterRequestDTO.setStatus(Arrays.asList("Upcoming","Expired"));
        when(goalService.getGoals(goalFilterRequestDTO)).thenThrow(new Exception("Internal server error"));
        ResponseEntity<?> response = managerController.getGoals(goalFilterRequestDTO);

        verify(goalService).getGoals(goalFilterRequestDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());

    }

}
