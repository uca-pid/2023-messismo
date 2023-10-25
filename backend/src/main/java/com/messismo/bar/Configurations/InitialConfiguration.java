package com.messismo.bar.Configurations;

import com.messismo.bar.DTOs.*;
import com.messismo.bar.Entities.Order;
import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.OrderRepository;
import com.messismo.bar.Repositories.ProductRepository;
import com.messismo.bar.Repositories.UserRepository;
import com.messismo.bar.Services.AuthenticationService;
import com.messismo.bar.Services.CategoryService;
import com.messismo.bar.Services.OrderService;
import com.messismo.bar.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InitialConfiguration {

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService authenticationService, UserRepository userRepository, ProductService productService, CategoryService categoryService, OrderService orderService, OrderRepository orderRepository, ProductRepository productRepository) {
        return args -> {
            RegisterRequestDTO admin = new RegisterRequestDTO();
            admin.setUsername("admin");
            admin.setEmail("admin@mail.com");
            admin.setPassword("Password1");
            authenticationService.register(admin);
            User createdAdmin = userRepository.findByEmail(admin.getEmail()).get();
            createdAdmin.setRole(Role.ADMIN);
            userRepository.save(createdAdmin);
            addSampleEmployees(authenticationService, userRepository);
            addSampleCategories(categoryService);
            addSampleProducts(productService);
            addSampleOrders(orderService, productRepository);
            closeOrders(orderRepository);
        };
    }

    private void closeOrders(OrderRepository orderRepository) {
        List<Order> allOrders = orderRepository.findAll();
        LocalDate cutoffDate = LocalDate.of(2023, 10, 1);
        for(Order order : allOrders){
            if (order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(cutoffDate)) {
                order.setStatus("Closed");
                orderRepository.save(order);
            }
        }
    }

    private void addSampleOrders(OrderService orderService, ProductRepository productRepository) throws ParseException {
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2023-08-04 08:10:43", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2023-09-25 05:02:23", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2023-09-27 14:30:10", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Cheeseburger").get()).quantity(3).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-09-26 17:45:22", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Caesar Salad").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Assorted Sushi").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Chocolate Profiteroles").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-09-24 11:20:05", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2023-09-23 10:15:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Italian Antipasto").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Shrimp Ceviche").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-09-22 19:55:15", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(3).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2023-09-21 08:00:11", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(3).build()));
        generateOrderRequestDTO(orderService, "guidomartin7@gmail.com", "2023-09-20 12:45:07", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Spaghetti Carbonara").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2023-09-19 15:10:25", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Espresso Coffee").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(3).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2023-01-15 12:30:10", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Chocolate Profiteroles").get()).quantity(3).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-02-20 18:45:22", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2023-03-10 09:20:05", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Italian Antipasto").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Shrimp Ceviche").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-05-05 16:15:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Caesar Salad").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Raspberry Soda").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-07-19 14:55:15", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Assorted Sushi").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2023-04-08 19:40:55", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Italian Antipasto").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2023-06-12 13:10:20", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Chocolate Profiteroles").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(3).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-08-25 17:30:45", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Shrimp Ceviche").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-09-15 08:45:55", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Assorted Sushi").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Raspberry Soda").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2023-10-20 14:20:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Raspberry Soda").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "admin@mail.com", "2023-02-14 10:25:15", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2023-05-20 19:15:40", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Chocolate Profiteroles").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Italian Antipasto").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Shrimp Ceviche").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-07-08 15:40:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Assorted Sushi").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Raspberry Soda").get()).quantity(3).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-09-18 12:30:55", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2023-10-22 17:10:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Raspberry Soda").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2023-01-15 08:45:20", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2023-01-25 19:30:10", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Chocolate Profiteroles").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-02-10 12:15:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "admin@mail.com", "2023-03-08 16:45:40", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Assorted Sushi").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Caesar Salad").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-04-05 14:20:15", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2023-05-19 09:30:20", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2023-05-22 18:15:50", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Raspberry Soda").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-06-10 20:40:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Italian Antipasto").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2023-07-14 14:10:25", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Chocolate Profiteroles").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "guidomartin7@gmail.com", "2023-08-05 11:25:15", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Shrimp Ceviche").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2023-09-19 08:30:20", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2023-10-22 15:15:45", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-08-11 19:00:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2023-10-14 17:20:35", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Assorted Sushi").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2023-10-28 21:10:50", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Cheeseburger").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2022-01-10 12:45:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-01-25 19:30:15", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2022-02-08 14:15:22", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Spanish Omelette").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Chocolate Profiteroles").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2022-03-12 17:20:10", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Shrimp Ceviche").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-03-27 21:55:45", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Italian Antipasto").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Red Berry Cheesecake").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-04-05 09:10:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Cheeseburger").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2022-05-15 14:30:40", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(3).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-06-20 20:45:25", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Cheeseburger").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2022-07-12 18:10:55", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Raspberry Soda").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Espresso Coffee").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2022-08-10 17:30:15", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2022-09-02 13:20:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-10-19 19:05:45", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Chocolate Profiteroles").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2022-11-03 11:15:40", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-12-28 21:10:50", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-12-28 21:10:50", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2022-01-20 13:30:10", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-02-14 17:45:25", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2022-03-05 12:20:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Shrimp Ceviche").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2022-04-22 18:10:55", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-05-19 14:25:45", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Chocolate Profiteroles").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2022-06-08 10:35:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Shrimp Ceviche").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(3).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2022-07-15 20:15:40", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-08-10 15:30:35", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2022-09-18 19:05:45", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2022-10-27 12:45:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Red Berry Cheesecake").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-11-03 11:15:40", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-12-28 21:10:50", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2022-12-10 08:20:15", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Cheeseburger").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Shrimp Ceviche").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2022-12-15 12:30:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2022-12-20 17:45:25", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Chocolate Profiteroles").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2022-12-25 19:20:10", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2022-12-30 22:30:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Italian Antipasto").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2022-12-31 23:59:59", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Red Berry Cheesecake").get()).quantity(3).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2021-02-14 13:45:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2021-05-20 16:30:15", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Shrimp Ceviche").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2021-08-11 10:15:22", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Chocolate Profiteroles").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2021-11-25 20:55:45", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Cheeseburger").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2021-12-31 23:59:59", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Italian Antipasto").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2021-03-15 14:30:40", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Raspberry Soda").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2021-06-20 19:45:25", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Spaghetti Carbonara").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(3).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2021-07-12 17:10:55", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Cheeseburger").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2021-10-10 10:30:15", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2021-12-24 20:10:50", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2021-03-15 14:30:40", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Raspberry Soda").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2021-06-20 19:45:25", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Spaghetti Carbonara").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2021-07-12 17:10:55", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Cheeseburger").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2021-10-10 10:30:15", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2021-12-24 20:10:50", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Cheeseburger").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2020-08-01 14:30:40", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Raspberry Soda").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2020-08-12 19:45:25", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Spaghetti Carbonara").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2020-09-01 17:10:55", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Cheeseburger").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2020-09-15 10:30:15", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2020-10-02 20:10:50", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(3).build(), ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Cheeseburger").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2020-11-10 11:15:40", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2020-12-05 19:30:35", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Raspberry Soda").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2020-08-02 14:20:30", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Cheeseburger").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Mango Mousse").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2020-08-10 20:05:45", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Chocolate Profiteroles").get()).quantity(2).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2020-08-19 11:15:40", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Lemon Mojito").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "martinguido@gmail.com", "2020-08-24 16:30:25", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Margherita Pizza").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Raspberry Soda").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Espresso Coffee").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "john.smith@example.com", "2020-09-05 19:45:50", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Veal Milanese with Fries").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Tiramisu").get()).quantity(1).build()));
        generateOrderRequestDTO(orderService, "sarah.jones@example.com", "2020-10-15 17:20:40", List.of(ProductOrderDTO.builder().product(productRepository.findByName("Shrimp Ceviche").get()).quantity(1).build(), ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(2).build(), ProductOrderDTO.builder().product(productRepository.findByName("Italian Antipasto").get()).quantity(1).build()));
    }


    private void generateOrderRequestDTO(OrderService orderService, String userEmail, String stringDate, List<ProductOrderDTO> productOrderDTO) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(stringDate);
        orderService.addNewOrder(OrderRequestDTO.builder().registeredEmployeeEmail(userEmail).dateCreated(date).productOrders(productOrderDTO).build());
    }

    private void addSampleEmployees(AuthenticationService authenticationService, UserRepository userRepository) {
        RegisterRequestDTO user00 = RegisterRequestDTO.builder().username("martinguido0").email("guidomartin7@gmail.com").password("Password1").build();
        authenticationService.register(user00);
        User user00Created = userRepository.findByEmail(user00.getEmail()).get();
        user00Created.setRole(Role.VALIDATEDEMPLOYEE);
        userRepository.save(user00Created);
        RegisterRequestDTO user0 = RegisterRequestDTO.builder().username("martinguido").email("martinguido@gmail.com").password("Password1").build();
        authenticationService.register(user0);
        User user0Created = userRepository.findByEmail(user0.getEmail()).get();
        user0Created.setRole(Role.MANAGER);
        userRepository.save(user0Created);
        RegisterRequestDTO user1 = RegisterRequestDTO.builder().username("john_smith").email("john.smith@example.com").password("Password1").build();
        authenticationService.register(user1);
        User user1Created = userRepository.findByEmail(user1.getEmail()).get();
        user1Created.setRole(Role.VALIDATEDEMPLOYEE);
        userRepository.save(user1Created);
        RegisterRequestDTO user2 = RegisterRequestDTO.builder().username("sarah_jones").email("sarah.jones@example.com").password("Password1").build();
        authenticationService.register(user2);
        User user2Created = userRepository.findByEmail(user2.getEmail()).get();
        user2Created.setRole(Role.VALIDATEDEMPLOYEE);
        userRepository.save(user2Created);
        RegisterRequestDTO user3 = RegisterRequestDTO.builder().username("michael_davis").email("michael.davis@example.com").password("Password1").build();
        authenticationService.register(user3);
        RegisterRequestDTO user4 = RegisterRequestDTO.builder().username("emily_wilson").email("emily.wilson@example.com").password("Password1").build();
        authenticationService.register(user4);
        RegisterRequestDTO user5 = RegisterRequestDTO.builder().username("david_johnson").email("david.johnson@example.com").password("Password1").build();
        authenticationService.register(user5);
        RegisterRequestDTO user6 = RegisterRequestDTO.builder().username("jane_doe").email("jane.doe@example.com").password("Password1").build();
        authenticationService.register(user6);
    }

    private void addSampleCategories(CategoryService categoryService) {
        CategoryRequestDTO categoryRequestDTO1 = CategoryRequestDTO.builder().categoryName("Starter").build();
        categoryService.addCategory(categoryRequestDTO1);
        CategoryRequestDTO categoryRequestDTO2 = CategoryRequestDTO.builder().categoryName("Main Course").build();
        categoryService.addCategory(categoryRequestDTO2);
        CategoryRequestDTO categoryRequestDTO3 = CategoryRequestDTO.builder().categoryName("Dessert").build();
        categoryService.addCategory(categoryRequestDTO3);
        CategoryRequestDTO categoryRequestDTO4 = CategoryRequestDTO.builder().categoryName("Drink").build();
        categoryService.addCategory(categoryRequestDTO4);
    }

    private void addSampleProducts(ProductService productService) {
        ProductDTO starter1 = ProductDTO.builder().name("Tomato Bruschetta").description("Toasted bread with fresh tomato, garlic, and basil").category("Starter").unitPrice(5500.00).stock(40).unitCost(1000.00).newCategory(false).build();
        productService.addProduct(starter1);
        ProductDTO starter2 = ProductDTO.builder().name("Fried Calamari").description("Crispy calamari served with lemon sauce").category("Starter").unitPrice(6800.00).stock(27).unitCost(2500.00).newCategory(false).build();
        productService.addProduct(starter2);
        ProductDTO starter3 = ProductDTO.builder().name("Spanish Omelette").description("Potato and onion omelette with eggs").category("Starter").unitPrice(6200.00).stock(48).unitCost(2000.00).newCategory(false).build();
        productService.addProduct(starter3);
        ProductDTO starter4 = ProductDTO.builder().name("Shrimp Ceviche").description("Fresh shrimp ceviche with lime and cilantro").category("Starter").unitPrice(7500.00).stock(26).unitCost(3500.00).newCategory(false).build();
        productService.addProduct(starter4);
        ProductDTO starter5 = ProductDTO.builder().name("Italian Antipasto").description("Selection of cold cuts, cheeses, and olives").category("Starter").unitPrice(8900.00).stock(20).unitCost(5000.00).newCategory(false).build();
        productService.addProduct(starter5);
        ProductDTO productDTO1 = ProductDTO.builder().name("Veal Milanese with Fries").description("Veal milanese with french fries").category("Main Course").unitPrice(4500.00).stock(45).unitCost(4000.00).newCategory(false).build();
        productService.addProduct(productDTO1);
        ProductDTO productDTO2 = ProductDTO.builder().name("Margherita Pizza").description("Pizza with tomato, mozzarella, and basil").category("Main Course").unitPrice(8500.00).stock(80).unitCost(7000.00).newCategory(false).build();
        productService.addProduct(productDTO2);
        ProductDTO productDTO3 = ProductDTO.builder().name("Cheeseburger").description("Beef burger with cheddar cheese").category("Main Course").unitPrice(6500.00).stock(80).unitCost(1500.00).newCategory(false).build();
        productService.addProduct(productDTO3);
        ProductDTO productDTO4 = ProductDTO.builder().name("Caesar Salad").description("Lettuce, grilled chicken, croutons, and Caesar dressing").category("Main Course").unitPrice(7500.00).stock(75).unitCost(3500.00).newCategory(false).build();
        productService.addProduct(productDTO4);
        ProductDTO productDTO5 = ProductDTO.builder().name("Assorted Sushi").description("Assorted sushi with nigiri, sashimi, and rolls").category("Main Course").unitPrice(9500.00).stock(87).unitCost(7500.00).newCategory(false).build();
        productService.addProduct(productDTO5);
        ProductDTO productDTO6 = ProductDTO.builder().name("Spaghetti Carbonara").description("Spaghetti with egg, pancetta, and parmesan cheese").category("Main Course").unitPrice(7800.00).stock(68).unitCost(7000.00).newCategory(false).build();
        productService.addProduct(productDTO6);
        ProductDTO dessert1 = ProductDTO.builder().name("Tiramisu").description("Italian coffee and mascarpone cake").category("Dessert").unitPrice(7600.00).stock(65).unitCost(5500.00).newCategory(false).build();
        productService.addProduct(dessert1);
        ProductDTO dessert2 = ProductDTO.builder().name("Red Berry Cheesecake").description("Cheesecake with red berry sauce").category("Dessert").unitPrice(6900.00).stock(62).unitCost(5500.00).newCategory(false).build();
        productService.addProduct(dessert2);
        ProductDTO dessert3 = ProductDTO.builder().name("Chocolate Profiteroles").description("Pastries filled with chocolate cream and covered in chocolate").category("Dessert").unitPrice(8200.00).stock(88).unitCost(8000.00).newCategory(false).build();
        productService.addProduct(dessert3);
        ProductDTO dessert4 = ProductDTO.builder().name("Caramel Flan").description("Homemade flan with caramel sauce").category("Dessert").unitPrice(6300.00).stock(82).unitCost(6000.00).newCategory(false).build();
        productService.addProduct(dessert4);
        ProductDTO dessert5 = ProductDTO.builder().name("Mango Mousse").description("Fresh mango mousse with tropical fruits").category("Dessert").unitPrice(7100.00).stock(74).unitCost(6000.00).newCategory(false).build();
        productService.addProduct(dessert5);
        ProductDTO drink1 = ProductDTO.builder().name("Lemon Mojito").description("Rum, lime, sugar, and mint cocktail").category("Drink").unitPrice(7500.00).stock(85).unitCost(6000.00).newCategory(false).build();
        productService.addProduct(drink1);
        ProductDTO drink2 = ProductDTO.builder().name("Craft Beer").description("Local craft beer on tap").category("Drink").unitPrice(6800.00).stock(80).unitCost(5000.00).newCategory(false).build();
        productService.addProduct(drink2);
        ProductDTO drink3 = ProductDTO.builder().name("Raspberry Soda").description("Chilled raspberry soda").category("Drink").unitPrice(4900.00).stock(95).unitCost(2000.00).newCategory(false).build();
        productService.addProduct(drink3);
        ProductDTO drink4 = ProductDTO.builder().name("Espresso Coffee").description("Italian espresso coffee").category("Drink").unitPrice(3600.00).stock(89).unitCost(2500.00).newCategory(false).build();
        productService.addProduct(drink4);
        ProductDTO drink5 = ProductDTO.builder().name("Green Tea").description("Green tea with mint and honey").category("Drink").unitPrice(4200.00).stock(78).unitCost(3500.00).newCategory(false).build();
        productService.addProduct(drink5);
    }
}
