package com.messismo.bar.EntitiesTests;

import com.messismo.bar.Entities.Category;
import com.messismo.bar.Entities.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ProductTests {

    @Test
    public void testProductsGettersAndSetters() {
        Category category1 = Category.builder().categoryId(1L).name("Plato Principal").build();
        Category category2 = Category.builder().categoryId(2L).name("Postre").build();

        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Pollo");
        product1.setUnitPrice(14.99);
        product1.setCategory(category1);
        product1.setDescription("Pollo con papas fritas");

        Product product2 = new Product();
        product2.setProductId(1L);
        product2.setName("Pollo");
        product2.setUnitPrice(14.99);
        product2.setCategory(category1);
        product2.setDescription("Pollo con papas fritas");

        Product product3 = new Product();
        product3.setProductId(2L);
        product3.setName("Milanesa");
        product3.setUnitPrice(19.99);
        product3.setCategory(category2);
        product3.setDescription("Milanesa con papas fritas");
        assertEquals(product1.getProductId(), product2.getProductId());
        assertEquals(product1.getName(), product2.getName());
        assertEquals(product1.getUnitPrice(), product2.getUnitPrice());
        assertEquals(product1.getCategory(), product2.getCategory());
        assertEquals(product1.getDescription(), product2.getDescription());
        assertNotEquals(product1.getProductId(), product3.getProductId());
        assertNotEquals(product1.getName(), product3.getName());
        assertNotEquals(product1.getUnitPrice(), product3.getUnitPrice());
        assertNotEquals(product1.getCategory(), product3.getCategory());
        assertNotEquals(product1.getDescription(), product3.getDescription());
    }

    @Test
    public void testProductEquals() {
        Category category1 = Category.builder().categoryId(1L).name("Plato Principal").build();
        Category category2 = Category.builder().categoryId(2L).name("Postre").build();
        Product product1 = new Product(1L, "Milanesa", 14.99, "Milanesa con papas fritas",50,category1);
        Product product2 = new Product(1L, "Milanesa", 14.99,"Milanesa con papas fritas",50,category1);
        Product product3 = new Product(2L, "Pollo", 15.99, "Milanesa con papas fritas",25,category2);

        assertEquals(product1, product2);
        assertNotEquals(product1, product3);
    }

    @Test
    public void testProductHashCode() {
        Category category1 = Category.builder().categoryId(1L).name("Plato Principal").build();
        Category category2 = Category.builder().categoryId(2L).name("Postre").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").description("Milanesa con papas fritas").category(category1).stock(50).unitPrice(14.99).build();
        Product product2 = new Product(1L, "Milanesa", 14.99,"Milanesa con papas fritas",50,category1);
        Product product3 = new Product(2L, "Pollo", 15.99, "Milanesa con papas fritas",25,category2);

        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1.hashCode(), product3.hashCode());
    }

}
