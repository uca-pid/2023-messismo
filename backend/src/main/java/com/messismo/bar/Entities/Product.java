package com.messismo.bar.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id",unique = true)
    private Long productId;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;



}
