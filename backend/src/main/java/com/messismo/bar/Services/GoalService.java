package com.messismo.bar.Services;

import com.messismo.bar.DTOs.GoalFilterRequestDTO;
import com.messismo.bar.Entities.Goal;
import com.messismo.bar.Repositories.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;

    public ResponseEntity<?> getAllGoals(GoalFilterRequestDTO goalFilterRequestDTO){
        Date actualDate = new Date();
        List<Goal> allGoals = goalRepository.findAll();
        for(Goal goal : allGoals){
            if(actualDate.after(goal.getStartingDate()) && actualDate.before(goal.getEndingDate())){
                goal.setStatus("In Process");
            }
            else if (actualDate.before(goal.getStartingDate())){
                goal.setStatus("Upcoming");
            }
            else if(actualDate.after(goal.getEndingDate())){
                goal.setStatus("Expired");
            }
            goalRepository.save(goal);
        }
        for(Goal goal : allGoals){
            if((Objects.equals(goal.getStatus(), "In Process") && Objects.equals(goal.getAchieved(), "Not Achieved")) || (Objects.equals(goal.getStatus(), "Expired") && Objects.equals(goal.getAchieved(), "Not Achieved"))){
                if(goalAchieved(goal)){
                    goal.setAchieved("Achieved");
                    goalRepository.save(goal);
                }
            }
        }


        allGoals = goalRepository.findAll();
        List<Goal> filteredGoals = new ArrayList<>();
        if(!goalFilterRequestDTO.getStatus().isEmpty()){
            for(String status: goalFilterRequestDTO.getStatus()){
                for(Goal goal : allGoals){
                    if(Objects.equals(goal.getStatus(), status)){
                        filteredGoals.add(goal);
                    }
                }
            }
        }
        else{
            filteredGoals= allGoals;
        }
        List<Goal> filteredGoalsByAchieved = new ArrayList<>();
        if(!goalFilterRequestDTO.getAchieved().isEmpty()){
            for(String achieved : goalFilterRequestDTO.getAchieved()){
                for(Goal goal : filteredGoals){
                    if(Objects.equals(goal.getAchieved(), achieved)){
                        filteredGoalsByAchieved.add(goal);
                    }
                }
            }
        }
        else{
            filteredGoalsByAchieved = filteredGoals;
        }
        return ResponseEntity.status(HttpStatus.OK).body(filteredGoalsByAchieved);
    }

    private boolean goalAchieved(Goal goal) {
        return true;
    }

}