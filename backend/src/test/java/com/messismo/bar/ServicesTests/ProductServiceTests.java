package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.ProductDTO;
import com.messismo.bar.DTOs.ProductPriceDTO;
import com.messismo.bar.Entities.Product;
import com.messismo.bar.Repositories.ProductRepository;
import com.messismo.bar.Services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductServiceTests {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        Product product1 = new Product(1L, "Milanesa", 14.99, "Entrada", "Milanesa con papas fritas");
        Product product2 = new Product(2L, "Milanesas", 44.99, "Entrada2", "Milanesa con papas fritas2");
        Product product3 = new Product(3L, "Pollo", 15.99, "Entrada3", "Milanesa con papas fritas3");
        Product productDTO1 = new Product();
        productDTO1.setName("Papas Con Bacon");
        productDTO1.setUnitPrice(77.00);
        productDTO1.setDescription("Entrada");
        productDTO1.setCategory("Carne");
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        when(productRepository.findByName("Milanesa")).thenReturn(Optional.ofNullable(product1));
        when(productRepository.findByProductId(1L)).thenReturn(Optional.ofNullable(product1));
        when(productRepository.findByProductId(10L)).thenReturn(Optional.empty());
        when(productRepository.findByProductId(null)).thenReturn(Optional.empty());
        when(productRepository.findByName("")).thenReturn(Optional.empty());
        when(productRepository.findByName("Papas Con Bacon")).thenReturn(Optional.empty());
        when(productRepository.findByName(null)).thenReturn(Optional.empty());
        when(productRepository.findAll()).thenReturn(products);
    }

    @Test
    public void testProductServiceGetAllProducts() {
        Product product1 = new Product(1L, "Milanesa", 14.99, "Entrada", "Milanesa con papas fritas");
        Product product2 = new Product(2L, "Milanesas", 44.99, "Entrada2", "Milanesa con papas fritas2");
        Product product3 = new Product(3L, "Pollo", 15.99, "Entrada3", "Milanesa con papas fritas3");
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        ResponseEntity<List<Product>> response = ResponseEntity.status(HttpStatus.OK).body(products);

        assertEquals(response, productService.getAllProducts());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testProductServiceAddProduct() {
        ProductDTO productDTO1 = new ProductDTO("Papas Con Bacon", 77.00, "Entrada", "Carne");
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");

        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(1)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceAddProduct_WithSameName() {
        ProductDTO productDTO1 = new ProductDTO("Milanesa", 77.00, "Entrada", "Carne");
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("The product already exists");

        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(1)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceAddProduct_WithNullName() {
        ProductDTO productDTO1 = new ProductDTO(null, 77.00, "Entrada", "Carne");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a  product");
        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(0)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceAddProduct_WithEmptyName() {
        ProductDTO productDTO1 = new ProductDTO("", 77.00, "Entrada", "Carne");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a  product");
        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(0)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceAddProduct_WithNullUnitPrice() {
        ProductDTO productDTO1 = new ProductDTO("Pollito", null, "Entrada", "Carne");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a  product");
        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(0)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceAddProduct_WithNullCategory() {
        ProductDTO productDTO1 = new ProductDTO("Pollito", 4.99, null, "Carne");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a  product");
        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(0)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceAddProduct_WithNullDescription() {
        ProductDTO productDTO1 = new ProductDTO("Pollito", 4.99, "Entrada", null);

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a  product");
        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(0)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceCannotAddProduct() {
        ProductDTO productDTO1 = new ProductDTO("Pizza", 4.99, "Plato principal", "Con queso Provolone");

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product NOT created");
        when(productRepository.findByName(productDTO1.getName())).thenThrow(new RuntimeException("Simulated Exception"));
        assertEquals(response, productService.addProduct(productDTO1));
    }

    @Test
    public void testProductServiceDeleteProduct() {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");

        assertEquals(response, productService.deleteProduct(1L));
        verify(productRepository, times(1)).findByProductId(1L);
    }

    @Test
    public void testProductServiceDeleteProduct_NotFound() {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product CANNOT be deleted");

        assertEquals(response, productService.deleteProduct(10L));
        verify(productRepository, times(1)).findByProductId(10L);
    }

    @Test
    public void testProductServiceDeleteProduct_WithNullProductId() {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product CANNOT be deleted");

        assertEquals(response, productService.deleteProduct(null));
        verify(productRepository, times(1)).findByProductId(null);
    }

    @Test
    public void testProductServiceModifyUnitPriceProduct() {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).body("Product price updated successfully");
        ProductPriceDTO productPriceDTO = new ProductPriceDTO(1L, 50.00);
        assertEquals(response, productService.modifyProductPrice(productPriceDTO));
        verify(productRepository, times(1)).findByProductId(productPriceDTO.getProductId());
    }

    @Test
    public void testProductServiceModifyUnitPriceProduct_NotFound() {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product price CANNOT be updated");
        ProductPriceDTO productPriceDTO = new ProductPriceDTO(10L, 50.00);
        assertEquals(response, productService.modifyProductPrice(productPriceDTO));
        verify(productRepository, times(1)).findByProductId(productPriceDTO.getProductId());
    }

    @Test
    public void testProductServiceModifyUnitPriceProduct_WithNullProductId() {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product price CANNOT be updated");
        ProductPriceDTO productPriceDTO = new ProductPriceDTO(null, 50.00);
        assertEquals(response, productService.modifyProductPrice(productPriceDTO));
        verify(productRepository, times(1)).findByProductId(productPriceDTO.getProductId());
    }

    @Test
    public void testProductServiceModifyUnitPriceProduct_WithNullUnitPrice() {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product price CANNOT be updated");
        ProductPriceDTO productPriceDTO = new ProductPriceDTO(1L, null);
        assertEquals(response, productService.modifyProductPrice(productPriceDTO));
        verify(productRepository, times(0)).findByProductId(productPriceDTO.getProductId());
    }


}
