package com.messismo.bar;

import com.messismo.bar.Entities.Menu;
import com.messismo.bar.Entities.Product;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ProductTests {

    @Test
    public void testProductsGettersAndSetters() {
        Menu menu1 = new Menu(1L,"Menu Invierno",new HashSet<>());
        Set<Menu> menus1 = new HashSet<>();
        menus1.add(menu1);

        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Pollo");
        product1.setUnitPrice(14.99);
        product1.setCategory("Plato principal");
        product1.setDescription("Pollo con papas fritas");
        product1.setMenus(menus1);

        Product product2 = new Product();
        product2.setProductId(1L);
        product2.setName("Pollo");
        product2.setUnitPrice(14.99);
        product2.setCategory("Plato principal");
        product2.setDescription("Pollo con papas fritas");
        product2.setMenus(menus1);

        Product product3 = new Product();
        product3.setProductId(2L);
        product3.setName("Milanesa");
        product3.setUnitPrice(19.99);
        product3.setCategory("Entrada");
        product3.setDescription("Milanesa con papas fritas");
        product3.setMenus(menus1);

        assertEquals(product1.getProductId(), product2.getProductId());
        assertEquals(product1.getName(), product2.getName());
        assertEquals(product1.getUnitPrice(), product2.getUnitPrice());
        assertEquals(product1.getCategory(), product2.getCategory());
        assertEquals(product1.getDescription(), product2.getDescription());
        assertEquals(product1.getMenus(), product2.getMenus());
        assertNotEquals(product1.getProductId(), product3.getProductId());
        assertNotEquals(product1.getName(), product3.getName());
        assertNotEquals(product1.getUnitPrice(), product3.getUnitPrice());
        assertNotEquals(product1.getCategory(), product3.getCategory());
        assertNotEquals(product1.getDescription(), product3.getDescription());
        assertEquals(product1.getMenus(), product3.getMenus());
    }

    @Test
    public void testProductEquals() {

        Menu menu1 = new Menu(1L,"Menu Invierno",new HashSet<>());
        Set<Menu> menus1 = new HashSet<>();
        menus1.add(menu1);
        Product product1 = new Product(1L, "Milanesa",14.99, "Entrada", "Milanesa con papas fritas",menus1);
        Product product2 = new Product(1L, "Milanesa",14.99, "Entrada", "Milanesa con papas fritas",menus1);
        Product product3 = new Product(2L, "Pollo",15.99, "Entrada", "Milanesa con papas fritas",menus1);

        assertEquals(product1, product2);
        assertNotEquals(product1, product3);
    }

    @Test
    public void testProductHashCode() {

        Menu menu1 = new Menu(1L,"Menu Invierno",new HashSet<>());
        Set<Menu> menus1 = new HashSet<>();
        menus1.add(menu1);
        Product product1 = new Product(1L, "Milanesa",14.99, "Entrada", "Milanesa con papas fritas",menus1);
        Product product2 = new Product(1L, "Milanesa",14.99, "Entrada", "Milanesa con papas fritas",menus1);
        Product product3 = new Product(2L, "Pollo",15.99, "Entrada", "Milanesa con papas fritas",menus1);

        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1.hashCode(), product3.hashCode());
    }

}
