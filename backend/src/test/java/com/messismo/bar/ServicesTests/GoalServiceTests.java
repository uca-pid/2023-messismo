package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.GoalDTO;
import com.messismo.bar.DTOs.GoalDeleteDTO;
import com.messismo.bar.DTOs.GoalFilterRequestDTO;
import com.messismo.bar.DTOs.GoalModifyDTO;
import com.messismo.bar.Entities.Goal;
import com.messismo.bar.Entities.Order;
import com.messismo.bar.Entities.ProductOrder;
import com.messismo.bar.Exceptions.*;
import com.messismo.bar.Repositories.GoalRepository;
import com.messismo.bar.Services.CategoryService;
import com.messismo.bar.Services.GoalService;
import com.messismo.bar.Services.OrderService;
import com.messismo.bar.Services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertThrows;
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
    public void testGoalServiceGetGoalsWithAchievedAndStatusFilters() throws Exception {

        GoalFilterRequestDTO achievedFilter = new GoalFilterRequestDTO();
        achievedFilter.setStatus(List.of("Not Achieved", "Achieved"));
        achievedFilter.setAchieved(List.of("Expired"));
        List<Goal> allGoals = createMockGoalList();

        when(goalService.updateGoals()).thenReturn(allGoals);
        assertEquals(new ArrayList<>(), goalService.getGoals(achievedFilter));

    }

    @Test
    public void testGoalServiceGetGoalsWithAchievedAndStatusFiltersEmpty() throws Exception {

        GoalFilterRequestDTO achievedFilter = new GoalFilterRequestDTO();
        achievedFilter.setStatus(List.of());
        achievedFilter.setAchieved(List.of());
        List<Goal> allGoals = createMockGoalList();

        when(goalService.updateGoals()).thenReturn(allGoals);
        assertEquals(allGoals, goalService.getGoals(achievedFilter));

    }

    @Test
    public void testGoalServiceGetGoalsErrorException() {

        GoalFilterRequestDTO achievedFilter = new GoalFilterRequestDTO();
        achievedFilter.setStatus(List.of());
        achievedFilter.setAchieved(List.of());
        when(goalService.updateGoals()).thenThrow(new RuntimeException("CANNOT get goals at the moment") {
        });
        Exception exception = assertThrows(Exception.class, () -> {
            goalService.getGoals(achievedFilter);
        });
        Assertions.assertEquals("CANNOT get goals at the moment", exception.getMessage());

    }

    @Test
    public void testGoalServiceModifyGoalSuccessfully() throws Exception {

        GoalModifyDTO validDTO = GoalModifyDTO.builder().goalId(1L).newGoalObjective(10.0).build();
        Goal existingGoal = new Goal();
        existingGoal.setGoalId(validDTO.getGoalId());
        existingGoal.setGoalObjective(5.0);

        when(goalRepository.findByGoalId(validDTO.getGoalId())).thenReturn(Optional.of(existingGoal));
        assertEquals("Goal modify successfully", goalService.modifyGoal(validDTO));
        assertEquals(validDTO.getNewGoalObjective(), existingGoal.getGoalObjective());

    }

    @Test
    public void testGoalServiceModifyExpiredGoal() {

        GoalModifyDTO expiredDTO = GoalModifyDTO.builder().goalId(2L).newGoalObjective(8.0).build();
        Goal expiredGoal = new Goal();
        expiredGoal.setGoalId(expiredDTO.getGoalId());
        expiredGoal.setStatus("Expired");

        when(goalRepository.findByGoalId(expiredDTO.getGoalId())).thenReturn(Optional.of(expiredGoal));
        GoalExpiredCannotBeModifiedException exception = assertThrows(GoalExpiredCannotBeModifiedException.class, () -> {
            goalService.modifyGoal(expiredDTO);
        });
        Assertions.assertEquals("Goal is expired, it cannot be modified", exception.getMessage());

    }

    @Test
    public void testGoalServiceModifyGoalError() {

        GoalModifyDTO validDTO = GoalModifyDTO.builder().goalId(1L).newGoalObjective(10.0).build();

        when(goalRepository.findByGoalId(validDTO.getGoalId())).thenThrow(new RuntimeException("Cannot access data"));
        Exception exception = assertThrows(Exception.class, () -> {
            goalService.modifyGoal(validDTO);
        });
        Assertions.assertEquals("CANNOT modify this goal at the moment", exception.getMessage());

    }


    @Test
    public void testGoalServiceDeleteGoalSuccessfully() throws Exception {

        GoalDeleteDTO validDTO = GoalDeleteDTO.builder().goalId(1L).build();
        Goal existingGoal = new Goal();
        existingGoal.setGoalId(validDTO.getGoalId());
        existingGoal.setStatus("SomeStatus");

        when(goalRepository.findByGoalId(validDTO.getGoalId())).thenReturn(Optional.of(existingGoal));
        assertEquals("Goal deleted successfully", goalService.deleteGoal(validDTO));
    }


    @Test
    public void testGoalServiceDeleteInProcessGoal() {

        GoalDeleteDTO inProcessDTO = GoalDeleteDTO.builder().goalId(1L).build();
        Goal inProcessGoal = new Goal();
        inProcessGoal.setGoalId(inProcessDTO.getGoalId());
        inProcessGoal.setStatus("In Process");

        when(goalRepository.findByGoalId(inProcessDTO.getGoalId())).thenReturn(Optional.of(inProcessGoal));
        GoalInProcessCannotBeDeletedException exception = assertThrows(GoalInProcessCannotBeDeletedException.class, () -> {
            goalService.deleteGoal(inProcessDTO);
        });
        Assertions.assertEquals("Goal is in process, it cannot be deleted", exception.getMessage());

    }

    @Test
    public void testGoalServiceDeleteGoalError() {

        GoalDeleteDTO validDTO = GoalDeleteDTO.builder().goalId(1L).build();

        when(goalRepository.findByGoalId(validDTO.getGoalId())).thenThrow(new RuntimeException("Cannot access data"));
        Exception exception = assertThrows(Exception.class, () -> {
            goalService.deleteGoal(validDTO);
        });
        Assertions.assertEquals("CANNOT delete this goal at the moment", exception.getMessage());

    }

    @Test
    public void testGoalServiceAddGoalSuccessfully() throws Exception {

        GoalDTO validDTO = GoalDTO.builder().name("Valid Goal").startingDate(new Date()).endingDate(new Date(System.currentTimeMillis() + 3600 * 1000)).objectType("Product").goalObject("ValidProduct").goalObjective(10.0).build();
        List<Goal> goalList = new ArrayList<>();
        goalList.add(Goal.builder().name("ExistingGoal").startingDate(new Date(System.currentTimeMillis() + 360000 * 1000)).endingDate(new Date(System.currentTimeMillis() + 360000L * 10000)).build());

        when(goalRepository.findAll()).thenReturn(goalList);
        assertEquals("Goal created successfully", goalService.addGoal(validDTO));

    }

    @Test
    public void testGoalServiceAddGoalInvalidDates() throws Exception {

        GoalDTO invalidDateDTO = GoalDTO.builder().name("Valid Goal").startingDate(new Date(System.currentTimeMillis() + 36000 * 1000)).endingDate(new Date(System.currentTimeMillis() + 3600 * 1000)).objectType("Product").goalObject("ValidProduct").goalObjective(10.0).build();

        EndingDateMustBeAfterStartingDateException exception = assertThrows(EndingDateMustBeAfterStartingDateException.class, () -> {
            goalService.addGoal(invalidDateDTO);
        });
        Assertions.assertEquals("Ending date must be after Starting date", exception.getMessage());

    }

    @Test
    public void testGoalServiceAddGoalCollidingDates() {

        GoalDTO collidingDatesDTO = GoalDTO.builder().name("Valid Goal").startingDate(new Date(System.currentTimeMillis() - 1800 * 1000)).endingDate(new Date(System.currentTimeMillis() + 1800 * 1000)).objectType("Product").goalObject("ValidProduct").goalObjective(10.0).build();
        List<Goal> goalList = new ArrayList<>();
        goalList.add(Goal.builder().name("CollidingGoal").startingDate(new Date(System.currentTimeMillis() - 3600 * 1000)).endingDate(new Date(System.currentTimeMillis() + 3600 * 1000)).objectType("Product").goalObject("ValidProduct").goalObjective(10.0).build());

        when(goalRepository.findAll()).thenReturn(goalList);
        ProvidedDatesMustNotCollideWithOtherDatesException exception = assertThrows(ProvidedDatesMustNotCollideWithOtherDatesException.class, () -> {
            goalService.addGoal(collidingDatesDTO);
        });
        Assertions.assertEquals("Ending date and Starting date must not collide with another goal dates", exception.getMessage());


    }

    @Test
    public void testGoalServiceAddGoalProductNotFound() throws Exception {

        GoalDTO productNotFoundDTO = GoalDTO.builder().name("Valid Goal").startingDate(new Date(System.currentTimeMillis() - 1800 * 1000)).endingDate(new Date(System.currentTimeMillis() + 1800 * 1000)).objectType("Product").goalObject("NonexistentProduct").goalObjective(10.0).build();

        when(productService.getProductByName("NonexistentProduct")).thenThrow(new ProductNotFoundException("ProductName DOES NOT match any productName"));
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            goalService.addGoal(productNotFoundDTO);
        });
        Assertions.assertEquals("ProductName DOES NOT match any productName", exception.getMessage());

    }

    @Test
    public void testGoalServiceAddGoalCategoryNotFound() throws Exception {

        GoalDTO categoryNotFoundDTO = GoalDTO.builder().name("Valid Goal").startingDate(new Date(System.currentTimeMillis() - 1800 * 1000)).endingDate(new Date(System.currentTimeMillis() + 1800 * 1000)).objectType("Category").goalObject("NonexistentCategory").goalObjective(10.0).build();

        when(categoryService.getCategoryByName("NonexistentCategory")).thenThrow(new CategoryNotFoundException("CategoryName DOES NOT match any categoryName"));
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            goalService.addGoal(categoryNotFoundDTO);
        });
        Assertions.assertEquals("CategoryName DOES NOT match any categoryName", exception.getMessage());

    }

    @Test
    public void testGoalServiceAddGoal_Exception()  {

        GoalDTO validDTO = GoalDTO.builder().name("Valid Goal").startingDate(new Date()).endingDate(new Date(System.currentTimeMillis() + 3600 * 1000)).objectType("Product").goalObject("ValidProduct").goalObjective(10.0).build();
        List<Goal> goalList = new ArrayList<>();
        goalList.add(Goal.builder().name("ExistingGoal").startingDate(new Date(System.currentTimeMillis() + 360000 * 1000)).endingDate(new Date(System.currentTimeMillis() + 360000L * 10000)).build());

        when(goalRepository.findAll()).thenReturn(goalList);
        doThrow(new RuntimeException("Runtime Exception")).when(goalRepository).save(any());
        Exception exception = assertThrows(Exception.class, () -> {
            goalService.addGoal(validDTO);
        });
        Assertions.assertEquals("CANNOT create a goal at the moment", exception.getMessage());

    }



    @Test
    public void testGoalServiceGoalAchievedWithProductType() {

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
    public void testGoalServiceUpdateGoals() {

        List<Goal> goals = new ArrayList<>();
        goals.add(Goal.builder().name("Goal1").currentGoal(0.00).goalObjective(25.00).goalObject("Product1").startingDate(new Date(System.currentTimeMillis() - 35000)).endingDate(new Date(System.currentTimeMillis() + 36000 * 1000)).goalId(1L).objectType("Product").status("Upcoming").achieved("Not Achieved").build());
        goals.add(Goal.builder().name("Goal2").currentGoal(0.00).goalObjective(25.00).goalObject("Product2").startingDate(new Date(System.currentTimeMillis() - 36000 * 1000)).endingDate(new Date(System.currentTimeMillis() - 36000)).goalId(2L).objectType("Product").status("Upcoming").achieved("Not Achieved").build());
        when(goalRepository.findAll()).thenReturn(goals);
        List<Goal> updatedGoals = goalService.updateGoals();
        assertEquals(2, updatedGoals.size());
        for (Goal goal : updatedGoals) {
            if (Objects.equals(goal.getName(), "Goal1")) {
                assertEquals(goal.getStatus(), "In Process");
            } else if (Objects.equals(goal.getName(), "Goal2")) {
                assertEquals(goal.getStatus(), "Expired");

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
