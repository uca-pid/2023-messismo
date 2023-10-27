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

    @Column(name="objectType")
    private String objectType;

    @Column(name="goalObject")
    private String goalObject;

    @Column(name="goalObjective")
    private Double goalObjective;

    @Column(name="status")
    private String status;

    @Column(name="achieved")
    private String achieved;
}
