package com.messismo.bar.DTOsTests;

import com.messismo.bar.DTOs.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        productDTO1.setStock(50);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setName("Pollo");
        productDTO2.setUnitPrice(14.99);
        productDTO2.setCategory("Plato principal");
        productDTO2.setDescription("Pollo con papas fritas");
        productDTO2.setStock(50);


        ProductDTO productDTO3 = new ProductDTO();
        productDTO3.setName("Milanesa");
        productDTO3.setUnitPrice(19.99);
        productDTO3.setCategory("Entrada");
        productDTO3.setDescription("Milanesa con papas fritas");
        productDTO3.setStock(20);


        assertEquals(productDTO1.getName(), productDTO2.getName());
        assertEquals(productDTO1.getUnitPrice(), productDTO2.getUnitPrice());
        assertEquals(productDTO1.getCategory(), productDTO2.getCategory());
        assertEquals(productDTO1.getStock(), productDTO2.getStock());
        assertNotEquals(productDTO1.getName(), productDTO3.getName());
        assertNotEquals(productDTO1.getUnitPrice(), productDTO3.getUnitPrice());
        assertNotEquals(productDTO1.getStock(), productDTO3.getStock());
        assertNotEquals(productDTO1.getDescription(), productDTO3.getDescription());
    }

    @Test
    public void testProductDTOEquals() {

        ProductDTO productDTO1 = new ProductDTO("Milanesa", 14.99, "Entrada", "Milanesa con papas fritas",50);
        ProductDTO productDTO2 = new ProductDTO("Milanesa", 14.99, "Entrada", "Milanesa con papas fritas",50);
        ProductDTO productDTO3 = new ProductDTO("Pollo", 15.99, "Entrada", "Milanesa con papas fritas",20);

        assertEquals(productDTO1, productDTO2);
        assertNotEquals(productDTO1, productDTO3);
    }

    @Test
    public void testProductDTOHashCode() {

        ProductDTO productDTO1 = new ProductDTO("Milanesa", 14.99, "Entrada", "Milanesa con papas fritas",50);
        ProductDTO productDTO2 = new ProductDTO("Milanesa", 14.99, "Entrada", "Milanesa con papas fritas",50);
        ProductDTO productDTO3 = new ProductDTO("Pollo", 15.99, "Entrada", "Milanesa con papas fritas",20);

        assertEquals(productDTO1.hashCode(), productDTO2.hashCode());
        assertNotEquals(productDTO1.hashCode(), productDTO3.hashCode());
    }

    @Test
    public void testProductDTOWithBuilder() {
        ProductDTO productDTO = ProductDTO.builder().name("Milanesa").unitPrice(14.99).category("Plato Principal")
                .description("Con papas fritas").stock(50).build();

        assertEquals("Milanesa", productDTO.getName());
        assertEquals(14.99, productDTO.getUnitPrice());
        assertEquals(50, productDTO.getStock());
        assertEquals("Plato Principal", productDTO.getCategory());
        assertEquals("Con papas fritas", productDTO.getDescription());
    }

    @Test
    public void testProductDTOWithBuilder_WithNoName() {
        ProductDTO productDTO = ProductDTO.builder().unitPrice(14.99).category("Plato Principal")
                .description("Con papas fritas").stock(50).build();

        Assertions.assertNull(productDTO.getName());
        assertEquals(14.99, productDTO.getUnitPrice());
        assertEquals(50, productDTO.getStock());
        assertEquals("Plato Principal", productDTO.getCategory());
        assertEquals("Con papas fritas", productDTO.getDescription());
    }

    @Test
    public void testProductDTOWithBuilder_WithNoUnitPrice() {
        ProductDTO productDTO = ProductDTO.builder().name("Milanesa").category("Plato Principal")
                .description("Con papas fritas").stock(50).build();

        assertEquals("Milanesa", productDTO.getName());
        Assertions.assertNull(productDTO.getUnitPrice());
        assertEquals(50, productDTO.getStock());
        assertEquals("Plato Principal", productDTO.getCategory());
        assertEquals("Con papas fritas", productDTO.getDescription());
    }

    @Test
    public void testProductDTOWithBuilder_WithNoCategory() {
        ProductDTO productDTO = ProductDTO.builder().name("Milanesa").unitPrice(14.99).description("Con papas fritas").stock(50)
                .build();

        assertEquals("Milanesa", productDTO.getName());
        assertEquals(14.99, productDTO.getUnitPrice());
        Assertions.assertNull(productDTO.getCategory());
        assertEquals(50, productDTO.getStock());
        assertEquals("Con papas fritas", productDTO.getDescription());
    }

    @Test
    public void testProductDTOWithBuilder_WithNoDescription() {
        ProductDTO productDTO = ProductDTO.builder().name("Milanesa").unitPrice(14.99).category("Plato Principal").stock(50)
                .build();

        assertEquals("Milanesa", productDTO.getName());
        assertEquals(14.99, productDTO.getUnitPrice());
        assertEquals(50, productDTO.getStock());
        assertEquals("Plato Principal", productDTO.getCategory());
        Assertions.assertNull(productDTO.getDescription());
    }
    @Test
    public void testProductDTOWithBuilder_WithNoStock() {
        ProductDTO productDTO = ProductDTO.builder().name("Milanesa").unitPrice(14.99).category("Plato Principal")
                .build();

        assertEquals("Milanesa", productDTO.getName());
        assertEquals(14.99, productDTO.getUnitPrice());
        Assertions.assertNull(productDTO.getStock());
        assertEquals("Plato Principal", productDTO.getCategory());
        Assertions.assertNull(productDTO.getDescription());
    }

    @Test
    public void testProductDTOWithEmptyBuilder() {
        ProductDTO productDTO = ProductDTO.builder().build();

        Assertions.assertNull(productDTO.getName());
        Assertions.assertNull(productDTO.getUnitPrice());
        Assertions.assertNull(productDTO.getCategory());
        Assertions.assertNull(productDTO.getDescription());
        Assertions.assertNull(productDTO.getStock());
    }

}
