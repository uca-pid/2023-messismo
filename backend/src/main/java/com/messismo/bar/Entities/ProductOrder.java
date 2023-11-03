package com.messismo.bar.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_order")
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productOrderId")
    private Long productOrderId;

    //    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "product", referencedColumnName = "product_id")
//    private Product product;
    @Column(name = "productName")
    private String productName;

    @Column(name = "productUnitCost")
    private Double productUnitCost;

    @Column(name = "productUnitPrice")
    private Double productUnitPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category", referencedColumnName = "category_id")
    private Category category;


    @Column(name = "quantity")
    private Integer quantity;


    public ProductOrder(String productName, Double productUnitPrice, Double productUnitCost, Category category, Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Product quantity must be greater than 0");
        } else {
            this.productName = productName;
            this.productUnitPrice = productUnitPrice;
            this.productUnitCost = productUnitCost;
            this.category = category;
            this.quantity = quantity;
        }
    }

    @Override
    public String toString() {
        return "ProductOrder{" + "productOrderId=" + productOrderId + ", product=" + productName + ", quantity=" + quantity + '}';
    }
}
