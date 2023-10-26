package com.messismo.bar.Services;

import com.messismo.bar.DTOs.DashboardRequestDTO;
import com.messismo.bar.DTOs.ThresholdDTO;
import com.messismo.bar.Entities.Category;
import com.messismo.bar.Entities.Order;
import com.messismo.bar.Entities.Product;
import com.messismo.bar.Entities.ProductOrder;
import com.messismo.bar.Repositories.CategoryRepository;
import com.messismo.bar.Repositories.OrderRepository;
import com.messismo.bar.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderRepository orderRepository;

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    public ResponseEntity<?> getTotalInfo() {
        try {
            List<Order> allOrders = orderRepository.findAll();
            double closedEarnings = 0.00;
            double openEarnings = 0.00;
            double totalEarnings = 0.00;
            for (Order order : allOrders) {
                if (Objects.equals(order.getStatus(), "Closed")) {
                    closedEarnings += (order.getTotalPrice() - order.getTotalCost());
                } else {
                    openEarnings += (order.getTotalPrice() - order.getTotalCost());
                }
                totalEarnings += (order.getTotalPrice() - order.getTotalCost());
            }
            HashMap<String, Object> response = new HashMap<>();
            response.put("totalSalesInEarnings", totalEarnings);
            response.put("openSalesInEarnings", openEarnings);
            response.put("closedSalesInEarnings", closedEarnings);
            response.put("totalOrdersQuantity", allOrders.size());
            response.put("openOrdersQuantity", orderRepository.findByStatus("Open").size());
            response.put("closedOrdersQuantity", orderRepository.findByStatus("Closed").size());
            response.put("totalProducts", productRepository.findAll().size());
            response.put("totalCategories", categoryRepository.findAll().size());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CANNOT get total information for dashboards right now.");
        }
    }

    public ResponseEntity<?> getProductStock(ThresholdDTO thresholdDTO) {
        try {
            if (thresholdDTO.getMinStock() == null || thresholdDTO.getMinStock() < 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Some values cannot be less than zero. Please check.");
            }
            HashMap<String, List<Product>> response = new HashMap<>();
            List<Product> allProducts = productRepository.findAll();
            List<Product> productsNearEndStock = new ArrayList<>();
            List<Product> productsWithStockAboveTreshold = new ArrayList<>();
            for (Product product : allProducts) {
                if (product.getStock() <= thresholdDTO.getMinStock()) {
                    productsNearEndStock.add(product);
                } else {
                    productsWithStockAboveTreshold.add(product);
                }
            }
            response.put("belowThreshold", productsNearEndStock);
            response.put("aboveThreshold", productsWithStockAboveTreshold);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CANNOT get product stock information for dashboards right now.");
        }
    }

    public ResponseEntity<?> getDashboardInformation(DashboardRequestDTO dashboardRequestDTO) {
        try {
            HashMap<String, Object> response = new HashMap<>();
            if (dashboardRequestDTO.getDateRequested() == null || dashboardRequestDTO.getDateRequested().isEmpty()) {
                response = getYearlyInformation();
            } else if (dashboardRequestDTO.getDateRequested().matches("\\d{4}")) {
                response = getMonthlyInformation(dashboardRequestDTO.getDateRequested());
            } else if (dashboardRequestDTO.getDateRequested().matches("\\d{4}-\\d{2}-\\d{2}")) {
                response = getWeeklyInformation(dashboardRequestDTO.getDateRequested());
            } else if (dashboardRequestDTO.getDateRequested().matches("\\d{4}-\\d{2}")) {
                response = getDailyInformation(dashboardRequestDTO.getDateRequested());
            } else if (dashboardRequestDTO.getDateRequested().equals("historic")) {
                response = getHistoricInformation();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Incorrect date format");
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CANNOT get information for dashboards right now.");
        }
    }

    private HashMap<String, Object> getYearlyInformation() { // DESDE AÑO INICIAL HASTA AÑO ACTUAL
        List<Order> allOrders = orderRepository.findAll();
        LocalDate minDate = allOrders.stream()
                .map(order -> order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .min(Comparator.naturalOrder()).orElse(LocalDate.now());
        LocalDate maxDate = allOrders.stream()
                .map(order -> order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .max(Comparator.naturalOrder()).orElse(LocalDate.now());
        List<Integer> years = new ArrayList<>();
        LocalDate currentDate = minDate;
        while (!currentDate.isAfter(maxDate)) {
            years.add(currentDate.getYear());
            currentDate = currentDate.plusYears(1);
        }
        TreeMap<Integer, Integer> ordersByYearQuantity = new TreeMap<>();
        TreeMap<Integer, Double> ordersByYearEarnings = new TreeMap<>();
        TreeMap<Integer, Double> averageByOrder = new TreeMap<>();
        for (Integer year : years) {
            ordersByYearQuantity.put(year, 0);
            ordersByYearEarnings.put(year, 0.0);
        }
        for (Order order : allOrders) {
            LocalDate orderDate = order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = orderDate.getYear();
            ordersByYearQuantity.put(year, ordersByYearQuantity.get(year) + 1);
            double earnings = ordersByYearEarnings.get(year) + (order.getTotalPrice() - order.getTotalCost());
            ordersByYearEarnings.put(year, earnings);
        }
        for (Integer year : years) {
            int quantity = ordersByYearQuantity.get(year);
            double earnings = ordersByYearEarnings.get(year);
            double average = quantity > 0 ? earnings / quantity : 0.0;
            average = (double) Math.round(average * 100.00) / 100.00;
            averageByOrder.put(year, average);
        }
        HashMap<String, Object> response = new HashMap<>();
        response.put("orderByQuantity", ordersByYearQuantity);
        response.put("orderByEarnings", ordersByYearEarnings);
        response.put("averageByOrder", averageByOrder);
        response.put("quantityProductDonut", getQuantityProductDonut(allOrders));
        response.put("earningProductDonut", getEarningProductDonut(allOrders));
        response.put("quantityCategoryDonut", getQuantityCategoryDonut(allOrders));
        response.put("earningCategoryDonut", getEarningCategoryDonut(allOrders));
        response.put("labels", new ArrayList<>(years));
        return response;
    }

    public HashMap<String, Object> getDailyInformation(String dateRequested) { // ESE MES DESDE DIA 1 HASTA UN MES MAS
        List<Order> allOrders = orderRepository.findAll();
        List<Order> filteredOrders = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate requestedDate = LocalDate.parse(dateRequested + "-01", formatter);
        int year = requestedDate.getYear();
        int month = requestedDate.getMonthValue();
        int lastDayOfMonth = requestedDate.lengthOfMonth();
        TreeMap<String, Integer> orderByDayQuantity = new TreeMap<>();
        TreeMap<String, Double> orderByDayEarnings = new TreeMap<>();
        TreeMap<String, Double> averageByOrder = new TreeMap<>();
        for (int day = 1; day <= lastDayOfMonth; day++) {
            LocalDate currentDate = LocalDate.of(year, month, day);
            String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd"));
            orderByDayQuantity.put(formattedDate, 0);
            orderByDayEarnings.put(formattedDate, 0.0);
        }
        for (Order order : allOrders) {
            LocalDate orderDate = order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (orderDate.getYear() == year && orderDate.getMonthValue() == month) {
                filteredOrders.add(order);
                String formattedDate = orderDate.format(DateTimeFormatter.ofPattern("dd"));
                orderByDayQuantity.put(formattedDate, orderByDayQuantity.get(formattedDate) + 1);
                double currentEarnings = orderByDayEarnings.get(formattedDate)
                        + (order.getTotalPrice() - order.getTotalCost());
                orderByDayEarnings.put(formattedDate, currentEarnings);
            }
        }
        for (String day : orderByDayEarnings.keySet()) {
            double earnings = orderByDayEarnings.get(day);
            int quantity = orderByDayQuantity.get(day);
            double average = quantity > 0 ? earnings / quantity : 0.0;
            average = (double) Math.round(average * 100.00) / 100.00;
            averageByOrder.put(day, average);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("orderByEarnings", orderByDayEarnings);
        result.put("orderByQuantity", orderByDayQuantity);
        result.put("averageByOrder", averageByOrder);
        result.put("quantityProductDonut", getQuantityProductDonut(filteredOrders));
        result.put("earningProductDonut", getEarningProductDonut(filteredOrders));
        result.put("quantityCategoryDonut", getQuantityCategoryDonut(filteredOrders));
        result.put("earningCategoryDonut", getEarningCategoryDonut(filteredOrders));
        result.put("labels", new ArrayList<>(orderByDayEarnings.keySet()));
        return result;
    }

    public HashMap<String, Object> getWeeklyInformation(String dateRequested) { // ESE DIA HASTA UNA SEMANA MAS
        List<Order> filteredOrders = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate requestedDate = LocalDate.parse(dateRequested, formatter);
        LocalDate endDate = requestedDate.plusDays(6);
        Map<String, Integer> orderByDateQuantity = new LinkedHashMap<>();
        Map<String, Double> orderByDateEarnings = new LinkedHashMap<>();
        Map<String, Double> averageByOrder = new LinkedHashMap<>();
        LocalDate currentDate = requestedDate;
        while (!currentDate.isAfter(endDate)) {
            String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd/MM"));
            orderByDateQuantity.put(formattedDate, 0);
            orderByDateEarnings.put(formattedDate, 0.0);
            currentDate = currentDate.plusDays(1);
        }
        List<Order> allOrders = orderRepository.findAll();
        for (Order order : allOrders) {
            LocalDate orderDate = order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!orderDate.isBefore(requestedDate) && !orderDate.isAfter(endDate)) {
                filteredOrders.add(order);
                String formattedDate = orderDate.format(DateTimeFormatter.ofPattern("dd/MM"));
                orderByDateQuantity.put(formattedDate, orderByDateQuantity.get(formattedDate) + 1);
                double currentEarnings = orderByDateEarnings.get(formattedDate)
                        + (order.getTotalPrice() - order.getTotalCost());
                orderByDateEarnings.put(formattedDate, currentEarnings);
            }
        }
        for (String day : orderByDateEarnings.keySet()) {
            double earnings = orderByDateEarnings.get(day);
            int quantity = orderByDateQuantity.get(day);
            double average = quantity > 0 ? earnings / quantity : 0.0;
            average = (double) Math.round(average * 100.00) / 100.00;
            averageByOrder.put(day, average);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("orderByEarnings", orderByDateEarnings);
        result.put("orderByQuantity", orderByDateQuantity);
        result.put("averageByOrder", averageByOrder);
        result.put("quantityProductDonut", getQuantityProductDonut(filteredOrders));
        result.put("earningProductDonut", getEarningProductDonut(filteredOrders));
        result.put("quantityCategoryDonut", getQuantityCategoryDonut(filteredOrders));
        result.put("earningCategoryDonut", getEarningCategoryDonut(filteredOrders));
        result.put("labels", new ArrayList<>(orderByDateEarnings.keySet()));
        return result;
    }

    public HashMap<String, Object> getMonthlyInformation(String yearRequested) { // ESE AÑO HASTA UN AÑO MAS
        List<Order> filteredOrders = new ArrayList<>();
        List<Order> allOrders = orderRepository.findAll();
        List<String> labels = List.of("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        int year = Integer.parseInt(yearRequested);
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year + 1, 1, 1);
        TreeMap<String, Integer> ordersByMonthQuantity = new TreeMap<>();
        TreeMap<String, Double> ordersByMonthEarnings = new TreeMap<>();
        TreeMap<String, Double> averageByOrder = new TreeMap<>();
        for (String label : labels) {
            ordersByMonthQuantity.put(label, 0);
            ordersByMonthEarnings.put(label, 0.00);
        }
        for (Order order : allOrders) {
            LocalDate orderDate = order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (orderDate.isAfter(startDate) && orderDate.isBefore(endDate)) {
                filteredOrders.add(order);
                String month = String.format("%02d", orderDate.getMonthValue());
                ordersByMonthQuantity.put(month, ordersByMonthQuantity.get(month) + 1);
                ordersByMonthEarnings.put(month,
                        ordersByMonthEarnings.get(month) + (order.getTotalPrice() - order.getTotalCost()));
            }
        }
        for (String month : labels) {
            int quantity = ordersByMonthQuantity.get(month);
            double earnings = ordersByMonthEarnings.get(month);
            double average = quantity > 0 ? earnings / quantity : 0.0;
            average = (double) Math.round(average * 100.00) / 100.00;
            averageByOrder.put(month, average);
        }
        HashMap<String, Object> response = new HashMap<>();
        response.put("orderByQuantity", ordersByMonthQuantity);
        response.put("orderByEarnings", ordersByMonthEarnings);
        response.put("averageByOrder", averageByOrder);
        response.put("quantityProductDonut", getQuantityProductDonut(filteredOrders));
        response.put("earningProductDonut", getEarningProductDonut(filteredOrders));
        response.put("quantityCategoryDonut", getQuantityCategoryDonut(filteredOrders));
        response.put("earningCategoryDonut", getEarningCategoryDonut(filteredOrders));
        response.put("labels", labels);
        return response;
    }

    public HashMap<String, Object> getHistoricInformation() { // DESDE PRIMER ORDEN HASTA AHORA
        List<Order> allOrders = orderRepository.findAll();
        LocalDate minDate = allOrders.stream()
                .map(order -> order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .min(Comparator.naturalOrder()).orElse(LocalDate.now());
        LocalDate maxDate = allOrders.stream()
                .map(order -> order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .max(Comparator.naturalOrder()).orElse(LocalDate.now());
        List<String> labels = new ArrayList<>();
        LocalDate currentDate = minDate;
        while (!currentDate.isAfter(maxDate)) {
            String label = currentDate.format(DateTimeFormatter.ofPattern("MM/yy"));
            labels.add(label);
            currentDate = currentDate.plusMonths(1);
        }
        TreeMap<String, Integer> ordersByMonthQuantity = new TreeMap<>();
        TreeMap<String, Double> ordersByMonthEarnings = new TreeMap<>();
        TreeMap<String, Double> averageByOrder = new TreeMap<>();
        for (String label : labels) {
            ordersByMonthQuantity.put(label, 0);
            ordersByMonthEarnings.put(label, 0.0);
        }
        for (Order order : allOrders) {
            LocalDate orderDate = order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String monthYearLabel = orderDate.format(DateTimeFormatter.ofPattern("MM/yy"));
            int currentOrderCount = ordersByMonthQuantity.getOrDefault(monthYearLabel, 0);
            double currentEarnings = ordersByMonthEarnings.getOrDefault(monthYearLabel, 0.0);
            ordersByMonthQuantity.put(monthYearLabel, currentOrderCount + 1);
            ordersByMonthEarnings.put(monthYearLabel, currentEarnings + (order.getTotalPrice()) - order.getTotalCost());
        }
        for (String label : labels) {
            int quantity = ordersByMonthQuantity.get(label);
            double earnings = ordersByMonthEarnings.get(label);
            double average = quantity > 0 ? earnings / quantity : 0.0;
            average = (double) Math.round(average * 100.00) / 100.00;
            averageByOrder.put(label, average);
        }
        HashMap<String, Object> response = new HashMap<>();
        response.put("orderByQuantity", ordersByMonthQuantity);
        response.put("orderByEarnings", ordersByMonthEarnings);
        response.put("averageByOrder", averageByOrder);
        response.put("quantityProductDonut", getQuantityProductDonut(allOrders));
        response.put("earningProductDonut", getEarningProductDonut(allOrders));
        response.put("quantityCategoryDonut", getQuantityCategoryDonut(allOrders));
        response.put("earningCategoryDonut", getEarningCategoryDonut(allOrders));
        response.put("labels", labels);
        return response;
    }

    public HashMap<String, Object> getEarningCategoryDonut(List<Order> orders) {
        HashMap<String, Object> categoryEarningsMap = new HashMap<>();
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            String categoryName = category.getName();
            double categoryEarnings = 0.0;
            for (Order order : orders) {
                for (ProductOrder productOrder : order.getProductOrders()) {
                    Product product = productOrder.getProduct();
                    if (product.getCategory().equals(category)) {
                        categoryEarnings += productOrder.getQuantity()
                                * (product.getUnitPrice() - product.getUnitCost());
                    }
                }
            }
            categoryEarningsMap.put(categoryName, categoryEarnings);
        }
        return categoryEarningsMap;
    }

    public HashMap<String, Object> getQuantityCategoryDonut(List<Order> orders) {
        HashMap<String, Integer> categorySalesMap = new HashMap<>();
        for (Order order : orders) {
            for (ProductOrder productOrder : order.getProductOrders()) {
                Product product = productOrder.getProduct();
                Category category = product.getCategory();
                categorySalesMap.put(category.getName(),
                        categorySalesMap.getOrDefault(category.getName(), 0) + productOrder.getQuantity());
            }
        }
        return new HashMap<>(categorySalesMap);
    }

    public HashMap<String, Double> getEarningProductDonut(List<Order> orders) {
        HashMap<String, Double> productProfitsMap = new HashMap<>();
        for (Order order : orders) {
            for (ProductOrder productOrder : order.getProductOrders()) {
                Product product = productOrder.getProduct();
                double productProfit = (product.getUnitPrice() - product.getUnitCost()) * productOrder.getQuantity();
                productProfitsMap.put(product.getName(),
                        productProfitsMap.getOrDefault(product.getName(), 0.0) + productProfit);
            }
        }
        return productProfitsMap;
    }

    public HashMap<String, Integer> getQuantityProductDonut(List<Order> orders) {
        HashMap<String, Integer> productSalesMap = new HashMap<>();
        for (Order order : orders) {
            for (ProductOrder productOrder : order.getProductOrders()) {
                Product product = productOrder.getProduct();
                productSalesMap.put(product.getName(),
                        productSalesMap.getOrDefault(product.getName(), 0) + productOrder.getQuantity());
            }
        }
        return productSalesMap;
    }
}
