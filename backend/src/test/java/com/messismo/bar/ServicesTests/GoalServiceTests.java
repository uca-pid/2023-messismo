package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.GoalDTO;
import com.messismo.bar.DTOs.GoalDeleteDTO;
import com.messismo.bar.DTOs.GoalFilterRequestDTO;
import com.messismo.bar.DTOs.GoalModifyDTO;
import com.messismo.bar.Entities.Goal;
import com.messismo.bar.Entities.Order;
import com.messismo.bar.Entities.ProductOrder;
import com.messismo.bar.Repositories.GoalRepository;
import com.messismo.bar.Services.CategoryService;
import com.messismo.bar.Services.GoalService;
import com.messismo.bar.Services.OrderService;
import com.messismo.bar.Services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class GoalServiceTests {

    @InjectMocks
    private GoalService goalService;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private GoalRepository goalRepository;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetGoalsWithEmptyFilters() throws ParseException {

        GoalFilterRequestDTO emptyFilter = new GoalFilterRequestDTO();
        List<Goal> allGoals = createMockGoalList();
        when(goalService.updateGoals()).thenReturn(allGoals);
        ResponseEntity<?> response = goalService.getGoals(emptyFilter);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testGetGoalsWithStatusFilter() throws ParseException {

        GoalFilterRequestDTO statusFilter = new GoalFilterRequestDTO();
        statusFilter.setStatus(List.of("In Progress"));
        List<Goal> allGoals = createMockGoalList();
        when(goalService.updateGoals()).thenReturn(allGoals);
        ResponseEntity<?> response = goalService.getGoals(statusFilter);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

    }

    @Test
    void testGetGoalsWithAchievedFilter() throws ParseException {

        GoalFilterRequestDTO achievedFilter = new GoalFilterRequestDTO();
        achievedFilter.setAchieved(List.of("Achieved"));
        List<Goal> allGoals = createMockGoalList();
        when(goalService.updateGoals()).thenReturn(allGoals);
        ResponseEntity<?> response = goalService.getGoals(achievedFilter);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

    }

    @Test
    void testGetGoalsWithAchievedAndStatusFilters() throws ParseException {

        GoalFilterRequestDTO achievedFilter = new GoalFilterRequestDTO();
        achievedFilter.setStatus(List.of("Not Achieved", "Achieved"));
        achievedFilter.setAchieved(List.of("Expired"));
        List<Goal> allGoals = createMockGoalList();
        when(goalService.updateGoals()).thenReturn(allGoals);
        ResponseEntity<?> response = goalService.getGoals(achievedFilter);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testGetGoalsWithAchievedAndStatusFiltersEmpty() throws ParseException {

        GoalFilterRequestDTO achievedFilter = new GoalFilterRequestDTO();
        achievedFilter.setStatus(List.of());
        achievedFilter.setAchieved(List.of());
        List<Goal> allGoals = createMockGoalList();
        when(goalService.updateGoals()).thenReturn(allGoals);
        ResponseEntity<?> response = goalService.getGoals(achievedFilter);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(allGoals, response.getBody());

    }

    @Test
    void testGetGoalsErrorException() {

        GoalFilterRequestDTO achievedFilter = new GoalFilterRequestDTO();
        achievedFilter.setStatus(List.of());
        achievedFilter.setAchieved(List.of());
        when(goalService.updateGoals()).thenThrow(new RuntimeException("CANNOT get goals at the moment.") {
        });
        ResponseEntity<?> response = goalService.getGoals(achievedFilter);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("CANNOT get goals at the moment.", response.getBody());

    }

    @Test
    void testModifyGoalSuccessfully() {

        GoalModifyDTO validDTO = GoalModifyDTO.builder().goalId(1L).newGoalObjective(10.0).build();
        Goal existingGoal = new Goal();
        existingGoal.setGoalId(validDTO.getGoalId());
        existingGoal.setGoalObjective(5.0);
        when(goalRepository.findByGoalId(validDTO.getGoalId())).thenReturn(Optional.of(existingGoal));
        ResponseEntity<?> response = goalService.modifyGoal(validDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Goal modify successfully", response.getBody());
        assertEquals(validDTO.getNewGoalObjective(), existingGoal.getGoalObjective());

    }

    @Test
    void testModifyExpiredGoal() {

        GoalModifyDTO expiredDTO = GoalModifyDTO.builder().goalId(2L).newGoalObjective(8.0).build();
        Goal expiredGoal = new Goal();
        expiredGoal.setGoalId(expiredDTO.getGoalId());
        expiredGoal.setStatus("Expired");
        when(goalRepository.findByGoalId(expiredDTO.getGoalId())).thenReturn(Optional.of(expiredGoal));
        ResponseEntity<?> response = goalService.modifyGoal(expiredDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Goal is expired, it cannot be modified", response.getBody());

    }

    @Test
    void testModifyGoalMissingInformation() {

        GoalModifyDTO missingInfoDTO = GoalModifyDTO.builder().goalId(1L).newGoalObjective(-5.0).build();
        ResponseEntity<?> response = goalService.modifyGoal(missingInfoDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing information to modify a goal", response.getBody());

    }

    @Test
    void testModifyGoalError() {

        GoalModifyDTO validDTO = GoalModifyDTO.builder().goalId(1L).newGoalObjective(10.0).build();
        when(goalRepository.findByGoalId(validDTO.getGoalId())).thenThrow(new RuntimeException("Cannot access data"));
        ResponseEntity<?> response = goalService.modifyGoal(validDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("CANNOT modify this goal at the moment.", response.getBody());

    }


    @Test
    void testDeleteGoalSuccessfully() {

        GoalDeleteDTO validDTO = GoalDeleteDTO.builder().goalId(1L).build();
        Goal existingGoal = new Goal();
        existingGoal.setGoalId(validDTO.getGoalId());
        existingGoal.setStatus("SomeStatus");
        when(goalRepository.findByGoalId(validDTO.getGoalId())).thenReturn(Optional.of(existingGoal));
        ResponseEntity<?> response = goalService.deleteGoal(validDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Goal deleted successfully", response.getBody());
    }

    @Test
    void testDeleteMissingInformation() {

        GoalDeleteDTO missingInfoDTO = GoalDeleteDTO.builder().goalId(null).build();
        ResponseEntity<?> response = goalService.deleteGoal(missingInfoDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing information to delete a goal", response.getBody());

    }

    @Test
    void testDeleteInProcessGoal() {

        GoalDeleteDTO inProcessDTO = GoalDeleteDTO.builder().goalId(1L).build();
        Goal inProcessGoal = new Goal();
        inProcessGoal.setGoalId(inProcessDTO.getGoalId());
        inProcessGoal.setStatus("In Process");
        when(goalRepository.findByGoalId(inProcessDTO.getGoalId())).thenReturn(Optional.of(inProcessGoal));
        ResponseEntity<?> response = goalService.deleteGoal(inProcessDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Goal is in process, it cannot be deleted", response.getBody());

    }

    @Test
    void testDeleteGoalError() {

        GoalDeleteDTO validDTO = GoalDeleteDTO.builder().goalId(1L).build();
        when(goalRepository.findByGoalId(validDTO.getGoalId())).thenThrow(new RuntimeException("Cannot access data"));
        ResponseEntity<?> response = goalService.deleteGoal(validDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("CANNOT delete this goal at the moment.", response.getBody());

    }

    @Test
    void testAddGoalSuccessfully() {

        GoalDTO validDTO = GoalDTO.builder().name("Valid Goal").startingDate(new Date()).endingDate(new Date(System.currentTimeMillis() + 3600 * 1000)).objectType("Product").goalObject("ValidProduct").goalObjective(10.0).build();
        List<Goal> goalList = new ArrayList<>();
        goalList.add(Goal.builder().name("ExistingGoal").startingDate(new Date(System.currentTimeMillis() + 360000 * 1000)).endingDate(new Date(System.currentTimeMillis() + 360000L * 10000)).build());
        when(goalRepository.findAll()).thenReturn(goalList);
        ResponseEntity<?> response = goalService.addGoal(validDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Goal created successfully", response.getBody());

    }

    @Test
    void testAddGoalMissingInformation() {

        GoalDTO missingInfoDTO = GoalDTO.builder().build();
        ResponseEntity<?> response = goalService.addGoal(missingInfoDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Missing information to create a goal", response.getBody());

    }

    @Test
    void testAddGoalInvalidDates() {

        GoalDTO invalidDateDTO = GoalDTO.builder().name("Valid Goal").startingDate(new Date(System.currentTimeMillis() + 36000 * 1000)).endingDate(new Date(System.currentTimeMillis() + 3600 * 1000)).objectType("Product").goalObject("ValidProduct").goalObjective(10.0).build();
        ResponseEntity<?> response = goalService.addGoal(invalidDateDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Ending date must be after Starting date", response.getBody());

    }

    @Test
    void testAddGoalCollidingDates() {

        GoalDTO collidingDatesDTO = GoalDTO.builder().name("Valid Goal").startingDate(new Date(System.currentTimeMillis() - 1800 * 1000)).endingDate(new Date(System.currentTimeMillis() + 1800 * 1000)).objectType("Product").goalObject("ValidProduct").goalObjective(10.0).build();
        List<Goal> goalList = new ArrayList<>();
        goalList.add(Goal.builder().name("CollidingGoal").startingDate(new Date(System.currentTimeMillis() - 3600 * 1000)).endingDate(new Date(System.currentTimeMillis() + 3600 * 1000)).objectType("Product").goalObject("ValidProduct").goalObjective(10.0).build());
        when(goalRepository.findAll()).thenReturn(goalList);
        ResponseEntity<?> response = goalService.addGoal(collidingDatesDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Ending date and Starting date must not collide with another goal dates", response.getBody());

    }

    @Test
    void testAddGoalProductNotFound() throws Exception {

        GoalDTO productNotFoundDTO = GoalDTO.builder().name("Valid Goal").startingDate(new Date(System.currentTimeMillis() - 1800 * 1000)).endingDate(new Date(System.currentTimeMillis() + 1800 * 1000)).objectType("Product").goalObject("NonexistentProduct").goalObjective(10.0).build();
//        GoalDTO productNotFoundDTO = GoalDTO.builder().objectType("Product").goalObject("NonexistentProduct").build();
        when(productService.getProductByName("NonexistentProduct")).thenThrow(new Exception("Exception"));
        ResponseEntity<?> response = goalService.addGoal(productNotFoundDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("CANNOT create a goal at the moment.", response.getBody());

    }

    @Test
    void testAddGoalCategoryNotFound() throws Exception {

        GoalDTO categoryNotFoundDTO = GoalDTO.builder().name("Valid Goal").startingDate(new Date(System.currentTimeMillis() - 1800 * 1000)).endingDate(new Date(System.currentTimeMillis() + 1800 * 1000)).objectType("Category").goalObject("NonexistentCategory").goalObjective(10.0).build();
        when(categoryService.getCategoryByName("NonexistentCategory")).thenThrow(new Exception("Exception"));
        ResponseEntity<?> response = goalService.addGoal(categoryNotFoundDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("CANNOT create a goal at the moment.", response.getBody());

    }

    @Test
    void testGoalAchievedWithProductType() {

        List<Order> orderList = new ArrayList<>();
        ProductOrder validProductOrder1 = ProductOrder.builder().productName("ValidProduct").productUnitPrice(20.0).productUnitCost(10.0).quantity(5).build();
        orderList.add(Order.builder().productOrders(Collections.singletonList(validProductOrder1)).build());
        Date startingDate = new Date();
        Date endingDate = new Date(System.currentTimeMillis() + 3600 * 1000);
        Goal goal = Goal.builder().objectType("Product").goalObject("ValidProduct").goalObjective(250.00).startingDate(startingDate).endingDate(endingDate).build();
        when(orderService.getAllOrdersBetweenTwoDates(startingDate, endingDate)).thenReturn(orderList);
        Double earnings = goalService.goalAchieved(goal);

        assertEquals(50.0, earnings);
    }
    @Test
    public void testUpdateGoals() {
        List<Goal> goals = new ArrayList<>();
        goals.add(Goal.builder().name("Goal1").currentGoal(0.00).goalObjective(25.00).goalObject("Product1").startingDate(new Date(System.currentTimeMillis() - 35000)).endingDate(new Date(System.currentTimeMillis() + 36000 * 1000)).goalId(1L).objectType("Product").status("Upcoming").achieved("Not Achieved").build());
        goals.add(Goal.builder().name("Goal2").currentGoal(0.00).goalObjective(25.00).goalObject("Product2").startingDate(new Date(System.currentTimeMillis() - 36000 * 1000)).endingDate(new Date(System.currentTimeMillis() - 36000)).goalId(2L).objectType("Product").status("Upcoming").achieved("Not Achieved").build());
        when(goalRepository.findAll()).thenReturn(goals);
        List<Goal> updatedGoals = goalService.updateGoals();
        assertEquals(2, updatedGoals.size());
        for (Goal goal : updatedGoals) {
            if (Objects.equals(goal.getName(), "Goal1")) {
                assertEquals(goal.getStatus(),"In Process");
            }
            else if(Objects.equals(goal.getName(), "Goal2")){
                assertEquals(goal.getStatus(),"Expired");

            }
        }
        verify(goalRepository, atLeastOnce()).findAll();
    }

    private List<Goal> createMockGoalList() throws ParseException {
        return Arrays.asList(Goal.builder().name("goal1").status("Expired").currentGoal(2.00).goalObjective(5.00).objectType("Product").goalObject("goalObject1").achieved("Not Achieved").endingDate(convertToFormat("2023-10-05 00:00:02")).startingDate(convertToFormat("2023-10-04 00:00:02")).build(), Goal.builder().name("goal2").status("Expired").currentGoal(2.00).goalObjective(5.00).objectType("Product").goalObject("goalObject2").achieved("Not Achieved").endingDate(convertToFormat("2023-10-03 00:00:02")).startingDate(convertToFormat("2023-10-02 00:00:02")).build(), Goal.builder().name("goal3").status("Expired").currentGoal(2.00).goalObjective(5.00).objectType("Product").goalObject("goalObject3").achieved("Not Achieved").endingDate(convertToFormat("2023-09-29 00:00:02")).startingDate(convertToFormat("2023-09-28 00:00:02")).build(), Goal.builder().name("goal4").status("Expired").currentGoal(2.00).goalObjective(5.00).objectType("Product").goalObject("goalObject4").achieved("Not Achieved").endingDate(convertToFormat("2023-09-27 00:00:02")).startingDate(convertToFormat("2023-09-26 00:00:02")).build());
    }

    private Date convertToFormat(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(date);
    }

}
