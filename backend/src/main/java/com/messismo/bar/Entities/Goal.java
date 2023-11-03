package com.messismo.bar.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "goal_id",nullable = false)
    private Long goalId;

    @Column(name = "name")
    private String name;

    @Column(name="startingDate")
    private Date startingDate;

    @Column(name="endingDate")
    private Date endingDate;

    @Column(name="objectType")  // CATEGORY, PRODUCT O TOTAL
    private String objectType;

    @Column(name="goalObject") // SI ES TOTAL, ESTA VACIO, SI ES CATEGORY O PRODUCT TIENE UNA CATEGORY O PRODUCT
    private String goalObject;

    @Column(name="goalObjective") // MONTO FINAL QUE DESEA ALCANZAR
    private Double goalObjective;

    @Column(name = "currentGoal") // MONTO ACTUAL
    private Double currentGoal;

    @Column(name="status")  // EXPIRED, IN PROGRESS O UPCOMING
    private String status;

    @Column(name="achieved")  // ACHIEVED O NOT ACHIEVED
    private String achieved;
}
