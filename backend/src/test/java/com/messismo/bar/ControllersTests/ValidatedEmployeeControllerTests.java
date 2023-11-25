package com.messismo.bar.ControllersTests;

import com.messismo.bar.Controllers.ValidatedEmployeeController;
import com.messismo.bar.DTOs.*;
import com.messismo.bar.Entities.*;
import com.messismo.bar.Exceptions.*;
import com.messismo.bar.Services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ValidatedEmployeeControllerTests {

    @InjectMocks
    private ValidatedEmployeeController validatedEmployeeController;

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testAddProduct_Success() throws Exception {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory("TestCategory");
        productDTO.setName("TestProduct");
        productDTO.setUnitPrice(20.0);
        productDTO.setDescription("Product description");
        productDTO.setStock(50);
        productDTO.setUnitCost(15.0);
        productDTO.setNewCategory(false);
        when(productService.addProduct(productDTO)).thenReturn("Product added successfully");
        ResponseEntity<?> response = validatedEmployeeController.addProduct(productDTO);

        verify(productService).addProduct(productDTO);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("Product added successfully", response.getBody());

    }

    @Test
    public void testAddProduct_Conflict_MissingInformation() throws Exception {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("TestProduct");
        ResponseEntity<?> response = validatedEmployeeController.addProduct(productDTO);

        verify(productService, never()).addProduct(any());
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("Missing information to create a product", response.getBody());

    }

    @Test
    public void testAddProduct_Conflict_InvalidValues() throws Exception {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory("TestCategory");
        productDTO.setName("TestProduct");
        productDTO.setUnitPrice(-5.0);
        productDTO.setDescription("Product description");
        productDTO.setStock(50);
        productDTO.setUnitCost(15.0);
        productDTO.setNewCategory(false);
        ResponseEntity<?> response = validatedEmployeeController.addProduct(productDTO);

        verify(productService, never()).addProduct(any());
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("Some values cannot be less than zero. Please check", response.getBody());

    }

    @Test
    public void testAddProduct_Conflict_CategoryNotFound() throws Exception {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory("NonExistingCategory");
        productDTO.setName("TestProduct");
        productDTO.setUnitPrice(20.0);
        productDTO.setDescription("Product description");
        productDTO.setStock(50);
        productDTO.setUnitCost(15.0);
        productDTO.setNewCategory(false);
        when(productService.addProduct(productDTO)).thenThrow(new CategoryNotFoundException("Category not found"));
        ResponseEntity<?> response = validatedEmployeeController.addProduct(productDTO);

        verify(productService).addProduct(productDTO);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("Category not found", response.getBody());

    }

    @Test
    public void testAddProduct_Conflict_ExistingProductFound() throws Exception {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory("TestCategory");
        productDTO.setName("TestProduct");
        productDTO.setUnitPrice(20.0);
        productDTO.setDescription("Product description");
        productDTO.setStock(50);
        productDTO.setUnitCost(15.0);
        productDTO.setNewCategory(false);
        when(productService.addProduct(productDTO)).thenThrow(new ExistingProductFoundException("Product already exists"));
        ResponseEntity<?> response = validatedEmployeeController.addProduct(productDTO);

        verify(productService).addProduct(productDTO);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("Product already exists", response.getBody());

    }

    @Test
    public void testAddProduct_InternalServerError() throws Exception {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory("TestCategory");
        productDTO.setName("TestProduct");
        productDTO.setUnitPrice(20.0);
        productDTO.setDescription("Product description");
        productDTO.setStock(50);
        productDTO.setUnitCost(15.0);
        productDTO.setNewCategory(false);
        when(productService.addProduct(productDTO)).thenThrow(new Exception("Internal error"));
        ResponseEntity<?> response = validatedEmployeeController.addProduct(productDTO);

        verify(productService).addProduct(productDTO);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Internal error", response.getBody());

    }

    @Test
    public void testGetAllProducts_Success() {

        Product product1 = new Product(1L,"Product1",10.0,7.0,"Description1",50,new Category(1L,"Category1"));
        Product product2 = new Product(2L,"Product2",15.0,7.0,"Description2",100,new Category(2L,"Category2"));
        Product product3 = new Product(3L,"Product3",20.0,10.0,"Description3",200,new Category(3L,"Category3"));
        List<Product> productList = Arrays.asList(product1,product2,product3);
        when(productService.getAllProducts()).thenReturn(productList);
        ResponseEntity<?> response = validatedEmployeeController.getAllProducts();

        verify(productService).getAllProducts();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void testFilterProducts_Success() throws Exception {

        Product product1 = new Product(1L,"Product1",10.0,7.0,"Description1",50,new Category(1L,"Category1"));
        Product product2 = new Product(2L,"Product2",15.0,7.0,"Description2",100,new Category(2L,"Category2"));
        Product product3 = new Product(3L,"Product3",20.0,10.0,"Description3",200,new Category(3L,"Category3"));
        List<Product> productList = Arrays.asList(product1,product2,product3);
        FilterProductDTO filterProductDTO = new FilterProductDTO();
        when(productService.filterProducts(any(FilterProductDTO.class)))
                .thenReturn(productList);
        ResponseEntity<?> response = validatedEmployeeController.filterProducts(filterProductDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void testFilterProducts_Conflict_CategoryNotFound() throws Exception {

        FilterProductDTO filterProductDTO = new FilterProductDTO();
        when(productService.filterProducts(any(FilterProductDTO.class)))
                .thenThrow(new CategoryNotFoundException("Category not found"));
        ResponseEntity<?> response = validatedEmployeeController.filterProducts(filterProductDTO);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("Category not found", response.getBody());

    }

    @Test
    public void testFilterProducts_InternalServerError() throws Exception {

        FilterProductDTO filterProductDTO = new FilterProductDTO();
        when(productService.filterProducts(any(FilterProductDTO.class)))
                .thenThrow(new Exception("Internal server error"));
        ResponseEntity<?> response = validatedEmployeeController.filterProducts(filterProductDTO);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Internal server error", response.getBody());

    }

    @Test
    public void testGetAllCategories() {

        Category category1 = new Category(1L,"Category1");
        Category category2 = new Category(2L,"Category2");
        Category category3 = new Category(3L,"Category3");
        List<Category> categoryList = Arrays.asList(category1,category2,category3);
        when(categoryService.getAllCategories()).thenReturn(categoryList);
        ResponseEntity<?> response = validatedEmployeeController.getAllCategories();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(categoryList, response.getBody());

    }


    @Test
    public void testAddNewOrder_Success() throws Exception {

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO("employee@example.com",new Date(),List.of(new ProductOrderDTO(new Product(1L,"Product1",10.0,7.0,"Description1",50,new Category(1L,"Category1")), 2)));
        when(orderService.addNewOrder(orderRequestDTO)).thenReturn("Order created successfully");
        ResponseEntity<?> response = validatedEmployeeController.addNewOrder(orderRequestDTO);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("Order created successfully", response.getBody());

    }

    @Test
    public void testAddNewOrder_Conflict_UserNotFound() throws Exception {

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO("employee@example.com",new Date(),List.of(new ProductOrderDTO(new Product(1L,"Product1",10.0,7.0,"Description1",50,new Category(1L,"Category1")), 2)));
        when(orderService.addNewOrder(orderRequestDTO)).thenThrow(new UserNotFoundException("User not found"));
        ResponseEntity<?> response = validatedEmployeeController.addNewOrder(orderRequestDTO);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("User not found", response.getBody());

    }

    @Test
    public void testAddNewOrder_Conflict_ProductQuantityBelowAvailableStock() throws Exception {

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO("employee@example.com",new Date(),List.of(new ProductOrderDTO(new Product(1L,"Product1",10.0,7.0,"Description1",50,new Category(1L,"Category1")), 2)));
        when(orderService.addNewOrder(orderRequestDTO)).thenThrow(new ProductQuantityBelowAvailableStock("Product quantity below available stock"));
        ResponseEntity<?> response = validatedEmployeeController.addNewOrder(orderRequestDTO);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("Product quantity below available stock", response.getBody());

    }

    @Test
    public void testAddNewOrder_InternalServerError() throws Exception {

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO("employee@example.com",new Date(),List.of(new ProductOrderDTO(new Product(1L,"Product1",10.0,7.0,"Description1",50,new Category(1L,"Category1")), 2)));
        when(orderService.addNewOrder(orderRequestDTO)).thenThrow(new Exception("Internal server error"));
        ResponseEntity<?> response = validatedEmployeeController.addNewOrder(orderRequestDTO);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Internal server error", response.getBody());

    }

    @Test
    public void testCloseOrder_Success() throws Exception {

        OrderIdDTO orderIdDTO = new OrderIdDTO(1L);
        when(orderService.closeOrder(orderIdDTO)).thenReturn("Order closed successfully");
        ResponseEntity<?> response = validatedEmployeeController.closeOrder(orderIdDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Order closed successfully", response.getBody());

    }

    @Test
    public void testCloseOrder_Conflict_OrderNotFound() throws Exception {

        OrderIdDTO orderIdDTO = new OrderIdDTO(1L);
        when(orderService.closeOrder(orderIdDTO)).thenThrow(new OrderNotFoundException("Order not found"));
        ResponseEntity<?> response = validatedEmployeeController.closeOrder(orderIdDTO);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("Order not found", response.getBody());

    }

    @Test
    public void testCloseOrder_InternalServerError() throws Exception {

        OrderIdDTO orderIdDTO = new OrderIdDTO(1L);
        when(orderService.closeOrder(orderIdDTO)).thenThrow(new RuntimeException("Internal server error"));
        ResponseEntity<?> response = validatedEmployeeController.closeOrder(orderIdDTO);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Internal server error", response.getBody());

    }

    @Test
    public void testModifyOrder_Success() throws Exception {

        ModifyOrderDTO modifyOrderDTO = new ModifyOrderDTO(1L,List.of(new ProductOrderDTO(new Product(1L,"Product1",10.0,7.0,"Description1",50,new Category(1L,"Category1")), 2)));
        when(orderService.modifyOrder(modifyOrderDTO)).thenReturn("Order modified successfully");
        ResponseEntity<?> response = validatedEmployeeController.modifyOrder(modifyOrderDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Order modified successfully", response.getBody());

    }

    @Test
    public void testModifyOrder_Conflict_ProductQuantityBelowAvailableStock() throws Exception {

        ModifyOrderDTO modifyOrderDTO = new ModifyOrderDTO(1L,List.of(new ProductOrderDTO(new Product(1L,"Product1",10.0,7.0,"Description1",50,new Category(1L,"Category1")), 2)));
        when(orderService.modifyOrder(modifyOrderDTO)).thenThrow(new ProductQuantityBelowAvailableStock("Insufficient stock"));
        ResponseEntity<?> response = validatedEmployeeController.modifyOrder(modifyOrderDTO);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("Insufficient stock", response.getBody());

    }

    @Test
    public void testModifyOrder_Conflict_OrderNotFound() throws Exception {

        ModifyOrderDTO modifyOrderDTO = new ModifyOrderDTO(1L,List.of(new ProductOrderDTO(new Product(1L,"Product1",10.0,7.0,"Description1",50,new Category(1L,"Category1")), 2)));
        when(orderService.modifyOrder(modifyOrderDTO)).thenThrow(new OrderNotFoundException("Order not found"));
        ResponseEntity<?> response = validatedEmployeeController.modifyOrder(modifyOrderDTO);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("Order not found", response.getBody());

    }

    @Test
    public void testModifyOrder_InternalServerError() throws Exception {

        ModifyOrderDTO modifyOrderDTO = new ModifyOrderDTO(1L,List.of(new ProductOrderDTO(new Product(1L,"Product1",10.0,7.0,"Description1",50,new Category(1L,"Category1")), 2)));
        when(orderService.modifyOrder(modifyOrderDTO)).thenThrow(new RuntimeException("Internal server error"));
        ResponseEntity<?> response = validatedEmployeeController.modifyOrder(modifyOrderDTO);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Internal server error", response.getBody());

    }

    @Test
    public void testGetAllOrders() {

        List<Order> mockOrders = Arrays.asList(
                new Order( new User("user1","user1@mail.com","Password1"), new Date(), List.of(new ProductOrder("Product1",10.0,7.0,new Category(1L,"Category1"), 2)),20.0,14.0),
                new Order( new User("user1","user1@mail.com","Password1"), new Date(), List.of(new ProductOrder("Product2",30.0,10.0,new Category(2L,"Category2"), 3)),90.0,30.0)
        );
        when(orderService.getAllOrders()).thenReturn(mockOrders);
        ResponseEntity<?> response = validatedEmployeeController.getAllOrders();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(mockOrders, response.getBody());

    }


}
