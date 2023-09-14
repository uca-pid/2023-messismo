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
@Table(name = "menus")
public class Menu {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "menu_id", unique = true)
    private Long menuId;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "menu_product", joinColumns = @JoinColumn(name = "menu_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();

}
