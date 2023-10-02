package com.messismo.bar.EntitiesTests;

import com.messismo.bar.Entities.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class CategoryTests {

    @Test
    public void testCategoryGettersAndSetters() {
        Category category1 = new Category();
        category1.setCategoryId(1L);
        category1.setName("Plato Principal");
        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setName("Plato Principal");
        Category category3 = new Category();
        category3.setCategoryId(2L);
        category3.setName("Postre");

        assertEquals(category1.getCategoryId(), category2.getCategoryId());
        assertEquals(category1.getName(), category2.getName());
        assertNotEquals(category1.getName(), category3.getName());
        assertNotEquals(category1.getCategoryId(), category3.getCategoryId());
    }

    @Test
    public void testCategoryEquals() {
        Category category1 = new Category(1L, "Plato Principal");
        Category category2 = new Category(1L, "Plato Principal");
        Category category3 = new Category(2L, "Postre");

        assertEquals(category1, category2);
        assertNotEquals(category1, category3);
    }

    @Test
    public void testCategoryHashcode() {
        Category category1 = new Category(1L, "Plato Principal");
        Category category2 = new Category(1L, "Plato Principal");
        Category category3 = new Category(2L, "Postre");

        assertEquals(category1.hashCode(), category2.hashCode());
        assertNotEquals(category1.hashCode(), category3.hashCode());
    }

    @Test
    public void testCategoryWithBuilder() {
        Category category1 = Category.builder().categoryId(1L).name("Plato Principal").build();
        Category category2 = Category.builder().categoryId(1L).name("Plato Principal").build();
        Category category3 = Category.builder().categoryId(2L).name("Postre").build();

        assertEquals(category1.getCategoryId(), category2.getCategoryId());
        assertEquals(category1.getName(), category2.getName());
        assertNotEquals(category1.getName(), category3.getName());
        assertNotEquals(category1.getCategoryId(), category3.getCategoryId());
    }

    @Test
    public void testCategoryWithBuilder_WithNoCategoryId() {
        Category category1 = Category.builder().name("Plato Principal").build();

        Assertions.assertNull(category1.getCategoryId());
        assertEquals(category1.getName(), "Plato Principal");
    }

    @Test
    public void testCategoryWithBuilder_WithNoName() {
        Category category1 = Category.builder().categoryId(1L).build();

        Assertions.assertNull(category1.getName());
    }

    @Test
    public void testCategoryWithEmptyBuilder() {
        Category category1 = Category.builder().build();

        Assertions.assertNull(category1.getName());
        Assertions.assertNull(category1.getCategoryId());
    }

}
