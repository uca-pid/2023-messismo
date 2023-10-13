package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.OrderRequestDTO;
import com.messismo.bar.DTOs.ProductOrderDTO;
import com.messismo.bar.Entities.*;
import com.messismo.bar.Repositories.*;
import com.messismo.bar.Services.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class OrderServiceTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductOrderRepository productOrderRepository;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllOrders() throws ParseException {
        List<Order> mockOrders = new ArrayList<>();
        when(orderRepository.findAll()).thenReturn(mockOrders);
        ResponseEntity<?> response = orderService.getAllOrders();
        verify(orderRepository, times(1)).findAll();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(mockOrders, response.getBody());
    }

    @Test
    public void testAddNewOrder() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = dateFormat.parse("2023-08-04 08:10:43");
        User user = User.builder().username("example").email("employee@example.com").password("password1").id(1L).role(Role.EMPLOYEE).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Empanadas").stock(50).category(category1).unitPrice(50.00).description("Empanadas de carne").build();
        Product product2 = Product.builder().productId(2L).name("Ensalada").stock(50).category(category1).unitPrice(50.00).description("Ensalada de pollo").build();
        List<ProductOrderDTO> productOrders = new ArrayList<>();
        ProductOrderDTO productOrderDTO1 = ProductOrderDTO.builder().product(product1).quantity(2).build();
        productOrders.add(productOrderDTO1);
        ProductOrderDTO productOrderDTO2 = ProductOrderDTO.builder().product(product2).quantity(3).build();
        productOrders.add(productOrderDTO2);
        OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder().dateCreated(date1).totalPrice(250.00).registeredEmployeeEmail("employee@example.com").productOrders(productOrders).build();

        when(userRepository.findByEmail("employee@example.com")).thenReturn(Optional.ofNullable(user));
        Assertions.assertEquals(orderService.addNewOrder(orderRequestDTO), ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully"));
    }

    @Test
    public void testAddNewOrder_UserNotFound() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = dateFormat.parse("2023-08-04 08:10:43");
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Empanadas").stock(50).category(category1).unitPrice(50.00).description("Empanadas de carne").build();
        Product product2 = Product.builder().productId(2L).name("Ensalada").stock(50).category(category1).unitPrice(50.00).description("Ensalada de pollo").build();
        List<ProductOrderDTO> productOrders = new ArrayList<>();
        ProductOrderDTO productOrderDTO1 = ProductOrderDTO.builder().product(product1).quantity(2).build();
        productOrders.add(productOrderDTO1);
        ProductOrderDTO productOrderDTO2 = ProductOrderDTO.builder().product(product2).quantity(3).build();
        productOrders.add(productOrderDTO2);
        OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder().dateCreated(date1).totalPrice(250.00).registeredEmployeeEmail("employee@example.com").productOrders(productOrders).build();

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        Assertions.assertEquals(orderService.addNewOrder(orderRequestDTO), ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT create an order at the moment."));
    }

    @Test
    public void testAddNewOrder_InsufficientStock() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = dateFormat.parse("2023-08-04 08:10:43");
        User user = User.builder().username("example").email("nonexistent@example.com").password("password1").id(1L).role(Role.EMPLOYEE).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Empanadas").stock(1).category(category1).unitPrice(50.00).description("Empanadas de carne").build();
        Product product2 = Product.builder().productId(2L).name("Ensalada").stock(50).category(category1).unitPrice(50.00).description("Ensalada de pollo").build();
        List<ProductOrderDTO> productOrders = new ArrayList<>();
        ProductOrderDTO productOrderDTO1 = ProductOrderDTO.builder().product(product1).quantity(2).build();
        productOrders.add(productOrderDTO1);
        ProductOrderDTO productOrderDTO2 = ProductOrderDTO.builder().product(product2).quantity(3).build();
        productOrders.add(productOrderDTO2);
        OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder().dateCreated(date1).totalPrice(250.00).registeredEmployeeEmail("employee@example.com").productOrders(productOrders).build();

        when(userRepository.findByEmail("employee@example.com")).thenReturn(Optional.ofNullable(user));
        Assertions.assertEquals(orderService.addNewOrder(orderRequestDTO), ResponseEntity.status(HttpStatus.CONFLICT).body("Not enough stock of a product"));
    }
}
