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


    @Override
    public String toString() {
        return "ProductOrder{" +
                "productOrderId=" + productOrderId +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    public ProductOrder(Product product, Integer quantity){
        if(quantity<=0){
            throw new IllegalArgumentException("Product quantity must be greater than 0");
        }
        else {
            this.product=product;
            this.quantity=quantity;
        }
    }
}
