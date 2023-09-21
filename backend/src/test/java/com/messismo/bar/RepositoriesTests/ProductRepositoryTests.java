package com.messismo.bar.RepositoriesTests;

import com.messismo.bar.Entities.Product;
import com.messismo.bar.Repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testProductRepositoryFindByProductId_ExistingProduct() {
        productRepository.deleteAll();

        Product product1 = new Product();
        product1.setProductId(4L);
        product1.setName("Papas");
        productRepository.save(product1);

        Optional<Product> productById1 = productRepository.findByProductId(4L);
        Optional<Product> productByIdRandom = productRepository.findByProductId(100L);
        System.out.println(productRepository.findAll());
        assertTrue(productById1.isPresent());
        assertEquals("Papas", productById1.get().getName());


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


}
