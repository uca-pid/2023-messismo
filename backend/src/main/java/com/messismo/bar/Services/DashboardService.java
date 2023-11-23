package com.messismo.bar.Services;

import com.messismo.bar.DTOs.DashboardRequestDTO;
import com.messismo.bar.Entities.Category;
import com.messismo.bar.Entities.Order;
import com.messismo.bar.Entities.ProductOrder;
import com.messismo.bar.Exceptions.InvalidDashboardRequestedDate;
import com.messismo.bar.Repositories.CategoryRepository;
import com.messismo.bar.Repositories.OrderRepository;
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


    public HashMap<String, Object>  getDashboardInformation(DashboardRequestDTO dashboardRequestDTO) throws Exception {
        try {
            HashMap<String, Object> response = new HashMap<>();
            if (dashboardRequestDTO.getDateRequested() == null || dashboardRequestDTO.getDateRequested().isEmpty()) {
                response = getYearlyInformation(dashboardRequestDTO.getCategoryList());
            } else if (dashboardRequestDTO.getDateRequested().matches("\\d{4}")) {
                response = getMonthlyInformation(dashboardRequestDTO.getDateRequested(), dashboardRequestDTO.getCategoryList());
            } else if (dashboardRequestDTO.getDateRequested().matches("\\d{4}-\\d{2}-\\d{2}")) {
                response = getWeeklyInformation(dashboardRequestDTO.getDateRequested(), dashboardRequestDTO.getCategoryList());
            } else if (dashboardRequestDTO.getDateRequested().matches("\\d{4}-\\d{2}")) {
                response = getDailyInformation(dashboardRequestDTO.getDateRequested(), dashboardRequestDTO.getCategoryList());
            } else {
                throw new InvalidDashboardRequestedDate("Incorrect date format");
            }
            return response;
        } catch (InvalidDashboardRequestedDate e) {
          throw e;
        }catch (Exception e) {
            throw new Exception("CANNOT get information for dashboards right now");
        }
    }


    private List<Order> filterByCategory2(List<Order> allOrders, List<Category> categoryList) {
        if (categoryList.isEmpty() || categoryList == null) {
            return allOrders;
        } else {
            List<Order> filteredOrders = new ArrayList<>();
            for (Order order : allOrders) {
                System.out.println(order);
                Order newOrder = Order.builder().status(order.getStatus()).dateCreated(order.getDateCreated()).id(order.getId()).user(order.getUser()).build();
                List<ProductOrder> productOrderList = new ArrayList<>();
                double orderPrice = 0.00;
                double orderCost = 0.00;
                for (ProductOrder productOrder : order.getProductOrders()) {
                    if (productOrderHasAnyCategory(categoryList, productOrder.getCategory())) {
                        orderPrice += (productOrder.getProductUnitPrice() * productOrder.getQuantity());
                        orderCost += (productOrder.getProductUnitCost() * productOrder.getQuantity());
                        productOrderList.add(productOrder);
                    }
                }
                if (orderPrice > 0.00 && orderCost > 0.00 && !productOrderList.isEmpty()) {
                    newOrder.setProductOrders(productOrderList);
                    newOrder.setTotalPrice(orderPrice);
                    newOrder.setTotalCost(orderCost);
                    filteredOrders.add(newOrder);
                }
            }
            return filteredOrders;
        }
    }

    private boolean productOrderHasAnyCategory(List<Category> categoryList, Category category) {
        for (Category category1 : categoryList) {
            if (Objects.equals(category.getName(), category1.getName())) {
                return true;
            }
        }
        return false;
    }


    private HashMap<String, Object> getYearlyInformation(List<Category> categoryList) { // DESDE AÑO INICIAL HASTA AÑO ACTUAL
        List<Order> allOrderList = orderRepository.findAll();
        List<Order> filteredOrdersByCategories = filterByCategory2(allOrderList, categoryList);
        LocalDate minDate = filteredOrdersByCategories.stream().map(order -> order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).min(Comparator.naturalOrder()).orElse(LocalDate.now());
        LocalDate maxDate = filteredOrdersByCategories.stream().map(order -> order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).max(Comparator.naturalOrder()).orElse(LocalDate.now());
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
        for (Order order : filteredOrdersByCategories) {
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
        response.put("quantityProductDonut", getQuantityProductDonut(filteredOrdersByCategories));
        response.put("earningProductDonut", getEarningProductDonut(filteredOrdersByCategories));
        response.put("quantityCategoryDonut", getQuantityCategoryDonut(filteredOrdersByCategories));
        response.put("earningCategoryDonut", getEarningCategoryDonut(filteredOrdersByCategories));
        response.put("labels", new ArrayList<>(years));
        return response;
    }

    public HashMap<String, Object> getDailyInformation(String dateRequested, List<Category> categoryList) { // ESE MES DESDE DIA 1 HASTA UN MES MAS
        List<Order> allOrderList = orderRepository.findAll();
        List<Order> filteredOrdersByCategories = filterByCategory2(allOrderList, categoryList);
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
        for (Order order : filteredOrdersByCategories) {
            LocalDate orderDate = order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (orderDate.getYear() == year && orderDate.getMonthValue() == month) {
                filteredOrders.add(order);
                String formattedDate = orderDate.format(DateTimeFormatter.ofPattern("dd"));
                orderByDayQuantity.put(formattedDate, orderByDayQuantity.get(formattedDate) + 1);
                double currentEarnings = orderByDayEarnings.get(formattedDate) + (order.getTotalPrice() - order.getTotalCost());
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

    public HashMap<String, Object> getWeeklyInformation(String dateRequested, List<Category> categoryList) { // ESE DIA HASTA UNA SEMANA MAS
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
        List<Order> allOrderList = orderRepository.findAll();
        List<Order> filteredOrdersByCategories = filterByCategory2(allOrderList, categoryList);
        for (Order order : filteredOrdersByCategories) {
            LocalDate orderDate = order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!orderDate.isBefore(requestedDate) && !orderDate.isAfter(endDate)) {
                filteredOrders.add(order);
                String formattedDate = orderDate.format(DateTimeFormatter.ofPattern("dd/MM"));
                orderByDateQuantity.put(formattedDate, orderByDateQuantity.get(formattedDate) + 1);
                double currentEarnings = orderByDateEarnings.get(formattedDate) + (order.getTotalPrice() - order.getTotalCost());
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

    public HashMap<String, Object> getMonthlyInformation(String yearRequested, List<Category> categoryList) { // ESE AÑO HASTA UN AÑO MAS
        List<Order> filteredOrders = new ArrayList<>();
        List<Order> allOrderList = orderRepository.findAll();
        List<Order> filteredOrdersByCategories = filterByCategory2(allOrderList, categoryList);
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
        for (Order order : filteredOrdersByCategories) {
            LocalDate orderDate = order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (orderDate.isAfter(startDate) && orderDate.isBefore(endDate)) {
                filteredOrders.add(order);
                String month = String.format("%02d", orderDate.getMonthValue());
                ordersByMonthQuantity.put(month, ordersByMonthQuantity.get(month) + 1);
                ordersByMonthEarnings.put(month, ordersByMonthEarnings.get(month) + (order.getTotalPrice() - order.getTotalCost()));
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

    public HashMap<String, Object> getEarningCategoryDonut(List<Order> orders) {
        HashMap<String, Object> categoryEarningsMap = new HashMap<>();
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            String categoryName = category.getName();
            double categoryEarnings = 0.0;
            for (Order order : orders) {
                for (ProductOrder productOrder : order.getProductOrders()) {
                    if (productOrder.getCategory().equals(category)) {
                        categoryEarnings += productOrder.getQuantity() * (productOrder.getProductUnitPrice() - productOrder.getProductUnitCost());
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
                Category category = productOrder.getCategory();
                categorySalesMap.put(category.getName(), categorySalesMap.getOrDefault(category.getName(), 0) + productOrder.getQuantity());
            }
        }
        return new HashMap<>(categorySalesMap);
    }

    public HashMap<String, Double> getEarningProductDonut(List<Order> orders) {
        HashMap<String, Double> productProfitsMap = new HashMap<>();
        for (Order order : orders) {
            for (ProductOrder productOrder : order.getProductOrders()) {
                double productProfit = (productOrder.getProductUnitPrice() - productOrder.getProductUnitCost()) * productOrder.getQuantity();
                productProfitsMap.put(productOrder.getProductName(), productProfitsMap.getOrDefault(productOrder.getProductName(), 0.0) + productProfit);
            }
        }
        return productProfitsMap;
    }

    public HashMap<String, Integer> getQuantityProductDonut(List<Order> orders) {
        HashMap<String, Integer> productSalesMap = new HashMap<>();
        for (Order order : orders) {
            for (ProductOrder productOrder : order.getProductOrders()) {
                productSalesMap.put(productOrder.getProductName(), productSalesMap.getOrDefault(productOrder.getProductName(), 0) + productOrder.getQuantity());
            }
        }
        return productSalesMap;
    }
}
