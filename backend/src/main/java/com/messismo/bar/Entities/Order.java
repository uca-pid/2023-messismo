package com.messismo.bar.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users", referencedColumnName = "id")
    private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_created")
    private Date dateCreated;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<ProductOrder> productOrders;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(name = "status")
    private String status;

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", user=" + user.getEmail() + ", dateCreated=" + dateCreated + ", productOrder=" + productOrders + ", totalPrice=" + totalPrice + ", totalCost=" + totalCost + ", status=" + status + '}';
    }

}
