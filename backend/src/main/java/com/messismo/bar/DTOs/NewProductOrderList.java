package com.messismo.bar.DTOs;

import com.messismo.bar.Entities.ProductOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewProductOrderList {

    private List<ProductOrder> productOrderList;

    private Double totalPrice;

    private Double totalCost;


}
