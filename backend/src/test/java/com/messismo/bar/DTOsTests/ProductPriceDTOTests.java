package com.messismo.bar.DTOsTests;

import com.messismo.bar.DTOs.ProductPriceDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductPriceDTOTests {

    @Test
    public void testProductPriceDTOGettersAndSetters() {

        ProductPriceDTO productPriceDTO1 = new ProductPriceDTO();
        productPriceDTO1.setProductId(1L);
        productPriceDTO1.setUnitPrice(14.99);
        ProductPriceDTO productPriceDTO2 = new ProductPriceDTO();
        productPriceDTO2.setProductId(1L);
        productPriceDTO2.setUnitPrice(14.99);
        ProductPriceDTO productPriceDTO3 = new ProductPriceDTO();
        productPriceDTO3.setProductId(2L);
        productPriceDTO3.setUnitPrice(19.99);

        assertEquals(productPriceDTO1.getUnitPrice(), productPriceDTO2.getUnitPrice());
        assertNotEquals(productPriceDTO1.getUnitPrice(), productPriceDTO3.getUnitPrice());
    }

    @Test
    public void testProductPriceDTOEquals() {

        ProductPriceDTO productPriceDTO1 = new ProductPriceDTO(1L, 14.99);
        ProductPriceDTO productPriceDTO2 = new ProductPriceDTO(1L, 14.99);
        ProductPriceDTO productPriceDTO3 = new ProductPriceDTO(2L, 19.99);

        assertEquals(productPriceDTO1, productPriceDTO2);
        assertNotEquals(productPriceDTO1, productPriceDTO3);
    }

    @Test
    public void testProductPriceDTOHashCode() {

        ProductPriceDTO productPriceDTO1 = new ProductPriceDTO(1L, 14.99);
        ProductPriceDTO productPriceDTO2 = new ProductPriceDTO(1L, 14.99);
        ProductPriceDTO productPriceDTO3 = new ProductPriceDTO(2L, 19.99);

        assertEquals(productPriceDTO1.hashCode(), productPriceDTO2.hashCode());
        assertNotEquals(productPriceDTO1.hashCode(), productPriceDTO3.hashCode());
    }

    @Test
    public void testProductPriceDTOWithBuilder() {
        ProductPriceDTO productPriceDTO = ProductPriceDTO.builder().productId(1L).unitPrice(9.99).build();

        assertEquals(1L, productPriceDTO.getProductId());
        assertEquals(9.99, productPriceDTO.getUnitPrice());
    }

    @Test
    public void testProductPriceDTOWithBuilder_WithNoProductId() {
        ProductPriceDTO productPriceDTO = ProductPriceDTO.builder().unitPrice(9.99).build();

        assertNull( productPriceDTO.getProductId());
        assertEquals(9.99, productPriceDTO.getUnitPrice());
    }

    @Test
    public void testProductPriceDTOWithBuilder_WithNoUnitPrice() {
        ProductPriceDTO productPriceDTO = ProductPriceDTO.builder().productId(1L).build();

        assertEquals(1L, productPriceDTO.getProductId());
        assertNull( productPriceDTO.getUnitPrice());
    }
    @Test
    public void testProductPriceDTOWithEmptyBuilder() {
        ProductPriceDTO productPriceDTO = ProductPriceDTO.builder().build();

        assertNull( productPriceDTO.getProductId());
        assertNull(productPriceDTO.getUnitPrice());
    }
}
