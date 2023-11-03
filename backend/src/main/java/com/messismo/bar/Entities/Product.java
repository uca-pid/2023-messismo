package com.messismo.bar.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name")
    private String name;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "unit_cost")
    private Double unitCost;

    @Column(name = "description")
    private String description;

    @Column(name = "stock")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String name, Double unitPrice, Double unitCost, String description, Integer stock, Category category){
        if(unitPrice<=0.00){
            throw new IllegalArgumentException("Unit price must be greater than 0");
        } else if (unitCost<=0.00) {
            throw new IllegalArgumentException("Unit cost must be greater than 0");
        } else if (stock<=0) {
            throw new IllegalArgumentException("Stock must be greather than 0");
        }
        else {
            this.name=name;
            this.unitPrice=unitPrice;
            this.unitCost=unitCost;
            this.description=description;
            this.stock=stock;
            this.category=category;
        }
    }

    public void updateStock(String operation, Integer quantity){
        if(operation.equals("add")){
            addStock(quantity);
        }
        else if(operation.equals("substract")) {
            removeStock(quantity);
        }
        else{
            throw new IllegalArgumentException("Incorrect operation type");
        }
    }
    public void removeStock(Integer quantity) {
        if (quantity<=0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }else {
            this.stock = (this.stock - quantity);
        }
    }
    public void addStock(Integer quantity) {
        if (quantity<=0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }else {
            this.stock = (this.stock + quantity);
        }
    }

    public void updateUnitPrice(Double unitPrice) {
        if(unitPrice<=0.00){
            throw new IllegalArgumentException("Unit price must be greater than 0.00");
        }
        else {
            this.unitPrice=unitPrice;
        }
    }

    public void updateUnitCost(Double unitCost) {
        if(unitCost<=0.00){
            throw new IllegalArgumentException("Unit cost must be greater than 0.00");
        }
        else {
            this.unitCost=unitCost;
        }
    }
}
