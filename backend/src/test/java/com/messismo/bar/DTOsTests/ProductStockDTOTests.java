package com.messismo.bar.DTOsTests;

import com.messismo.bar.DTOs.ProductStockDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductStockDTOTests {

    @Test
    public void testProductStockDTOGettersAndSetters() {
        ProductStockDTO productStockDTO1 = new ProductStockDTO(1L, 20);
        ProductStockDTO productStockDTO2 = new ProductStockDTO(1L, 20);
        ProductStockDTO productStockDTO3 = new ProductStockDTO(2L, 25);

        assertEquals(productStockDTO1.getProductId(), productStockDTO2.getProductId());
        assertEquals(productStockDTO1.getAddStock(), productStockDTO2.getAddStock());
        assertNotEquals(productStockDTO1.getProductId(), productStockDTO3.getProductId());
        assertNotEquals(productStockDTO1.getAddStock(), productStockDTO3.getAddStock());
    }

    @Test
    public void testProductStockDTOEquals() {
        ProductStockDTO productStockDTO1 = new ProductStockDTO(1L, 20);
        ProductStockDTO productStockDTO2 = new ProductStockDTO(1L, 20);
        ProductStockDTO productStockDTO3 = new ProductStockDTO(2L, 25);

        assertEquals(productStockDTO1, productStockDTO2);
        assertNotEquals(productStockDTO1, productStockDTO3);
    }

    @Test
    public void testProductStockDTOHashCode() {
        ProductStockDTO productStockDTO1 = new ProductStockDTO(1L, 20);
        ProductStockDTO productStockDTO2 = new ProductStockDTO(1L, 20);
        ProductStockDTO productStockDTO3 = new ProductStockDTO(2L, 25);

        assertEquals(productStockDTO1.hashCode(), productStockDTO2.hashCode());
        assertNotEquals(productStockDTO1.hashCode(), productStockDTO3.hashCode());

    }

    @Test
    public void testProductStockDTOWithBuilder() {
        ProductStockDTO productStockDTO1 = ProductStockDTO.builder().productId(1L).addStock(20).build();
        ProductStockDTO productStockDTO2 = ProductStockDTO.builder().productId(1L).addStock(20).build();
        ProductStockDTO productStockDTO3 = ProductStockDTO.builder().productId(2L).addStock(25).build();

        assertEquals(productStockDTO1, productStockDTO2);
        assertNotEquals(productStockDTO1, productStockDTO3);
    }

    @Test
    public void testProductStockDTOWithBuilder_WithNoProductId() {
        ProductStockDTO productStockDTO1 = ProductStockDTO.builder().addStock(20).build();

        assertEquals(productStockDTO1.getAddStock(),20);
        assertNull(productStockDTO1.getProductId());
    }

    @Test
    public void testProductStockDTOWithBuilder_WithNoAddStock() {
        ProductStockDTO productStockDTO1 = ProductStockDTO.builder().productId(1L).build();

        assertEquals(productStockDTO1.getProductId(),1L);
        assertNull(productStockDTO1.getAddStock());
    }


    @Test
    public void tesProductStockDTOWithEmptyBuilder() {
        ProductStockDTO productStockDTO1 = ProductStockDTO.builder().build();

        assertNull(productStockDTO1.getAddStock());
        assertNull(productStockDTO1.getProductId());
    }
}
