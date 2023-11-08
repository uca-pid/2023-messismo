package com.messismo.bar.Services;

import com.messismo.bar.DTOs.*;
import com.messismo.bar.Entities.*;
import com.messismo.bar.Exceptions.*;
import com.messismo.bar.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ProductOrderRepository productOrderRepository;

    public String addNewOrder(OrderRequestDTO orderRequestDTO) throws Exception {
        try {
            User employee = userRepository.findByEmail(orderRequestDTO.getRegisteredEmployeeEmail()).orElseThrow(() -> new UserNotFoundException("No user has that email"));
            NewProductOrderList newProductOrderList = createProductOrder(orderRequestDTO.getProductOrders());
            Order newOrder = new Order(employee, orderRequestDTO.getDateCreated(), newProductOrderList.getProductOrderList(), newProductOrderList.getTotalPrice(), newProductOrderList.getTotalCost());
            orderRepository.save(newOrder);
            return "Order created successfully";
        } catch (UserNotFoundException | ProductQuantityBelowAvailableStock e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("CANNOT create an order at the moment");
        }
    }

    public String closeOrder(OrderIdDTO orderIdDTO) throws Exception {
        try {
            Order order = orderRepository.findById(orderIdDTO.getOrderId()).orElseThrow(() -> new OrderNotFoundException("Order not found"));
            order.close();
            orderRepository.save(order);
            return "Order closed successfully";
        } catch (OrderNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("CANNOT close an order at the moment");
        }
    }

    public String modifyOrder(ModifyOrderDTO modifyOrderDTO) throws Exception {
        try {
            Order order = orderRepository.findById(modifyOrderDTO.getOrderId()).orElseThrow(() -> new OrderNotFoundException("Order not found"));
            NewProductOrderList newProductOrderList = createProductOrder(modifyOrderDTO.getProductOrders());
            order.updateProductOrders(newProductOrderList.getProductOrderList());
            order.updateTotalPrice(newProductOrderList.getTotalPrice());
            order.updateTotalCost(newProductOrderList.getTotalCost());
            orderRepository.save(order);
            return "Order modified successfully";
        } catch (ProductQuantityBelowAvailableStock | OrderNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("CANNOT modify this order at the moment");
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getAllOrdersBetweenTwoDates(Date startingDate, Date endingDate) {
        List<Order> allOrders = orderRepository.findAll();
        List<Order> filteredOrderByDate = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.getDateCreated().after(startingDate) && order.getDateCreated().before(endingDate)) {
                filteredOrderByDate.add(order);
            }
        }
        return filteredOrderByDate;
    }

    public NewProductOrderList createProductOrder(List<ProductOrderDTO> productOrderDTOList) throws ProductQuantityBelowAvailableStock {
        List<ProductOrder> productOrderList = new ArrayList<>();
        double totalPrice = 0.00;
        double totalCost = 0.00;
        for (ProductOrderDTO productOrderDTO : productOrderDTOList) {
            if (productOrderDTO.getProduct().getStock() < productOrderDTO.getQuantity()) {
                throw new ProductQuantityBelowAvailableStock("Not enough stock of a product");
            } else {
                Product product = productOrderDTO.getProduct();
                product.removeStock(productOrderDTO.getQuantity());
                productRepository.save(product);
                totalPrice += (product.getUnitPrice() * productOrderDTO.getQuantity());
                totalCost += (product.getUnitCost() * productOrderDTO.getQuantity());
                ProductOrder productOrder = new ProductOrder(product.getName(), product.getUnitPrice(), product.getUnitCost(), product.getCategory(), productOrderDTO.getQuantity());
                productOrderRepository.save(productOrder);
                productOrderList.add(productOrder);
            }
        }
        return NewProductOrderList.builder().productOrderList(productOrderList).totalCost(totalCost).totalPrice(totalPrice).build();
    }
}
