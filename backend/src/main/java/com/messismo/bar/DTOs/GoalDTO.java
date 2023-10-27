package com.messismo.bar.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalDTO {

    private String name;
    private String type;
    private String anObject;
    private Date startingDate;
    private Date endingDate;


}
