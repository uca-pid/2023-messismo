package com.messismo.bar.Services;

import com.messismo.bar.DTOs.DashboardRequestDTO;
import com.messismo.bar.Entities.Order;
import com.messismo.bar.Repositories.CategoryRepository;
import com.messismo.bar.Repositories.OrderRepository;
import com.messismo.bar.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    public ResponseEntity<?> getDashboardInformation(DashboardRequestDTO dashboardRequestDTO) {
        return null;
    }

    public ResponseEntity<?> getTotalInfo() {
        try {
            List<Order> allOrders = orderRepository.findAll();
            double totalEarnings = 0.00;
            for(Order order : allOrders){
                totalEarnings= totalEarnings + order.getTotalPrice();
            }
            HashMap<String, Object> response = new HashMap<>();
            response.put("totalSalesInEarnings", totalEarnings);
            response.put("totalOrdersQuantity", allOrders.size());
            response.put("totalProducts", productRepository.findAll().size());
            response.put("totalCategories", categoryRepository.findAll().size());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT get total information for dashboards right now. ");
        }
    }
}
