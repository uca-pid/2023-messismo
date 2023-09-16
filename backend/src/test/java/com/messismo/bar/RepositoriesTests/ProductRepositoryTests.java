package com.messismo.bar.RepositoriesTests;

import com.messismo.bar.Entities.Product;
import com.messismo.bar.Repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void testProductRepositoryFindByProductId() {
        productRepository.deleteAll();

        Product product1 = new Product();
        product1.setName("Papas");
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Pollo");
        productRepository.save(product2);

        Product product3 = new Product();
        product3.setName("Batatas");
        productRepository.save(product3);

        Optional<Product> productById1 = productRepository.findByProductId(1L);
        Optional<Product> productById2 = productRepository.findByProductId(2L);
        Optional<Product> productById3 = productRepository.findByProductId(3L);
        Optional<Product> productByIdRandom = productRepository.findByProductId(100L);

        assertTrue(productById1.isPresent());
        assertEquals("Papas", productById1.get().getName());

        assertTrue(productById2.isPresent());
        assertEquals("Pollo", productById2.get().getName());

        assertTrue(productById3.isPresent());
        assertEquals("Batatas", productById3.get().getName());

        assertTrue(productByIdRandom.isEmpty());
    }

    @Test
    public void testProductRepositoryFindByProductId_NotFound() {

        assertThrows(NoSuchElementException.class, () -> {
            productRepository.findByProductId(100L).orElseThrow(() -> new NoSuchElementException("Product not found"));
        });
    }

    @Test
    public void testProductRepositoryFindByProductId_NullNotFound() {

        assertThrows(NoSuchElementException.class, () -> {
            productRepository.findByProductId(null).orElseThrow(() -> new NoSuchElementException("Null Product not found"));
        });
    }

    @Test
    public void testProductRepositoryFindByName() {
        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Papas");
        productRepository.save(product1);
        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setName("Pollo");
        productRepository.save(product2);
        Product product3 = new Product();
        product3.setProductId(3L);
        product3.setName("Batatas");
        productRepository.save(product3);
        Optional<Product> productByName1 = productRepository.findByName("Papas");
        Optional<Product> productByName2 = productRepository.findByName("Pollo");
        Optional<Product> productByName3 = productRepository.findByName("Batatas");
        Optional<Product> productByNameRandom = productRepository.findByName("Milanesas");

        assertTrue(productByName1.isPresent());
        assertEquals("Papas", productByName1.get().getName());
        assertEquals(1L, productByName1.get().getProductId());
        assertTrue(productByName2.isPresent());
        assertEquals("Pollo", productByName2.get().getName());
        assertEquals(2L, productByName2.get().getProductId());
        assertTrue(productByName3.isPresent());
        assertEquals("Batatas", productByName3.get().getName());
        assertEquals(3L, productByName3.get().getProductId());
        assertTrue(productByNameRandom.isEmpty());
    }

    @Test
    public void testProductRepositoryFindByName_NotFound() {

        assertThrows(NoSuchElementException.class, () -> {
            productRepository.findByName("Pollo").orElseThrow(() -> new NoSuchElementException("Product not found"));
        });
    }

    @Test
    public void testProductRepositoryFindByName_NullNotFound() {

        assertThrows(NoSuchElementException.class, () -> {
            productRepository.findByName(null).orElseThrow(() -> new NoSuchElementException("Null Product not found"));
        });
    }

    @Test
    public void testProductRepositoryDeleteAProduct() {
        Product product1 = new Product(1L, "Papas", 14.99, "Plato", "Papas fritas");
        productRepository.save(product1);
        Product product2 = new Product(2L, "Pollo", 20.99, "Plato Principal", "Pollo con papas");
        productRepository.save(product2);
        productRepository.delete(product1);
        Optional<Product> productByName1 = productRepository.findByProductId(1L);
        Optional<Product> productByName2 = productRepository.findByProductId(2L);
        System.out.println(productRepository.findAll());

        assertTrue(productByName1.isEmpty());
        assertTrue(productByName2.isPresent());
        assertEquals("Pollo", productByName2.get().getName());

    }

    @Test
    public void testProductRepositoryDeleteProductNotSaved() {
        Product product1 = new Product(1L, "Milanesa", 14.99, "Entrada", "Milanesa con papas fritas");
        Integer size1 = productRepository.findAll().size();
        productRepository.delete(product1);
        Integer size2 = productRepository.findAll().size();
        assertEquals(size1, size2);
    }

    @Test
    public void testProductRepositoryDeleteCannotBeNull() {

        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            productRepository.delete(null);
        });
    }

}
