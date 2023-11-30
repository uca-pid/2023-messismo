package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.DashboardRequestDTO;
import com.messismo.bar.Entities.*;
import com.messismo.bar.Exceptions.InvalidDashboardRequestedDate;
import com.messismo.bar.Repositories.CategoryRepository;
import com.messismo.bar.Repositories.OrderRepository;
import com.messismo.bar.Repositories.ProductRepository;
import com.messismo.bar.Services.DashboardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

public class DashboardServiceTests {

    @InjectMocks
    private DashboardService dashboardService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Category category1 = Category.builder().categoryId(1L).name("Pastries").build();
        Category category2 = Category.builder().categoryId(2L).name("Beverages").build();
        Category category3 = Category.builder().categoryId(3L).name("TomatosVIP").build();
        Category category4 = Category.builder().categoryId(4L).name("PotatoKing").build();
        Category category5 = Category.builder().categoryId(5L).name("VeggieDelight").build();
        Category category6 = Category.builder().categoryId(6L).name("SaladCentral").build();
        Category category7 = Category.builder().categoryId(7L).name("Fruits").build();
        Category category8 = Category.builder().categoryId(8L).name("Bananas").build();
        Category category9 = Category.builder().categoryId(9L).name("Meat").build();
        Category category10 = Category.builder().categoryId(10L).name("Pork").build();
        Category category11 = Category.builder().categoryId(11L).name("Dairy").build();
        Category category12 = Category.builder().categoryId(12L).name("Cheese").build();
        Category category13 = Category.builder().categoryId(13L).name("Bakery").build();
        Category category14 = Category.builder().categoryId(14L).name("Sweets").build();
        List<Category> categories = Arrays.asList(category1, category2, category3, category4, category5, category6, category7, category8, category9, category10, category11, category12, category13, category14);
        when(categoryRepository.findAll()).thenReturn(categories);
    }

    @Test
    public void testGetQuantityProductDonut() {

        List<Order> orders = new ArrayList<>();
        Order order1 = createOrderWithProductsAndCategory("Donut", "Pastries", 3, 1.5, 1.0);
        Order order2 = createOrderWithProductsAndCategory("Coffee", "Beverages", 2, 2.0, 1.0);
        Order order3 = createOrderWithProductsAndCategory("Donut", "Pastries", 2, 1.5, 1.0);
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        HashMap<String, Integer> result = dashboardService.getQuantityProductDonut(orders);
        Map<String, Integer> expectedMap = new HashMap<>();
        expectedMap.put("Donut", 5);
        expectedMap.put("Coffee", 2);

        Assertions.assertEquals(expectedMap, result);
    }

    @Test
    public void testGetEarningProductDonut() {

        List<Order> orders = new ArrayList<>();
        Order order1 = createOrderWithProductsAndCategory("Donut", "Pastries", 3, 1.5, 1.0);
        Order order2 = createOrderWithProductsAndCategory("Coffee", "Beverages", 2, 2.0, 1.0);
        Order order3 = createOrderWithProductsAndCategory("Donut", "Pastries", 2, 1.5, 1.0);
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        HashMap<String, Double> result = dashboardService.getEarningProductDonut(orders);
        Map<String, Double> expectedMap = new HashMap<>();
        expectedMap.put("Donut", 2.5);
        expectedMap.put("Coffee", 2.0);

        Assertions.assertEquals(expectedMap, result);
    }

    @Test
    public void testGetQuantityCategoryDonut() {

        List<Order> orders = new ArrayList<>();
        Order order1 = createOrderWithProductsAndCategory("Donut", "Pastries", 3, 1.5, 1.0);
        Order order2 = createOrderWithProductsAndCategory("Coffee", "Beverages", 2, 2.0, 1.0);
        Order order3 = createOrderWithProductsAndCategory("Donut", "Pastries", 2, 1.5, 1.0);
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        HashMap<String, Object> result = dashboardService.getQuantityCategoryDonut(orders);
        Map<String, Integer> expectedMap = new HashMap<>();
        expectedMap.put("Pastries", 5);
        expectedMap.put("Beverages", 2);

        Assertions.assertEquals(expectedMap, result);
    }

    @Test
    public void testGetEarningCategoryDonut() {

        when(categoryRepository.findAll()).thenReturn(createCategories());
        List<Order> orders = new ArrayList<>();
        Order order1 = createOrderWithProductsAndCategory("Donut", "Pastries", 3, 1.5, 1.0);
        Order order2 = createOrderWithProductsAndCategory("Coffee", "Beverages", 2, 2.0, 1.0);
        Order order3 = createOrderWithProductsAndCategory("Donut", "Pastries", 2, 1.5, 1.0);
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        HashMap<String, Object> result = dashboardService.getEarningCategoryDonut(orders);
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("Pastries", 2.5);
        expectedMap.put("Beverages", 2.0);

        Assertions.assertEquals(expectedMap, result);
    }

    @Test
    public void testGetDashboardInformationYearly() throws Exception {

        List<Order> fakeOrders = createFakeOrders();

        when(orderRepository.findAll()).thenReturn(fakeOrders);
        DashboardRequestDTO request = DashboardRequestDTO.builder().categoryList(new ArrayList<>()).build();
        HashMap<String, Object> response = dashboardService.getDashboardInformation(request);
        HashMap<String, Integer> quantityCategoryDonut = new HashMap<>();
        quantityCategoryDonut.put("PotatoKing", 3);
        quantityCategoryDonut.put("Bakery", 5);
        quantityCategoryDonut.put("Cheese", 4);
        quantityCategoryDonut.put("SaladCentral", 5);
        quantityCategoryDonut.put("Fruits", 5);
        quantityCategoryDonut.put("Meat", 2);
        quantityCategoryDonut.put("TomatosVIP", 2);
        quantityCategoryDonut.put("Bananas", 2);
        quantityCategoryDonut.put("Dairy", 6);
        quantityCategoryDonut.put("Beef", 3);
        quantityCategoryDonut.put("VeggieDelight", 4);
        quantityCategoryDonut.put("Sweets", 3);
        quantityCategoryDonut.put("Pork", 4);
        TreeMap<Integer, Double> averageByOrder = new TreeMap<>();
        averageByOrder.put(2023, 30.6);
        TreeMap<Integer, Integer> orderByQuantity = new TreeMap<>();
        orderByQuantity.put(2023, 5);
        Set<String> responseLabels = new HashSet<>(Arrays.asList("quantityCategoryDonut", "quantityProductDonut", "earningProductDonut", "averageByOrder", "orderByQuantity", "earningCategoryDonut", "orderByEarnings", "labels"));

        Assertions.assertEquals(responseLabels, response.keySet());
        Assertions.assertEquals(quantityCategoryDonut, response.get("quantityCategoryDonut"));
        Assertions.assertEquals(averageByOrder, response.get("averageByOrder"));
        Assertions.assertEquals(orderByQuantity, response.get("orderByQuantity"));
        Assertions.assertEquals(List.of(2023), response.get("labels"));

    }


    @Test
    public void testGetDashboardInformationMonthly() throws Exception {

        List<Order> fakeOrders = createFakeOrders();
        when(orderRepository.findAll()).thenReturn(fakeOrders);
        DashboardRequestDTO request = DashboardRequestDTO.builder().dateRequested("2023").categoryList(new ArrayList<>()).build();
        HashMap<String, Object> response = dashboardService.getDashboardInformation(request);
        HashMap<String, Integer> quantityCategoryDonut = new HashMap<>();
        quantityCategoryDonut.put("PotatoKing", 3);
        quantityCategoryDonut.put("Bakery", 5);
        quantityCategoryDonut.put("Cheese", 4);
        quantityCategoryDonut.put("SaladCentral", 5);
        quantityCategoryDonut.put("Fruits", 5);
        quantityCategoryDonut.put("Meat", 2);
        quantityCategoryDonut.put("TomatosVIP", 2);
        quantityCategoryDonut.put("Bananas", 2);
        quantityCategoryDonut.put("Dairy", 6);
        quantityCategoryDonut.put("Beef", 3);
        quantityCategoryDonut.put("VeggieDelight", 4);
        quantityCategoryDonut.put("Sweets", 3);
        quantityCategoryDonut.put("Pork", 4);
        HashMap<String, Double> averageByOrder = new HashMap<>();
        averageByOrder.put("01", 0.0);
        averageByOrder.put("02", 0.0);
        averageByOrder.put("03", 0.0);
        averageByOrder.put("04", 38.5);
        averageByOrder.put("05", 25.33);
        averageByOrder.put("06", 0.0);
        averageByOrder.put("07", 0.0);
        averageByOrder.put("08", 0.0);
        averageByOrder.put("09", 0.0);
        averageByOrder.put("10", 0.0);
        averageByOrder.put("11", 0.0);
        averageByOrder.put("12", 0.0);
        HashMap<String, Integer> orderByQuantity = new HashMap<>();
        orderByQuantity.put("01", 0);
        orderByQuantity.put("02", 0);
        orderByQuantity.put("03", 0);
        orderByQuantity.put("04", 2);
        orderByQuantity.put("05", 3);
        orderByQuantity.put("06", 0);
        orderByQuantity.put("07", 0);
        orderByQuantity.put("08", 0);
        orderByQuantity.put("09", 0);
        orderByQuantity.put("10", 0);
        orderByQuantity.put("11", 0);
        orderByQuantity.put("12", 0);
        Set<String> responseLabels = new HashSet<>(Arrays.asList("quantityCategoryDonut", "quantityProductDonut", "earningProductDonut", "averageByOrder", "orderByQuantity", "earningCategoryDonut", "orderByEarnings", "labels"));

        Assertions.assertEquals(responseLabels, response.keySet());
        Assertions.assertEquals(quantityCategoryDonut, response.get("quantityCategoryDonut"));
        Assertions.assertEquals(averageByOrder, response.get("averageByOrder"));
        Assertions.assertEquals(orderByQuantity, response.get("orderByQuantity"));
        Assertions.assertEquals(List.of("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"), response.get("labels"));
    }

    @Test
    public void testGetDashboardInformationWeekly() throws Exception {

        List<Order> fakeOrders = createFakeOrders();
        when(orderRepository.findAll()).thenReturn(fakeOrders);
        DashboardRequestDTO request = DashboardRequestDTO.builder().dateRequested("2023-05-10").categoryList(new ArrayList<>()).build();
        HashMap<String, Object> response = dashboardService.getDashboardInformation(request);
        HashMap<String, Integer> quantityCategoryDonut = new HashMap<>();
        quantityCategoryDonut.put("Cheese", 4);
        quantityCategoryDonut.put("Dairy", 6);
        HashMap<String, Double> averageByOrder = new HashMap<>();
        averageByOrder.put("10/05", 0.0);
        averageByOrder.put("11/05", 19.0);
        averageByOrder.put("12/05", 0.0);
        averageByOrder.put("13/05", 0.0);
        averageByOrder.put("14/05", 0.0);
        averageByOrder.put("15/05", 0.0);
        averageByOrder.put("16/05", 0.0);
        HashMap<String, Integer> orderByQuantity = new HashMap<>();
        orderByQuantity.put("10/05", 0);
        orderByQuantity.put("11/05", 1);
        orderByQuantity.put("12/05", 0);
        orderByQuantity.put("13/05", 0);
        orderByQuantity.put("14/05", 0);
        orderByQuantity.put("15/05", 0);
        orderByQuantity.put("16/05", 0);
        Set<String> responseLabels = new HashSet<>(Arrays.asList("quantityCategoryDonut", "quantityProductDonut", "earningProductDonut", "averageByOrder", "orderByQuantity", "earningCategoryDonut", "orderByEarnings", "labels"));

        Assertions.assertEquals(responseLabels, response.keySet());
        Assertions.assertEquals(quantityCategoryDonut, response.get("quantityCategoryDonut"));
        Assertions.assertEquals(averageByOrder, response.get("averageByOrder"));
        Assertions.assertEquals(orderByQuantity, response.get("orderByQuantity"));
        Assertions.assertEquals(List.of("10/05", "11/05", "12/05", "13/05", "14/05", "15/05", "16/05"), response.get("labels"));
    }

    @Test
    public void testGetDashboardInformationDaily() throws Exception {

        List<Order> fakeOrders = createFakeOrders();
        when(orderRepository.findAll()).thenReturn(fakeOrders);
        DashboardRequestDTO request = DashboardRequestDTO.builder().dateRequested("2023-05").categoryList(new ArrayList<>()).build();
        HashMap<String, Object> response = dashboardService.getDashboardInformation(request);
        HashMap<String, Integer> quantityCategoryDonut = new HashMap<>();
        quantityCategoryDonut.put("Dairy", 6);
        quantityCategoryDonut.put("Bakery", 5);
        quantityCategoryDonut.put("Cheese", 4);
        quantityCategoryDonut.put("Beef", 3);
        quantityCategoryDonut.put("Sweets", 3);
        quantityCategoryDonut.put("Pork", 4);
        quantityCategoryDonut.put("Meat", 2);
        HashMap<String, Double> averageByOrder = new HashMap<>();
        averageByOrder.put("01", 0.0);
        averageByOrder.put("02", 0.0);
        averageByOrder.put("03", 0.0);
        averageByOrder.put("04", 46.0);
        averageByOrder.put("05", 0.0);
        averageByOrder.put("06", 0.0);
        averageByOrder.put("07", 0.0);
        averageByOrder.put("08", 0.0);
        averageByOrder.put("09", 0.0);
        averageByOrder.put("10", 0.0);
        averageByOrder.put("11", 19.0);
        averageByOrder.put("12", 0.0);
        averageByOrder.put("13", 0.0);
        averageByOrder.put("14", 0.0);
        averageByOrder.put("15", 0.0);
        averageByOrder.put("16", 0.0);
        averageByOrder.put("17", 0.0);
        averageByOrder.put("18", 11.0);
        averageByOrder.put("19", 0.0);
        averageByOrder.put("20", 0.0);
        averageByOrder.put("21", 0.0);
        averageByOrder.put("22", 0.0);
        averageByOrder.put("23", 0.0);
        averageByOrder.put("24", 0.0);
        averageByOrder.put("25", 0.0);
        averageByOrder.put("26", 0.0);
        averageByOrder.put("27", 0.0);
        averageByOrder.put("28", 0.0);
        averageByOrder.put("29", 0.0);
        averageByOrder.put("30", 0.0);
        averageByOrder.put("31", 0.0);
        HashMap<String, Integer> orderByQuantity = new HashMap<>();
        orderByQuantity.put("01", 0);
        orderByQuantity.put("02", 0);
        orderByQuantity.put("03", 0);
        orderByQuantity.put("04", 1);
        orderByQuantity.put("05", 0);
        orderByQuantity.put("06", 0);
        orderByQuantity.put("07", 0);
        orderByQuantity.put("08", 0);
        orderByQuantity.put("09", 0);
        orderByQuantity.put("10", 0);
        orderByQuantity.put("11", 1);
        orderByQuantity.put("12", 0);
        orderByQuantity.put("13", 0);
        orderByQuantity.put("14", 0);
        orderByQuantity.put("15", 0);
        orderByQuantity.put("16", 0);
        orderByQuantity.put("17", 0);
        orderByQuantity.put("18", 1);
        orderByQuantity.put("19", 0);
        orderByQuantity.put("20", 0);
        orderByQuantity.put("21", 0);
        orderByQuantity.put("22", 0);
        orderByQuantity.put("23", 0);
        orderByQuantity.put("24", 0);
        orderByQuantity.put("25", 0);
        orderByQuantity.put("26", 0);
        orderByQuantity.put("27", 0);
        orderByQuantity.put("28", 0);
        orderByQuantity.put("29", 0);
        orderByQuantity.put("30", 0);
        orderByQuantity.put("31", 0);
        Set<String> responseLabels = new HashSet<>(Arrays.asList("quantityCategoryDonut", "quantityProductDonut", "earningProductDonut", "averageByOrder", "orderByQuantity", "earningCategoryDonut", "orderByEarnings", "labels"));

        Assertions.assertEquals(responseLabels, response.keySet());
        Assertions.assertEquals(quantityCategoryDonut, response.get("quantityCategoryDonut"));
        Assertions.assertEquals(averageByOrder, response.get("averageByOrder"));
        Assertions.assertEquals(orderByQuantity, response.get("orderByQuantity"));
        Assertions.assertEquals(List.of("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"), response.get("labels"));

    }

    @Test
    public void testGetDashboardInformationCatch() {

        DashboardRequestDTO request = new DashboardRequestDTO();
        when(orderRepository.findAll()).thenThrow(new RuntimeException("Simulated exception"));

        Exception exception = assertThrows(Exception.class, () -> {
            dashboardService.getDashboardInformation(request);
        });
        Assertions.assertEquals("CANNOT get information for dashboards right now", exception.getMessage());

    }

    @Test
    public void testGetDashboardInformationIncorrectDateFormat() {

        DashboardRequestDTO request = new DashboardRequestDTO();
        request.setDateRequested("formatoIncorrecto");

        InvalidDashboardRequestedDate exception = assertThrows(InvalidDashboardRequestedDate.class, () -> {
            dashboardService.getDashboardInformation(request);
        });
        Assertions.assertEquals("Incorrect date format", exception.getMessage());

    }

    @Test
    public void testGetDailyInformation() {


        List<Category> allCategories = categoryRepository.findAll();
        String dateRequested = "2023-05";
        List<Order> fakeOrders = createFakeOrders();
        when(orderRepository.findAll()).thenReturn(fakeOrders);
        HashMap<String, Object> result = dashboardService.getDailyInformation(dateRequested, allCategories);

        Assertions.assertNotNull(result.get("orderByQuantity"));
        Assertions.assertNotNull(result.get("orderByEarnings"));
        Assertions.assertNotNull(result.get("averageByOrder"));
        TreeMap<String, Integer> orderByQuantity = (TreeMap<String, Integer>) result.get("orderByQuantity");
        Assertions.assertNotNull(orderByQuantity);
        Assertions.assertEquals(31, orderByQuantity.size());
        TreeMap<String, Double> orderByEarnings = (TreeMap<String, Double>) result.get("orderByEarnings");
        Assertions.assertNotNull(orderByEarnings);
        Assertions.assertEquals(31, orderByEarnings.size());
        TreeMap<String, Double> averageByOrder = (TreeMap<String, Double>) result.get("averageByOrder");
        Assertions.assertNotNull(averageByOrder);
        Assertions.assertEquals(0, (int) orderByQuantity.get("01"));
        Assertions.assertEquals(1, (int) orderByQuantity.get("04"));
        Assertions.assertEquals(0, (int) orderByQuantity.get("03"));

    }

    @Test
    public void testGetWeeklyInformation() {

        List<Category> allCategories = categoryRepository.findAll();
        String dateRequested = "2023-05-10";
        List<Order> fakeOrders = createFakeOrders();
        when(orderRepository.findAll()).thenReturn(fakeOrders);
        HashMap<String, Object> result = dashboardService.getWeeklyInformation(dateRequested, allCategories);

        Assertions.assertNotNull(result.get("orderByQuantity"));
        Assertions.assertNotNull(result.get("orderByEarnings"));
        Assertions.assertNotNull(result.get("averageByOrder"));
        Assertions.assertEquals(Integer.valueOf(0), ((Map<String, Integer>) result.get("orderByQuantity")).get("10/05"));
        Assertions.assertEquals(Integer.valueOf(1), ((Map<String, Integer>) result.get("orderByQuantity")).get("11/05"));
        Assertions.assertEquals(Integer.valueOf(0), ((Map<String, Integer>) result.get("orderByQuantity")).get("12/05"));
        Assertions.assertEquals(Double.valueOf(0.0), ((Map<String, Double>) result.get("orderByEarnings")).get("10/05"));
        Assertions.assertEquals(Double.valueOf(19.0), ((Map<String, Double>) result.get("orderByEarnings")).get("11/05"));
        Assertions.assertEquals(Double.valueOf(0.0), ((Map<String, Double>) result.get("orderByEarnings")).get("12/05"));
        Assertions.assertNotNull(result.get("labels"));
        List<String> labels = (List<String>) result.get("labels");
        Assertions.assertEquals("10/05", labels.get(0));
        Assertions.assertEquals("11/05", labels.get(1));
        Assertions.assertEquals("12/05", labels.get(2));
    }

    @Test
    public void testGetMonthlyInformation() {

        List<Category> allCategories = categoryRepository.findAll();
        String yearRequested = "2023";
        List<Order> fakeOrders = createFakeOrders();
        when(orderRepository.findAll()).thenReturn(fakeOrders);
        HashMap<String, Object> result = dashboardService.getMonthlyInformation(yearRequested, allCategories);

        Assertions.assertNotNull(result.get("orderByQuantity"));
        Assertions.assertNotNull(result.get("orderByEarnings"));
        Assertions.assertNotNull(result.get("averageByOrder"));
    }


    private List<Order> createFakeOrders() {
        List<ProductOrder> productOrders1 = new ArrayList<>();
        productOrders1.add(createFakeProductOrder("Tomato", 10.00, 5.00, "Tomato description", 10, "TomatosVIP", 2));
        productOrders1.add(createFakeProductOrder("Potato", 20.00, 10.00, "Potato description", 20, "PotatoKing", 3));
        productOrders1.add(createFakeProductOrder("Carrot", 8.00, 4.00, "Carrot description", 15, "VeggieDelight", 4));
        productOrders1.add(createFakeProductOrder("Lettuce", 6.00, 3.00, "Lettuce description", 12, "SaladCentral", 5));
        List<ProductOrder> productOrders2 = new ArrayList<>();
        productOrders2.add(createFakeProductOrder("Apple", 2.00, 1.00, "Fresh apples", 50, "Fruits", 5));
        productOrders2.add(createFakeProductOrder("Banana", 1.00, 0.50, "Ripe bananas", 40, "Bananas", 2));
        List<ProductOrder> productOrders3 = new ArrayList<>();
        productOrders3.add(createFakeProductOrder("Chicken", 8.00, 4.00, "Free-range chicken", 30, "Meat", 2));
        productOrders3.add(createFakeProductOrder("Beef", 12.00, 6.00, "Grass-fed beef", 25, "Beef", 3));
        productOrders3.add(createFakeProductOrder("Pork", 10.00, 5.00, "Organic pork", 20, "Pork", 4));
        List<ProductOrder> productOrders4 = new ArrayList<>();
        productOrders4.add(createFakeProductOrder("Milk", 3.00, 1.50, "Fresh milk", 60, "Dairy", 6));
        productOrders4.add(createFakeProductOrder("Cheese", 5.00, 2.50, "Cheddar cheese", 40, "Cheese", 4));
        List<ProductOrder> productOrders5 = new ArrayList<>();
        productOrders5.add(createFakeProductOrder("Bread", 2.00, 1.00, "Whole-grain bread", 30, "Bakery", 5));
        productOrders5.add(createFakeProductOrder("Cookies", 4.00, 2.00, "Chocolate chip cookies", 50, "Sweets", 3));
        List<Order> orders = new ArrayList<>();
        Date date1 = createDate(2023, 4, 20);
        Date date2 = createDate(2023, 4, 27);
        Date date3 = createDate(2023, 5, 4);
        Date date4 = createDate(2023, 5, 11);
        Date date5 = createDate(2023, 5, 18);
        orders.add(createFakeOrder(productOrders1, "martincito", "martin@gmail.com", 142.00, 71.00, "Closed", date1));
        orders.add(createFakeOrder(productOrders2, "alice", "alice@gmail.com", 12.00, 6.00, "Open", date2));
        orders.add(createFakeOrder(productOrders3, "bob", "bob@gmail.com", 92.00, 46.00, "Open", date3));
        orders.add(createFakeOrder(productOrders4, "carol", "carol@gmail.com", 38.00, 19.00, "Closed", date4));
        orders.add(createFakeOrder(productOrders5, "dave", "dave@gmail.com", 22.00, 11.00, "Open", date5));
        return orders;
    }

    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day); // Meses en Calendar van de 0 a 11, por lo que restamos 1.
        return calendar.getTime();
    }

    private Order createFakeOrder(List<ProductOrder> productOrders, String username, String email, Double totalPrice, Double totalCost, String status, Date date) {

        return Order.builder().productOrders(productOrders).user(createFakeUser(username, email)).dateCreated(date).totalPrice(totalPrice).totalCost(totalCost).status(status).build();
    }

    private User createFakeUser(String username, String email) {
        return User.builder().username(username).email(email).role(Role.VALIDATEDEMPLOYEE).password("Password1").build();
    }

    private ProductOrder createFakeProductOrder(String name, Double unitPrice, Double unitCost, String description, Integer stock, String category, int quantity) {
        Product product = createFakeProduct(name, unitPrice, unitCost, description, stock, category);
        return ProductOrder.builder().productName(name).productUnitPrice(unitPrice).productUnitCost(unitCost).category(product.getCategory()).quantity(quantity).build();
    }

    private Product createFakeProduct(String name, Double unitPrice, Double unitCost, String description, Integer stock, String category) {
        return Product.builder().name(name).unitPrice(unitPrice).unitCost(unitCost).description(description).stock(stock).category(createFakeCategory(category)).build();
    }

    private Category createFakeCategory(String categoryName) {
        return Category.builder().name(categoryName).build();
    }

    private List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        Category pastries = new Category();
        pastries.setName("Pastries");
        categories.add(pastries);
        Category beverages = new Category();
        beverages.setName("Beverages");
        categories.add(beverages);
        return categories;
    }

    private Order createOrderWithProductsAndCategory(String productName, String categoryName, int quantity, double unitPrice, double unitCost) {
        ProductOrder productOrder = new ProductOrder();
        Category category = Category.builder().name(categoryName).build();
        Product product = Product.builder().name(productName).unitPrice(unitPrice).unitCost(unitCost).category(category).build();
        productOrder.setProductName(productName);
        productOrder.setCategory(category);
        productOrder.setProductUnitPrice(unitPrice);
        productOrder.setProductUnitCost(unitCost);
        productOrder.setQuantity(quantity);
        Order order = new Order();
        order.setProductOrders(List.of(productOrder));
        return order;
    }

}
