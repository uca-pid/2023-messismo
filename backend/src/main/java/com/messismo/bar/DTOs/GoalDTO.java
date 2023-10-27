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

    private Date startingDate;

    private Date endingDate;

    private String objectType; // CATEGORY, PRODUCT O TOTAL

    private String goalObject; // SI ES TOTAL, ESTA VACIO, SI ES CATEGORY O PRODUCT TIENE UNA CATEGORY O PRODUCT

    private Double goalObjective; // MONTO FINAL QUE DESEA ALCANZAR

}
