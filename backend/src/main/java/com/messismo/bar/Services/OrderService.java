package com.messismo.bar.Services;

import com.messismo.bar.DTOs.OrderRequestDTO;
import com.messismo.bar.DTOs.ProductOrderDTO;
import com.messismo.bar.Entities.Order;
import com.messismo.bar.Entities.Product;
import com.messismo.bar.Entities.ProductOrder;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.OrderRepository;
import com.messismo.bar.Repositories.ProductOrderRepository;
import com.messismo.bar.Repositories.ProductRepository;
import com.messismo.bar.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ProductOrderRepository productOrderRepository;

    public ResponseEntity<?> addNewOrder(OrderRequestDTO orderRequestDTO) {
        try {
            User employee = userRepository.findByEmail(orderRequestDTO.getRegisteredEmployeeEmail()).orElseThrow(() -> new Exception("Employee not found"));

            List<ProductOrder> productOrderList = new ArrayList<>();
            for (ProductOrderDTO productOrderDTO : orderRequestDTO.getProductOrders()) {
                if (productOrderDTO.getProduct().getStock() < productOrderDTO.getQuantity()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Not enough stock of a product");
                } else {
                    Product product = productOrderDTO.getProduct();
                    product.setStock(product.getStock() - productOrderDTO.getQuantity());
                    productRepository.save(product);
                    ProductOrder productOrder = ProductOrder.builder().product(product).quantity(productOrderDTO.getQuantity()).build();
                    productOrderRepository.save(productOrder);
                    productOrderList.add(productOrder);
                }
            }
            Order newOrder = Order.builder().productOrders(productOrderList).user(employee).dateCreated(orderRequestDTO.getDateCreated()).totalPrice(orderRequestDTO.getTotalPrice()).totalCost(orderRequestDTO.getTotalCost()).build();
            orderRepository.save(newOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT create an order at the moment." );
        }
    }

    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderRepository.findAll());
    }
}
