package com.messismo.bar.DTOsTests;

import com.messismo.bar.DTOs.CategoryRequestDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryRequestDTOTests {

    @Test
    public void testCategoryRequestDTOGettersAndSetters() {
        CategoryRequestDTO categoryRequestDTO1 = new CategoryRequestDTO("Plato Principal");
        CategoryRequestDTO categoryRequestDTO2 = new CategoryRequestDTO("Plato Principal");
        CategoryRequestDTO categoryRequestDTO3 = new CategoryRequestDTO("Postre");

        assertEquals(categoryRequestDTO1.getCategoryName(), categoryRequestDTO2.getCategoryName());
        assertNotEquals(categoryRequestDTO1.getCategoryName(), categoryRequestDTO3.getCategoryName());
    }

    @Test
    public void testCategoryRequestDTOEquals() {
        CategoryRequestDTO categoryRequestDTO1 = new CategoryRequestDTO("Plato Principal");
        CategoryRequestDTO categoryRequestDTO2 = new CategoryRequestDTO("Plato Principal");
        CategoryRequestDTO categoryRequestDTO3 = new CategoryRequestDTO("Postre");

        assertEquals(categoryRequestDTO1, categoryRequestDTO2);
        assertNotEquals(categoryRequestDTO1, categoryRequestDTO3);
    }

    @Test
    public void testCategoryRequestDTOHashCode() {
        CategoryRequestDTO categoryRequestDTO1 = new CategoryRequestDTO("Plato Principal");
        CategoryRequestDTO categoryRequestDTO2 = new CategoryRequestDTO("Plato Principal");
        CategoryRequestDTO categoryRequestDTO3 = new CategoryRequestDTO("Postre");

        assertEquals(categoryRequestDTO1.hashCode(), categoryRequestDTO2.hashCode());
        assertNotEquals(categoryRequestDTO1.hashCode(), categoryRequestDTO3.hashCode());

    }

    @Test
    public void testCategoryRequestDTOWithBuilder() {
        CategoryRequestDTO categoryRequestDTO1 = CategoryRequestDTO.builder().categoryName("Plato Principal").build();
        CategoryRequestDTO categoryRequestDTO2 = CategoryRequestDTO.builder().categoryName("Plato Principal").build();
        CategoryRequestDTO categoryRequestDTO3 = CategoryRequestDTO.builder().categoryName("Postre").build();

        assertEquals(categoryRequestDTO1, categoryRequestDTO2);
        assertNotEquals(categoryRequestDTO1, categoryRequestDTO3);
    }

    @Test
    public void testCategoryRequestDTOWithEmptyBuilder() {
        CategoryRequestDTO categoryRequestDTO1 = CategoryRequestDTO.builder().build();

        assertNull(categoryRequestDTO1.getCategoryName());
    }
}
