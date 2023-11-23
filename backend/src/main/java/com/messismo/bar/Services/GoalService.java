package com.messismo.bar.Services;

import com.messismo.bar.DTOs.GoalDTO;
import com.messismo.bar.DTOs.GoalDeleteDTO;
import com.messismo.bar.DTOs.GoalFilterRequestDTO;
import com.messismo.bar.DTOs.GoalModifyDTO;
import com.messismo.bar.Entities.*;
import com.messismo.bar.Repositories.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            List<Goal> goalList = updateGoals();
//            if (Objects.equals(goalDTO.getName(), "") || goalDTO.getName() == null || goalDTO.getStartingDate() == null || goalDTO.getEndingDate() == null || Objects.equals(goalDTO.getObjectType(), "") || goalDTO.getObjectType() == null || goalDTO.getGoalObjective() <= 0.00 || goalDTO.getGoalObjective() == null || ((goalDTO.getObjectType().equals("Product") || goalDTO.getObjectType().equals("Category")) && (Objects.equals(goalDTO.getGoalObject(), "") || goalDTO.getGoalObject() == null))) {
//                return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a goal");
//            }
//            if (goalDTO.getStartingDate().after(goalDTO.getEndingDate())) {
//                return ResponseEntity.status(HttpStatus.CONFLICT).body("Ending date must be after Starting date");
//            }
            for (Goal goal : goalList) {
                if ((goalDTO.getStartingDate().after(goal.getStartingDate()) && goalDTO.getStartingDate().before(goal.getEndingDate())) || (goalDTO.getEndingDate().after(goal.getStartingDate()) && goalDTO.getEndingDate().before(goal.getEndingDate())) || (goalDTO.getStartingDate().equals(goal.getStartingDate()) || goalDTO.getStartingDate().equals(goal.getEndingDate())) || (goalDTO.getEndingDate().equals(goal.getStartingDate()) || goalDTO.getEndingDate().equals(goal.getEndingDate()))) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Ending date and Starting date must not collide with another goal dates");
                }
            } if (goalDTO.getObjectType().equals("Product")) {
                Product product = productService.getProductByName(goalDTO.getGoalObject());
            } else if (goalDTO.getObjectType().equals("Category")) {
                Category category = categoryService.getCategoryByName(goalDTO.getGoalObject());
            }
            Goal newGoal = Goal.builder().name(goalDTO.getName()).startingDate(goalDTO.getStartingDate()).endingDate(goalDTO.getEndingDate()).objectType(goalDTO.getObjectType()).goalObject(goalDTO.getGoalObject()).goalObjective(goalDTO.getGoalObjective()).currentGoal(0.00).status("Upcoming").achieved("Not Achieved").build();
            goalRepository.save(newGoal);
            updateGoals();
            return "Goal created successfully";
        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT create a goal at the moment.");
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<?> getGoals(GoalFilterRequestDTO goalFilterRequestDTO) {
        try {
            if (goalFilterRequestDTO.getAchieved() == null || goalFilterRequestDTO.getStatus() == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Lists must not be null");
            }
            List<Goal> allGoals = updateGoals();
            List<Goal> filteredGoals = allGoals.stream()
                    .filter(goal -> goalFilterRequestDTO.getStatus().isEmpty() || goalFilterRequestDTO.getStatus().contains(goal.getStatus()))
                    .filter(goal -> goalFilterRequestDTO.getAchieved().isEmpty() || goalFilterRequestDTO.getAchieved().contains(goal.getAchieved()))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(filteredGoals);

//            List<Goal> allGoals = updateGoals();
//            List<Goal> filteredGoals = new ArrayList<>();
//            if (!goalFilterRequestDTO.getStatus().isEmpty()) {
//                for (String status : goalFilterRequestDTO.getStatus()) {
//                    for (Goal goal : allGoals) {
//                        if (Objects.equals(goal.getStatus(), status)) {
//                            filteredGoals.add(goal);
//                        }
//                    }
//                }
//            } else {
//                filteredGoals = allGoals;
//            }
//            List<Goal> filteredGoalsByAchieved = new ArrayList<>();
//            if (!goalFilterRequestDTO.getAchieved().isEmpty()) {
//                for (String achieved : goalFilterRequestDTO.getAchieved()) {
//                    for (Goal goal : filteredGoals) {
//                        if (Objects.equals(goal.getAchieved(), achieved)) {
//                            filteredGoalsByAchieved.add(goal);
//                        }
//                    }
//                }
//            } else {
//                filteredGoalsByAchieved = filteredGoals;
//            }
//            return ResponseEntity.status(HttpStatus.OK).body(filteredGoalsByAchieved);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT get goals at the moment.");
        }
    }

    public ResponseEntity<?> deleteGoal(GoalDeleteDTO goalDeleteDTO) {
        try {
            if (goalDeleteDTO.getGoalId() == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to delete a goal");
            }
            updateGoals();
            Goal goal = goalRepository.findByGoalId(goalDeleteDTO.getGoalId()).orElseThrow(() -> new Exception("GoalId DOES NOT match any goalId"));
            if (Objects.equals(goal.getStatus(), "In Process")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Goal is in process, it cannot be deleted");
            } else {
                goalRepository.delete(goal);
                return ResponseEntity.status(HttpStatus.OK).body("Goal deleted successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT delete this goal at the moment.");
        }
    }

    public ResponseEntity<?> modifyGoal(GoalModifyDTO goalModifyDTO) {
        try {
            if (goalModifyDTO.getGoalId() == null || goalModifyDTO.getNewGoalObjective() <= 0.00) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to modify a goal");
            }
            updateGoals();
            Goal goal = goalRepository.findByGoalId(goalModifyDTO.getGoalId()).orElseThrow(() -> new Exception("GoalId DOES NOT match any goalId"));
            if (Objects.equals(goal.getStatus(), "Expired")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Goal is expired, it cannot be modified");
            } else {
                goal.setGoalObjective(goalModifyDTO.getNewGoalObjective());
                goalRepository.save(goal);
                return ResponseEntity.status(HttpStatus.OK).body("Goal modify successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT modify this goal at the moment.");
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