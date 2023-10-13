package com.messismo.bar.Configurations;

import com.messismo.bar.DTOs.*;
import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InitialConfiguration {

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService authenticationService, UserRepository userRepository, ProductService productService, CategoryService categoryService, OrderService orderService, ProductRepository productRepository) {
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
        };
    }

    private void addSampleOrders(OrderService orderService, ProductRepository productRepository) throws ParseException {
        String userEmail1 = "john.smith@example.com";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = dateFormat.parse("2023-08-04 08:10:43");
        Double totalPrice1 = 17800.00;
        Double totalCost1= 4500.00;
        List<ProductOrderDTO> productOrderDTOList1 = new ArrayList<>();
        ProductOrderDTO productOrderDTO1 = ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(2).build();
        ProductOrderDTO productOrderDTO2 = ProductOrderDTO.builder().product(productRepository.findByName("Fried Calamari").get()).quantity(1).build();
        productOrderDTOList1.add(productOrderDTO1);
        productOrderDTOList1.add(productOrderDTO2);
        OrderRequestDTO orderRequestDTO1 = OrderRequestDTO.builder().productOrders(productOrderDTOList1).dateCreated(date1).registeredEmployeeEmail(userEmail1).totalPrice(totalPrice1).totalCost(totalCost1).build();
        orderService.addNewOrder(orderRequestDTO1);
        String userEmail2 = "martinguido@gmail.com";
        Date date2 = dateFormat.parse("2023-09-25 05:02:23");
        Double totalPrice2 = 44300.00;
        Double totalCost2 = 33500.00;
        List<ProductOrderDTO> productOrderDTOList2 = new ArrayList<>();
        ProductOrderDTO productOrderDTO3 = ProductOrderDTO.builder().product(productRepository.findByName("Tomato Bruschetta").get()).quantity(1).build(); // 1000
        ProductOrderDTO productOrderDTO4 = ProductOrderDTO.builder().product(productRepository.findByName("Caramel Flan").get()).quantity(2).build(); // 12000
        ProductOrderDTO productOrderDTO5 = ProductOrderDTO.builder().product(productRepository.findByName("Green Tea").get()).quantity(3).build(); // 10500
        ProductOrderDTO productOrderDTO6 = ProductOrderDTO.builder().product(productRepository.findByName("Craft Beer").get()).quantity(2).build(); // 10000
        productOrderDTOList2.add(productOrderDTO3);
        productOrderDTOList2.add(productOrderDTO4);
        productOrderDTOList2.add(productOrderDTO5);
        productOrderDTOList2.add(productOrderDTO6);
        OrderRequestDTO orderRequestDTO2 = OrderRequestDTO.builder().productOrders(productOrderDTOList2).dateCreated(date2).registeredEmployeeEmail(userEmail2).totalPrice(totalPrice2).totalCost(totalCost2).build();
        orderService.addNewOrder(orderRequestDTO2);
        orderService.getAllOrders();
    }


    private void addSampleEmployees(AuthenticationService authenticationService, UserRepository userRepository) {
        RegisterRequestDTO user00 = RegisterRequestDTO.builder().username("martinguido0").email("guidomartin7@gmail.com").password("Password1").build();
        authenticationService.register(user00);
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
        ProductDTO starter1 = ProductDTO.builder().name("Tomato Bruschetta").description("Toasted bread with fresh tomato, garlic, and basil").category("Starter").unitPrice(5500.00).stock(20).unitCost(1000.00).build();
        productService.addProduct(starter1);
        ProductDTO starter2 = ProductDTO.builder().name("Fried Calamari").description("Crispy calamari served with lemon sauce").category("Starter").unitPrice(6800.00).stock(15).unitCost(2500.00).build();
        productService.addProduct(starter2);
        ProductDTO starter3 = ProductDTO.builder().name("Spanish Omelette").description("Potato and onion omelette with eggs").category("Starter").unitPrice(6200.00).stock(18).unitCost(2000.00).build();
        productService.addProduct(starter3);
        ProductDTO starter4 = ProductDTO.builder().name("Shrimp Ceviche").description("Fresh shrimp ceviche with lime and cilantro").category("Starter").unitPrice(7500.00).stock(12).unitCost(3500.00).build();
        productService.addProduct(starter4);
        ProductDTO starter5 = ProductDTO.builder().name("Italian Antipasto").description("Selection of cold cuts, cheeses, and olives").category("Starter").unitPrice(8900.00).stock(10).unitCost(5000.00).build();
        productService.addProduct(starter5);
        ProductDTO productDTO1 = ProductDTO.builder().name("Veal Milanese with Fries").description("Veal milanese with french fries").category("Main Course").unitPrice(4500.00).stock(25).unitCost(4000.00).build();
        productService.addProduct(productDTO1);
        ProductDTO productDTO2 = ProductDTO.builder().name("Margherita Pizza").description("Pizza with tomato, mozzarella, and basil").category("Main Course").unitPrice(8500.00).stock(30).unitCost(7000.00).build();
        productService.addProduct(productDTO2);
        ProductDTO productDTO3 = ProductDTO.builder().name("Cheeseburger").description("Beef burger with cheddar cheese").category("Main Course").unitPrice(6500.00).stock(20).unitCost(1500.00).build();
        productService.addProduct(productDTO3);
        ProductDTO productDTO4 = ProductDTO.builder().name("Caesar Salad").description("Lettuce, grilled chicken, croutons, and Caesar dressing").category("Main Course").unitPrice(7500.00).stock(15).unitCost(3500.00).build();
        productService.addProduct(productDTO4);
        ProductDTO productDTO5 = ProductDTO.builder().name("Assorted Sushi").description("Assorted sushi with nigiri, sashimi, and rolls").category("Main Course").unitPrice(9500.00).stock(40).unitCost(7500.00).build();
        productService.addProduct(productDTO5);
        ProductDTO productDTO6 = ProductDTO.builder().name("Spaghetti Carbonara").description("Spaghetti with egg, pancetta, and parmesan cheese").category("Main Course").unitPrice(7800.00).stock(18).unitCost(7000.00).build();
        productService.addProduct(productDTO6);
        ProductDTO dessert1 = ProductDTO.builder().name("Tiramisu").description("Italian coffee and mascarpone cake").category("Dessert").unitPrice(7600.00).stock(15).unitCost(5500.00).build();
        productService.addProduct(dessert1);
        ProductDTO dessert2 = ProductDTO.builder().name("Red Berry Cheesecake").description("Cheesecake with red berry sauce").category("Dessert").unitPrice(6900.00).stock(12).unitCost(5500.00).build();
        productService.addProduct(dessert2);
        ProductDTO dessert3 = ProductDTO.builder().name("Chocolate Profiteroles").description("Pastries filled with chocolate cream and covered in chocolate").category("Dessert").unitPrice(8200.00).stock(18).unitCost(8000.00).build();
        productService.addProduct(dessert3);
        ProductDTO dessert4 = ProductDTO.builder().name("Caramel Flan").description("Homemade flan with caramel sauce").category("Dessert").unitPrice(6300.00).stock(20).unitCost(6000.00).build();
        productService.addProduct(dessert4);
        ProductDTO dessert5 = ProductDTO.builder().name("Mango Mousse").description("Fresh mango mousse with tropical fruits").category("Dessert").unitPrice(7100.00).stock(14).unitCost(6000.00).build();
        productService.addProduct(dessert5);
        ProductDTO drink1 = ProductDTO.builder().name("Lemon Mojito").description("Rum, lime, sugar, and mint cocktail").category("Drink").unitPrice(7500.00).stock(25).unitCost(6000.00).build();
        productService.addProduct(drink1);
        ProductDTO drink2 = ProductDTO.builder().name("Craft Beer").description("Local craft beer on tap").category("Drink").unitPrice(6800.00).stock(30).unitCost(5000.00).build();
        productService.addProduct(drink2);
        ProductDTO drink3 = ProductDTO.builder().name("Raspberry Soda").description("Chilled raspberry soda").category("Drink").unitPrice(4900.00).stock(35).unitCost(2000.00).build();
        productService.addProduct(drink3);
        ProductDTO drink4 = ProductDTO.builder().name("Espresso Coffee").description("Italian espresso coffee").category("Drink").unitPrice(3600.00).stock(40).unitCost(2500.00).build();
        productService.addProduct(drink4);
        ProductDTO drink5 = ProductDTO.builder().name("Green Tea").description("Green tea with mint and honey").category("Drink").unitPrice(4200.00).stock(28).unitCost(3500.00).build();
        productService.addProduct(drink5);
    }
}
