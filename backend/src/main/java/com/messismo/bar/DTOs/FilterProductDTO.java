package com.messismo.bar.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterProductDTO {

    private String productName;

    private String categoryName;

    private Double minUnitPrice;

    private Double maxUnitPrice;

    private Integer minStock;

    private Integer maxStock;
}
