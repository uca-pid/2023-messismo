package com.messismo.bar.DTOs;

import com.messismo.bar.Entities.ProductOrder;
import com.messismo.bar.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    private String registeredEmployeeEmail;

    private Date dateCreated;

    private List<ProductOrderDTO> productOrders;

    private Double totalPrice;
}
