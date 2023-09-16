package com.messismo.bar.DTOsTests;

import com.messismo.bar.DTOs.ProductPriceDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ProductPriceDTOTests {

    @Test
    public void testProductPriceDTOGettersAndSetters() {

        ProductPriceDTO productPriceDTO1 = new ProductPriceDTO();
        productPriceDTO1.setUnitPrice(14.99);
        ProductPriceDTO productPriceDTO2 = new ProductPriceDTO();
        productPriceDTO2.setUnitPrice(14.99);
        ProductPriceDTO productPriceDTO3 = new ProductPriceDTO();
        productPriceDTO3.setUnitPrice(19.99);

        assertEquals(productPriceDTO1.getUnitPrice(), productPriceDTO2.getUnitPrice());
        assertNotEquals(productPriceDTO1.getUnitPrice(), productPriceDTO3.getUnitPrice());
    }

    @Test
    public void testProductPriceDTOEquals() {

        ProductPriceDTO productPriceDTO1 = new ProductPriceDTO(14.99);
        ProductPriceDTO productPriceDTO2 = new ProductPriceDTO(14.99);
        ProductPriceDTO productPriceDTO3 = new ProductPriceDTO(19.99);

        assertEquals(productPriceDTO1, productPriceDTO2);
        assertNotEquals(productPriceDTO1, productPriceDTO3);
    }

    @Test
    public void testProductPriceDTOHashCode() {

        ProductPriceDTO productPriceDTO1 = new ProductPriceDTO(14.99);
        ProductPriceDTO productPriceDTO2 = new ProductPriceDTO(14.99);
        ProductPriceDTO productPriceDTO3 = new ProductPriceDTO(19.99);

        assertEquals(productPriceDTO1.hashCode(), productPriceDTO2.hashCode());
        assertNotEquals(productPriceDTO1.hashCode(), productPriceDTO3.hashCode());
    }
}
