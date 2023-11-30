package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.ModifyOrderDTO;
import com.messismo.bar.DTOs.OrderIdDTO;
import com.messismo.bar.DTOs.OrderRequestDTO;
import com.messismo.bar.DTOs.ProductOrderDTO;
import com.messismo.bar.Entities.*;
import com.messismo.bar.Exceptions.OrderNotFoundException;
import com.messismo.bar.Exceptions.ProductQuantityBelowAvailableStock;
import com.messismo.bar.Exceptions.UserNotFoundException;
import com.messismo.bar.Repositories.OrderRepository;
import com.messismo.bar.Repositories.ProductOrderRepository;
import com.messismo.bar.Repositories.ProductRepository;
import com.messismo.bar.Repositories.UserRepository;
import com.messismo.bar.Services.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
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
    public void testGetAllOrders() {

        List<Order> mockOrders = new ArrayList<>();
        when(orderRepository.findAll()).thenReturn(mockOrders);
        List<?> response = orderService.getAllOrders();

        verify(orderRepository, times(1)).findAll();
        Assertions.assertEquals(mockOrders, response);

    }

    @Test
    public void testAddNewOrder() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = dateFormat.parse("2023-08-04 08:10:43");
        User user = User.builder().username("example").email("employee@example.com").password("password1").id(1L)
                .role(Role.EMPLOYEE).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Empanadas").stock(50).category(category1)
                .unitPrice(50.00).unitCost(20.00).description("Empanadas de carne").build();
        Product product2 = Product.builder().productId(2L).name("Ensalada").stock(50).category(category1)
                .unitPrice(50.00).unitCost(25.00).description("Ensalada de pollo").build();
        List<ProductOrderDTO> productOrders = new ArrayList<>();
        ProductOrderDTO productOrderDTO1 = ProductOrderDTO.builder().product(product1).quantity(2).build();
        productOrders.add(productOrderDTO1);
        ProductOrderDTO productOrderDTO2 = ProductOrderDTO.builder().product(product2).quantity(3).build();
        productOrders.add(productOrderDTO2);
        OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder().dateCreated(date1)
                .registeredEmployeeEmail("employee@example.com").productOrders(productOrders).build();

        when(userRepository.findByEmail("employee@example.com")).thenReturn(Optional.ofNullable(user));
        Assertions.assertEquals(orderService.addNewOrder(orderRequestDTO),
                "Order created successfully");

    }

    @Test
    public void testAddNewOrder_UserNotFound() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = dateFormat.parse("2023-08-04 08:10:43");
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Empanadas").stock(50).category(category1).unitPrice(50.00).unitCost(20.00).description("Empanadas de carne").build();
        Product product2 = Product.builder().productId(2L).name("Ensalada").stock(50).category(category1).unitPrice(50.00).unitCost(25.00).description("Ensalada de pollo").build();
        List<ProductOrderDTO> productOrders = new ArrayList<>();
        ProductOrderDTO productOrderDTO1 = ProductOrderDTO.builder().product(product1).quantity(2).build();
        productOrders.add(productOrderDTO1);
        ProductOrderDTO productOrderDTO2 = ProductOrderDTO.builder().product(product2).quantity(3).build();
        productOrders.add(productOrderDTO2);
        OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder().dateCreated(date1).registeredEmployeeEmail("employee@example.com").productOrders(productOrders).build();

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            orderService.addNewOrder(orderRequestDTO);
        });
        Assertions.assertEquals("No user has that email", exception.getMessage());

    }

    @Test
    public void testAddNewOrder_InsufficientStock() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = dateFormat.parse("2023-08-04 08:10:43");
        User user = User.builder().username("example").email("nonexistent@example.com").password("password1").id(1L).role(Role.EMPLOYEE).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Empanadas").stock(1).category(category1).unitPrice(50.00).unitCost(25.00).description("Empanadas de carne").build();
        Product product2 = Product.builder().productId(2L).name("Ensalada").stock(50).category(category1).unitPrice(50.00).unitCost(25.00).description("Ensalada de pollo").build();
        List<ProductOrderDTO> productOrders = new ArrayList<>();
        ProductOrderDTO productOrderDTO1 = ProductOrderDTO.builder().product(product1).quantity(2).build();
        productOrders.add(productOrderDTO1);
        ProductOrderDTO productOrderDTO2 = ProductOrderDTO.builder().product(product2).quantity(3).build();
        productOrders.add(productOrderDTO2);
        OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder().dateCreated(date1).registeredEmployeeEmail("employee@example.com").productOrders(productOrders).build();

        when(userRepository.findByEmail("employee@example.com")).thenReturn(Optional.ofNullable(user));
        ProductQuantityBelowAvailableStock exception = assertThrows(ProductQuantityBelowAvailableStock.class, () -> {
            orderService.addNewOrder(orderRequestDTO);
        });
        Assertions.assertEquals("Not enough stock of a product", exception.getMessage());

    }

    @Test
    public void testAddOrder_Exception() throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = dateFormat.parse("2023-08-04 08:10:43");
        User user = User.builder().username("example").email("employee@example.com").password("password1").id(1L)
                .role(Role.EMPLOYEE).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Empanadas").stock(50).category(category1)
                .unitPrice(50.00).unitCost(20.00).description("Empanadas de carne").build();
        Product product2 = Product.builder().productId(2L).name("Ensalada").stock(50).category(category1)
                .unitPrice(50.00).unitCost(25.00).description("Ensalada de pollo").build();
        List<ProductOrderDTO> productOrders = new ArrayList<>();
        ProductOrderDTO productOrderDTO1 = ProductOrderDTO.builder().product(product1).quantity(2).build();
        productOrders.add(productOrderDTO1);
        ProductOrderDTO productOrderDTO2 = ProductOrderDTO.builder().product(product2).quantity(3).build();
        productOrders.add(productOrderDTO2);
        OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder().dateCreated(date1)
                .registeredEmployeeEmail("employee@example.com").productOrders(productOrders).build();

        when(userRepository.findByEmail("employee@example.com")).thenReturn(Optional.ofNullable(user));

        doThrow(new RuntimeException("Runtime Exception")).when(orderRepository).save(any());
        Exception exception = assertThrows(Exception.class, () -> {
            orderService.addNewOrder(orderRequestDTO);
        });
        Assertions.assertEquals("CANNOT create an order at the moment", exception.getMessage());

    }

    @Test
    public void testCloseOrder_Success() throws Exception {

        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setStatus("Open");
        OrderIdDTO orderIdDTO = new OrderIdDTO();
        orderIdDTO.setOrderId(1L);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(existingOrder));
        String response = orderService.closeOrder(orderIdDTO);

        Assertions.assertEquals("Order closed successfully", response);
        verify(orderRepository, times(1)).save(any());
    }


    @Test
    public void testCloseOrder_Exception() throws ParseException {

        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setStatus("Open");
        OrderIdDTO orderIdDTO = new OrderIdDTO();
        orderIdDTO.setOrderId(1L);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(existingOrder));

        doThrow(new RuntimeException("Runtime Exception")).when(orderRepository).save(any());
        Exception exception = assertThrows(Exception.class, () -> {
            orderService.closeOrder(orderIdDTO);
        });
        Assertions.assertEquals("CANNOT close an order at the moment", exception.getMessage());

    }
    @Test
    public void testCloseOrder_OrderIdNotFound() {

        OrderIdDTO orderIdDTO = new OrderIdDTO();
        orderIdDTO.setOrderId(1L);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.closeOrder(orderIdDTO);
        });
        Assertions.assertEquals("Order not found", exception.getMessage());

    }

    @Test
    public void testModifyOrder_InsufficientStock() {

        User usuario = User.builder().username("admincito").id(155L).role(Role.ADMIN).email("admin@mail.com").password("Password1").build();
        Order existingOrder = Order.builder().dateCreated(new Date()).status("open").id(166L).productOrders(new ArrayList<>()).totalCost(0.00).totalPrice(0.00).user(usuario).build();
        List<ProductOrderDTO> productOrderDTO = new ArrayList<>();
        Category category = Category.builder().categoryId(166L).name("Tomates").build();
        Product product = Product.builder().name("Tomato").productId(155L).unitCost(500.00).unitPrice(4500.00).description("aProduct").category(category).stock(10).build();
        ProductOrderDTO productOrderDTO1 = ProductOrderDTO.builder().product(product).quantity(111).build();
        productOrderDTO.add(productOrderDTO1);
        ModifyOrderDTO modifyOrderDTO = ModifyOrderDTO.builder().orderId(166L).productOrders(productOrderDTO).build();

        when(orderRepository.findById(existingOrder.getId())).thenReturn(Optional.of(existingOrder));
        ProductQuantityBelowAvailableStock exception = assertThrows(ProductQuantityBelowAvailableStock.class, () -> {
            orderService.modifyOrder(modifyOrderDTO);
        });
        Assertions.assertEquals("Not enough stock of a product", exception.getMessage());

    }

    @Test
    public void testModifyOrder_Exception() {

        ModifyOrderDTO modifyOrderDTO = new ModifyOrderDTO();
        modifyOrderDTO.setOrderId(10000L);

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.modifyOrder(modifyOrderDTO);
        });
        Assertions.assertEquals("Order not found", exception.getMessage());

    }

    @Test
    public void testModifyOrder_OrderIdNotFound() {

        User user = User.builder().username("admincito").id(155L).role(Role.ADMIN).email("admin@mail.com").password("Password1").build();
        Order existingOrder = Order.builder().dateCreated(new Date()).status("open").id(166L).productOrders(new ArrayList<>()).totalCost(0.00).totalPrice(0.00).user(user).build();
        List<ProductOrderDTO> productOrderDTO = new ArrayList<>();
        Category category = Category.builder().categoryId(166L).name("Tomates").build();
        Product product = Product.builder().name("Tomato").productId(155L).unitCost(500.00).unitPrice(4500.00).description("aProduct").category(category).stock(10).build();
        ProductOrderDTO productOrderDTO1 = ProductOrderDTO.builder().product(product).quantity(1).build();
        productOrderDTO.add(productOrderDTO1);
        ModifyOrderDTO modifyOrderDTO = ModifyOrderDTO.builder().orderId(166L).productOrders(productOrderDTO).build();
        when(orderRepository.findById(existingOrder.getId())).thenReturn(Optional.of(existingOrder));
        doThrow(new RuntimeException("Runtime Exception")).when(orderRepository).save(any());
        Exception exception = assertThrows(Exception.class, () -> {
            orderService.modifyOrder(modifyOrderDTO);
        });
        Assertions.assertEquals("CANNOT modify this order at the moment", exception.getMessage());

    }


    @Test
    public void testModifyOrder() throws Exception {

        User usuario = User.builder().username("admincito").id(155L).role(Role.ADMIN).email("admin@mail.com").password("Password1").build();
        Order existingOrder = Order.builder().dateCreated(new Date()).status("open").id(166L).productOrders(new ArrayList<>()).totalCost(0.00).totalPrice(0.00).user(usuario).build();
        List<ProductOrderDTO> productOrderDTO = new ArrayList<>();
        Category category = Category.builder().categoryId(166L).name("Tomates").build();
        Product product = Product.builder().name("Tomato").productId(155L).unitCost(500.00).unitPrice(4500.00).description("aProduct").category(category).stock(10).build();
        ProductOrderDTO productOrderDTO1 = ProductOrderDTO.builder().product(product).quantity(1).build();
        productOrderDTO.add(productOrderDTO1);
        ModifyOrderDTO modifyOrderDTO = ModifyOrderDTO.builder().orderId(166L).productOrders(productOrderDTO).build();
        when(orderRepository.findById(existingOrder.getId())).thenReturn(Optional.of(existingOrder));
        String response = orderService.modifyOrder(modifyOrderDTO);

        Assertions.assertEquals("Order modified successfully", response);
    }

}
