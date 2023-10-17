package com.messismo.bar.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterProductDTO {

    private String productName;

    private List<String> categories;

    private Double minUnitPrice;

    private Double maxUnitPrice;

    private Double minUnitCost;

    private Double maxUnitCost;

    private Integer minStock;

    private Integer maxStock;
}
