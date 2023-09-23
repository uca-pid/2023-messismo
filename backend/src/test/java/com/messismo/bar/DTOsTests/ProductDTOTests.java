package com.messismo.bar.DTOsTests;

import com.messismo.bar.DTOs.ProductDTO;
import com.messismo.bar.Entities.Role;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ProductDTOTests {
    @Test
    public void testProductDTOGettersAndSetters() {

        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setName("Pollo");
        productDTO1.setUnitPrice(14.99);
        productDTO1.setCategory("Plato principal");
        productDTO1.setDescription("Pollo con papas fritas");

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setName("Pollo");
        productDTO2.setUnitPrice(14.99);
        productDTO2.setCategory("Plato principal");
        productDTO2.setDescription("Pollo con papas fritas");

        ProductDTO productDTO3 = new ProductDTO();
        productDTO3.setName("Milanesa");
        productDTO3.setUnitPrice(19.99);
        productDTO3.setCategory("Entrada");
        productDTO3.setDescription("Milanesa con papas fritas");

        assertEquals(productDTO1.getName(), productDTO2.getName());
        assertEquals(productDTO1.getUnitPrice(), productDTO2.getUnitPrice());
        assertEquals(productDTO1.getCategory(), productDTO2.getCategory());
        assertEquals(productDTO1.getDescription(), productDTO2.getDescription());
        assertNotEquals(productDTO1.getName(), productDTO3.getName());
        assertNotEquals(productDTO1.getUnitPrice(), productDTO3.getUnitPrice());
        assertNotEquals(productDTO1.getCategory(), productDTO3.getCategory());
        assertNotEquals(productDTO1.getDescription(), productDTO3.getDescription());
    }

    @Test
    public void testProductDTOEquals() {

        ProductDTO productDTO1 = new ProductDTO("Milanesa",14.99, "Entrada", "Milanesa con papas fritas");
        ProductDTO productDTO2 = new ProductDTO("Milanesa",14.99, "Entrada", "Milanesa con papas fritas");
        ProductDTO productDTO3 = new ProductDTO("Pollo",15.99, "Entrada", "Milanesa con papas fritas");

        assertEquals(productDTO1, productDTO2);
        assertNotEquals(productDTO1, productDTO3);
    }

    @Test
    public void testProductDTOHashCode() {

        ProductDTO productDTO1 = new ProductDTO("Milanesa",14.99, "Entrada", "Milanesa con papas fritas");
        ProductDTO productDTO2 = new ProductDTO("Milanesa",14.99, "Entrada", "Milanesa con papas fritas");
        ProductDTO productDTO3 = new ProductDTO("Pollo",15.99, "Entrada", "Milanesa con papas fritas");

        assertEquals(productDTO1.hashCode(), productDTO2.hashCode());
        assertNotEquals(productDTO1.hashCode(), productDTO3.hashCode());
    }

    @Test
    public void testProductDTOWithBuilder() {
        ProductDTO productDTO = ProductDTO.builder().name("Milanesa").unitPrice(14.99).category("Plato Principal").description("Con papas fritas").build();

        assertEquals("Milanesa", productDTO.getName());
        assertEquals(14.99, productDTO.getUnitPrice());
        assertEquals("Plato Principal", productDTO.getCategory());
        assertEquals("Con papas fritas", productDTO.getDescription());
    }

    @Test
    public void testProductDTOWithBuilder_WithNoName() {
        ProductDTO productDTO = ProductDTO.builder().unitPrice(14.99).category("Plato Principal").description("Con papas fritas").build();

        Assertions.assertNull(productDTO.getName());
        assertEquals(14.99, productDTO.getUnitPrice());
        assertEquals("Plato Principal", productDTO.getCategory());
        assertEquals("Con papas fritas", productDTO.getDescription());
    }

    @Test
    public void testProductDTOWithBuilder_WithNoUnitPrice() {
        ProductDTO productDTO = ProductDTO.builder().name("Milanesa").category("Plato Principal").description("Con papas fritas").build();

        assertEquals("Milanesa", productDTO.getName());
        Assertions.assertNull(productDTO.getUnitPrice());
        assertEquals("Plato Principal", productDTO.getCategory());
        assertEquals("Con papas fritas", productDTO.getDescription());
    }

    @Test
    public void testProductDTOWithBuilder_WithNoCategory() {
        ProductDTO productDTO = ProductDTO.builder().name("Milanesa").unitPrice(14.99).description("Con papas fritas").build();

        assertEquals("Milanesa", productDTO.getName());
        assertEquals(14.99, productDTO.getUnitPrice());
        Assertions.assertNull(productDTO.getCategory());
        assertEquals("Con papas fritas", productDTO.getDescription());
    }

    @Test
    public void testProductDTOWithBuilder_WithNoDescription() {
        ProductDTO productDTO = ProductDTO.builder().name("Milanesa").unitPrice(14.99).category("Plato Principal").build();

        assertEquals("Milanesa", productDTO.getName());
        assertEquals(14.99, productDTO.getUnitPrice());
        assertEquals("Plato Principal", productDTO.getCategory());
        Assertions.assertNull(productDTO.getDescription());
    }

    @Test
    public void testProductDTOWithEmptyBuilder() {
        ProductDTO productDTO = ProductDTO.builder().build();

        Assertions.assertNull( productDTO.getName());
        Assertions.assertNull( productDTO.getUnitPrice());
        Assertions.assertNull(productDTO.getCategory());
        Assertions.assertNull( productDTO.getDescription());
    }

}
