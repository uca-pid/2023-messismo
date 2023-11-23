package com.messismo.bar.Services;

import com.messismo.bar.DTOs.GoalDTO;
import com.messismo.bar.DTOs.GoalDeleteDTO;
import com.messismo.bar.DTOs.GoalFilterRequestDTO;
import com.messismo.bar.DTOs.GoalModifyDTO;
import com.messismo.bar.Entities.*;
import com.messismo.bar.Exceptions.*;
import com.messismo.bar.Repositories.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;

    private final OrderService orderService;

    private final ProductService productService;

    private final CategoryService categoryService;


    public String addGoal(GoalDTO goalDTO) throws Exception {
        try {
            if (goalDTO.getStartingDate().after(goalDTO.getEndingDate())) {
                throw new EndingDateMustBeAfterStartingDateException("Ending date must be after Starting date");
            }
            List<Goal> goalList = updateGoals();
            for (Goal goal : goalList) {
                if ((goalDTO.getStartingDate().after(goal.getStartingDate()) && goalDTO.getStartingDate().before(goal.getEndingDate())) || (goalDTO.getEndingDate().after(goal.getStartingDate()) && goalDTO.getEndingDate().before(goal.getEndingDate())) || (goalDTO.getStartingDate().equals(goal.getStartingDate()) || goalDTO.getStartingDate().equals(goal.getEndingDate())) || (goalDTO.getEndingDate().equals(goal.getStartingDate()) || goalDTO.getEndingDate().equals(goal.getEndingDate()))) {
                    throw new ProvidedDatesMustNotCollideWithOtherDatesException("Ending date and Starting date must not collide with another goal dates");
                }
            }
            if (goalDTO.getObjectType().equals("Product")) {
                Product product = productService.getProductByName(goalDTO.getGoalObject());
            } else if (goalDTO.getObjectType().equals("Category")) {
                Category category = categoryService.getCategoryByName(goalDTO.getGoalObject());
            }
            Goal newGoal = Goal.builder().name(goalDTO.getName()).startingDate(goalDTO.getStartingDate()).endingDate(goalDTO.getEndingDate()).objectType(goalDTO.getObjectType()).goalObject(goalDTO.getGoalObject()).goalObjective(goalDTO.getGoalObjective()).currentGoal(0.00).status("Upcoming").achieved("Not Achieved").build();
            goalRepository.save(newGoal);
            updateGoals();
            return "Goal created successfully";
        } catch (ProvidedDatesMustNotCollideWithOtherDatesException | ProductNotFoundException | CategoryNotFoundException | EndingDateMustBeAfterStartingDateException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("CANNOT create a goal at the moment");
        }
    }

    public List<Goal> getGoals(GoalFilterRequestDTO goalFilterRequestDTO) throws Exception {
        try {
            List<Goal> allGoals = updateGoals();
            return allGoals.stream().filter(goal -> goalFilterRequestDTO.getStatus().isEmpty() || goalFilterRequestDTO.getStatus().contains(goal.getStatus())).filter(goal -> goalFilterRequestDTO.getAchieved().isEmpty() || goalFilterRequestDTO.getAchieved().contains(goal.getAchieved())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("CANNOT get goals at the moment");
        }
    }

    public String deleteGoal(GoalDeleteDTO goalDeleteDTO) throws Exception {
        try {
            updateGoals();
            Goal goal = goalRepository.findByGoalId(goalDeleteDTO.getGoalId()).orElseThrow(() -> new GoalIdNotFoundException("GoalId DOES NOT match any goalId"));
            if (Objects.equals(goal.getStatus(), "In Process")) {
                throw new GoalInProcessCannotBeDeletedException("Goal is in process, it cannot be deleted");
            } else {
                goalRepository.delete(goal);
                return "Goal deleted successfully";
            }
        } catch (GoalIdNotFoundException | GoalInProcessCannotBeDeletedException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("CANNOT delete this goal at the moment");
        }
    }

    public String modifyGoal(GoalModifyDTO goalModifyDTO) throws Exception {
        try {
            updateGoals();
            Goal goal = goalRepository.findByGoalId(goalModifyDTO.getGoalId()).orElseThrow(() -> new GoalIdNotFoundException("GoalId DOES NOT match any goalId"));
            if (Objects.equals(goal.getStatus(), "Expired")) {
                throw new GoalExpiredCannotBeModifiedException("Goal is expired, it cannot be modified");
            } else {
                goal.setGoalObjective(goalModifyDTO.getNewGoalObjective());
                goalRepository.save(goal);
                return "Goal modify successfully";
            }
        } catch (GoalIdNotFoundException | GoalExpiredCannotBeModifiedException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("CANNOT modify this goal at the moment");
        }
    }

    public List<Goal> updateGoals() {
        Date actualDate = new Date();
        List<Goal> allGoals = goalRepository.findAll();
        for (Goal goal : allGoals) {
            if (actualDate.after(goal.getStartingDate()) && actualDate.before(goal.getEndingDate())) {
                goal.setStatus("In Process");
            } else if (actualDate.before(goal.getStartingDate())) {
                goal.setStatus("Upcoming");
            } else if (actualDate.after(goal.getEndingDate())) {
                goal.setStatus("Expired");
            }
            goalRepository.save(goal);
        }
        for (Goal goal : allGoals) {
            double earnings = 0.00;
            if ((Objects.equals(goal.getStatus(), "In Process")) || (Objects.equals(goal.getStatus(), "Expired") && Objects.equals(goal.getAchieved(), "Not Achieved"))) {
                earnings = goalAchieved(goal);
                if (earnings >= goal.getGoalObjective()) {
                    goal.setAchieved("Achieved");
                }
                goal.setCurrentGoal(earnings);
                goalRepository.save(goal);
            }
        }
        return goalRepository.findAll();
    }


    public Double goalAchieved(Goal goal) {
        List<Order> orderList = orderService.getAllOrdersBetweenTwoDates(goal.getStartingDate(), goal.getEndingDate());
        double earnings = 0.00;
        if (Objects.equals(goal.getObjectType(), "Total")) {
            for (Order order : orderList) {
                earnings += (order.getTotalPrice() - order.getTotalCost());
            }
        } else if (Objects.equals(goal.getObjectType(), "Product")) {
            for (Order order : orderList) {
                for (ProductOrder productOrder : order.getProductOrders()) {
                    if (Objects.equals(productOrder.getProductName(), goal.getGoalObject())) {
                        earnings += (productOrder.getProductUnitPrice() - productOrder.getProductUnitCost()) * productOrder.getQuantity();
                    }
                }
            }
        } else if (Objects.equals(goal.getObjectType(), "Category")) {
            for (Order order : orderList) {
                for (ProductOrder productOrder : order.getProductOrders()) {
                    if (Objects.equals(productOrder.getCategory().getName(), goal.getGoalObject())) {
                        earnings += (productOrder.getProductUnitPrice() - productOrder.getProductUnitCost()) * productOrder.getQuantity();
                    }
                }
            }
        }
        return earnings;
    }


}