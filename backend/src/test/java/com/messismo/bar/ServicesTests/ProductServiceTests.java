package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.*;
import com.messismo.bar.Entities.Category;
import com.messismo.bar.Entities.Goal;
import com.messismo.bar.Entities.Product;
import com.messismo.bar.Exceptions.CategoryNotFoundException;
import com.messismo.bar.Exceptions.ExistingProductFoundException;
import com.messismo.bar.Exceptions.ProductNotFoundException;
import com.messismo.bar.Repositories.CategoryRepository;
import com.messismo.bar.Repositories.ProductRepository;
import com.messismo.bar.Services.CategoryService;
import com.messismo.bar.Services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductServiceTests {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(55.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(15.00).build();

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
        when(categoryRepository.findByName("Entrada")).thenReturn(Optional.ofNullable(category1));
        when(productRepository.findByName(null)).thenReturn(Optional.empty());
        when(productRepository.findAll()).thenReturn(products);
    }

    @Test
    public void testProductServiceGetAllProducts() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(55.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(15.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);

        assertEquals(products, productService.getAllProducts());
        verify(productRepository, times(1)).findAll();

    }

    @Test
    public void testProductServiceAddProduct() throws Exception {

        ProductDTO productDTO1 = ProductDTO.builder().category("Entrada").stock(40).description("Con aderezos").unitPrice(77.00).name("Papas Con Bacon").unitCost(5.00).newCategory(false).build();

        assertEquals("Product created successfully", productService.addProduct(productDTO1));
        verify(productRepository, times(1)).findByName(productDTO1.getName());

    }

    @Test
    public void testProductServiceAddProduct_WithSameName() {

        ProductDTO productDTO1 = ProductDTO.builder().name("Milanesa").category("Entrada").description("Milanesa con papas fritas").stock(50).unitPrice(70.00).unitCost(5.00).newCategory(false).build();

        ExistingProductFoundException exception = assertThrows(ExistingProductFoundException.class, () -> {
            productService.addProduct(productDTO1);
        });
        Assertions.assertEquals("The product already exists", exception.getMessage());

    }

    @Test
    public void testProductServiceCannotAddProduct() {

        ProductDTO productDTO1 = ProductDTO.builder().name("Pizza").category("Entrada").description("Pizza con papas fritas").stock(50).unitPrice(70.00).unitCost(5.00).newCategory(false).build();

        when(productRepository.findByName(productDTO1.getName())).thenThrow(new RuntimeException("Simulated Exception"));
        Exception exception = assertThrows(Exception.class, () -> {
            productService.addProduct(productDTO1);
        });
        Assertions.assertEquals("Product NOT created", exception.getMessage());

    }

    @Test
    public void testProductServiceDeleteProduct() throws Exception {

        assertEquals("Product deleted successfully", productService.deleteProduct(1L));
        verify(productRepository, times(1)).findByProductId(1L);

    }
    @Test
    public void testProductServiceDeleteProduct_Exception()  {

        doThrow(new RuntimeException("Runtime Exception")).when(productRepository).delete(any());
        Exception exception = assertThrows(Exception.class, () -> {
            productService.deleteProduct(1L);
        });
        Assertions.assertEquals("Product CANNOT be deleted", exception.getMessage());

    }


    @Test
    public void testProductServiceDeleteProduct_NotFound() {

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(10L);
        });
        Assertions.assertEquals("ProductId DOES NOT match any productId", exception.getMessage());
        verify(productRepository, times(1)).findByProductId(10L);

    }


    @Test
    public void testProductServiceModifyUnitPriceProduct() throws Exception {

        ProductPriceDTO productPriceDTO = new ProductPriceDTO(1L, 50.00);

        assertEquals("Product price updated successfully", productService.modifyProductPrice(productPriceDTO));
        verify(productRepository, times(1)).findByProductId(productPriceDTO.getProductId());

    }

    @Test
    public void testProductServiceModifyUnitPriceProduct_NotFound() {

        ProductPriceDTO productPriceDTO = new ProductPriceDTO(10L, 50.00);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.modifyProductPrice(productPriceDTO);
        });
        Assertions.assertEquals("ProductId DOES NOT match any productId", exception.getMessage());
        verify(productRepository, times(1)).findByProductId(productPriceDTO.getProductId());

    }

    @Test
    public void testProductServiceModifyUnitCostProduct_NotFound() {

        ProductPriceDTO productPriceDTO = new ProductPriceDTO(10L, 50.00);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.modifyProductCost(productPriceDTO);
        });
        Assertions.assertEquals("ProductId DOES NOT match any productId", exception.getMessage());
        verify(productRepository, times(1)).findByProductId(productPriceDTO.getProductId());

    }

    @Test
    public void testProductServiceAddProductStock() throws Exception {

        ProductStockDTO productStockDTO = ProductStockDTO.builder().productId(1L).operation("add").modifyStock(50).build();

        assertEquals("Product stock updated successfully", productService.modifyProductStock(productStockDTO));

    }

    @Test
    public void testProductServiceAddProductStock_WithANonExistantProductId() {

        ProductStockDTO productStockDTO = ProductStockDTO.builder().productId(10L).operation("add").modifyStock(50).build();

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.modifyProductStock(productStockDTO);
        });
        Assertions.assertEquals("ProductId DOES NOT match any productId", exception.getMessage());

    }


    @Test
    public void testProductServiceFilterByName() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product2);

        assertEquals(response, productService.filterByName(products, "Mil"));
    }

    @Test
    public void testProductServiceFilterByNullName() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product2);
        response.add(product3);

        assertEquals(response, productService.filterByName(products, null));
    }

    @Test
    public void testProductServiceFilterByNoExistentProductWithThatName() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();

        assertEquals(response, productService.filterByName(products, "Pizza"));
    }

    @Test
    public void testProductServiceFilterByCategory() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Category category2 = Category.builder().categoryId(2L).name("Plato Principal").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category2).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product2);

        assertEquals(response, productService.filterByCategory(products, category1));
    }

    @Test
    public void testProductServiceFilterByNullCategory() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Category category2 = Category.builder().categoryId(2L).name("Plato Principal").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category2).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product2);
        response.add(product3);

        assertEquals(response, productService.filterByCategory(products, null));
    }

    @Test
    public void testProductServiceFilterByCategory_WithNoProductsMatching() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Category category2 = Category.builder().categoryId(2L).name("Plato Principal").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();

        assertEquals(response, productService.filterByCategory(products, category2));
    }

    @Test
    public void testProductServiceFilterByMinStockPrice() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(15).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();
        response.add(product2);

        assertEquals(response, productService.filterByMinStock(products, 20));
    }

    @Test
    public void testProductServiceFilterByNullMinStockPrice() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product2);
        response.add(product3);

        assertEquals(response, productService.filterByMinStock(products, null));
    }

    @Test
    public void testProductServiceFilterByMinStockPrice_WithNoProductsMatching() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();

        assertEquals(response, productService.filterByMinStock(products, 350));
    }

    @Test
    public void testProductServiceFilterByMaxStockPrice() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();
        response.add(product3);

        assertEquals(response, productService.filterByMaxStock(products, 15));
    }

    @Test
    public void testProductServiceFilterByNullMaxStockPrice() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product2);
        response.add(product3);

        assertEquals(response, productService.filterByMaxStock(products, null));
    }

    @Test
    public void testProductServiceFilterByMaxStockPrice_WithNoProductsMatching() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();

        assertEquals(response, productService.filterByMaxStock(products, 1));
    }

    @Test
    public void testProductServiceFilterByMinUnitPrice() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();
        response.add(product2);

        assertEquals(response, productService.filterByMinUnitPrice(products, 20.00));
    }

    @Test
    public void testProductServiceFilterByNullMinUnitPrice() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product2);
        response.add(product3);

        assertEquals(response, productService.filterByMinUnitPrice(products, null));
    }

    @Test
    public void testProductServiceFilterByMinUnitPrice_WithNoProductsMatching() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();

        assertEquals(response, productService.filterByMinUnitPrice(products, 50.00));
    }

    @Test
    public void testProductServiceFilterByMaxUnitPrice() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();
        response.add(product1);

        assertEquals(response, productService.filterByMaxUnitPrice(products, 15.00));
    }

    @Test
    public void testProductServiceFilterByNullMaxUnitPrice() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product2);
        response.add(product3);

        assertEquals(response, productService.filterByMaxUnitPrice(products, null));
    }

    @Test
    public void testProductServiceFilterByMaxUnitPrice_WithNoProductsMatching() {

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        List<Product> response = new ArrayList<>();

        assertEquals(response, productService.filterByMaxUnitPrice(products, 10.99));

    }

    @Test
    public void testProductServiceFilterProductsByName() throws Exception {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().productName("Pollo").build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(15.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product3);

        assertEquals(response, productService.filterProducts(filterProductDTO));

    }

    @Test
    public void testGoalServiceAddGoal_Exception()  {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().productName("Pollo").build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(15.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product3);

        doThrow(new RuntimeException("Runtime Exception")).when(productRepository).findAll();
        Exception exception = assertThrows(Exception.class, () -> {
            productService.filterProducts(filterProductDTO);
        });
        Assertions.assertEquals("CANNOT filter at the moment", exception.getMessage());

    }


    @Test
    public void testProductServiceFilterProductsByCategory() throws Exception {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().categories(List.of("Entrada")).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(55.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(15.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product2);
        response.add(product3);

        assertEquals(response, productService.filterProducts(filterProductDTO));

    }

    @Test
    public void testProductServiceFilterProductsByMaxUnitPrice() throws Exception {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().maxUnitPrice(20.00).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(15.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product3);

        assertEquals(response, productService.filterProducts(filterProductDTO));

    }

    @Test
    public void testProductServiceFilterProductsByMinUnitPrice() throws Exception {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().minUnitPrice(20.00).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(55.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product2);

        assertEquals(response, productService.filterProducts(filterProductDTO));

    }

    @Test
    public void testProductServiceFilterProductsByMaxUnitCost() throws Exception {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().maxUnitCost(20.00).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(15.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product3);

        assertEquals(response, productService.filterProducts(filterProductDTO));

    }

    @Test
    public void testProductServiceFilterProductsByMinUnitCost() throws Exception {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().minUnitCost(50.00).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(55.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product2);

        assertEquals(response, productService.filterProducts(filterProductDTO));

    }

    @Test
    public void testProductServiceFilterProductsByMaxStock() throws Exception {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().maxStock(20).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(15.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product3);

        assertEquals(response, productService.filterProducts(filterProductDTO));

    }

    @Test
    public void testProductServiceFilterProductsByMinStock() throws Exception {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().minStock(20).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(55.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product2);

        assertEquals(response, productService.filterProducts(filterProductDTO));

    }

    @Test
    public void testProductServiceFilterProducts() throws Exception {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().productName("Milanesa").categories(List.of("Entrada")).maxStock(30).minStock(0).maxUnitPrice(50.00).minUnitPrice(16.00).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(55.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product2);

        assertEquals(response, productService.filterProducts(filterProductDTO));

    }

    @Test
    public void testProductServiceFilterProducts_WithNonExistentCategory() throws Exception {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().productName("Milanesa").categories(List.of("Plato")).maxStock(30).minStock(0).maxUnitPrice(50.00).minUnitPrice(16.00).build();

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            productService.filterProducts(filterProductDTO);
        });
        Assertions.assertEquals("Provided category name DOES NOT match any category name", exception.getMessage());

    }

    @Test
    public void testAddProductWithNewCategory() throws Exception {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("New Product");
        productDTO.setUnitPrice(19.99);
        productDTO.setCategory("New Category");
        productDTO.setDescription("A new product");
        productDTO.setStock(100);
        productDTO.setUnitCost(10.0);
        productDTO.setNewCategory(true);
        Category category1 = Category.builder().categoryId(5L).name("New Category").build();

        when(categoryRepository.findByName("New Category")).thenReturn(Optional.ofNullable(category1));
        when(productRepository.findByName("New Product")).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(new Product());
        assertEquals("Product created successfully", productService.addProduct(productDTO));
    }

    @Test
    public void testModifyProductPriceWithNegativeUnitPrice() {

        ProductPriceDTO productPriceDTO = new ProductPriceDTO();
        productPriceDTO.setProductId(1L);
        productPriceDTO.setUnitPrice(-10.0);
        when(productRepository.findByProductId(productPriceDTO.getProductId())).thenReturn(Optional.of(new Product()));
        Exception exception = assertThrows(Exception.class, () -> {
            productService.modifyProductPrice(productPriceDTO);
        });
        verify(productRepository, never()).save(any());
    }

    @Test
    public void testModifyProductCostWithNegativeUnitPrice() {

        ProductPriceDTO productPriceDTO = new ProductPriceDTO();
        productPriceDTO.setProductId(1L);
        productPriceDTO.setUnitPrice(-10.0);
        when(productRepository.findByProductId(productPriceDTO.getProductId())).thenReturn(Optional.of(new Product()));
        Exception exception = assertThrows(Exception.class, () -> {
            productService.modifyProductCost(productPriceDTO);
        });
        verify(productRepository, never()).save(any());
    }

    @Test
    public void testModifyProductCostWithValidInput() throws Exception {

        ProductPriceDTO productPriceDTO = new ProductPriceDTO();
        productPriceDTO.setProductId(1L);
        productPriceDTO.setUnitPrice(20.0);

        when(productRepository.findByProductId(productPriceDTO.getProductId())).thenReturn(Optional.of(new Product()));
        assertEquals("Product cost updated successfully", productService.modifyProductCost(productPriceDTO));
        verify(productRepository, times(1)).save(any());

    }

    @Test
    public void testModifyProductCostWithException() {

        ProductPriceDTO productPriceDTO = new ProductPriceDTO();
        productPriceDTO.setProductId(1L);
        productPriceDTO.setUnitPrice(20.0);
        when(productRepository.findByProductId(productPriceDTO.getProductId()))
                .thenReturn(Optional.of(new Product()));
        doThrow(new RuntimeException("Simulated exception")).when(productRepository).save(any());
        Exception exception = assertThrows(Exception.class, () -> {
            productService.modifyProductCost(productPriceDTO);
        });
    }

    @Test
    public void testModifyProductStockWithNegativeStock() {

        ProductStockDTO productStockDTO = new ProductStockDTO();
        productStockDTO.setProductId(1L);
        productStockDTO.setModifyStock(-10);
        productStockDTO.setOperation("add");
        when(productRepository.findByProductId(productStockDTO.getProductId()))
                .thenReturn(Optional.of(new Product()));
        Exception exception = assertThrows(Exception.class, () -> {
            productService.modifyProductStock(productStockDTO);
        });
        verify(productRepository, never()).save(any());
    }

    @Test
    public void testModifyProductStockWithSubstractOperation() throws Exception {

        ProductStockDTO productStockDTO = new ProductStockDTO();
        productStockDTO.setProductId(1L);
        productStockDTO.setModifyStock(10);
        productStockDTO.setOperation("substract");

        when(productRepository.findByProductId(productStockDTO.getProductId())).thenReturn(Optional.of(Product.builder().productId(5L).name("aProduct").stock(5).unitPrice(15.00).unitCost(5.00).description("aDescription").build()));
        assertEquals("Product stock updated successfully", productService.modifyProductStock(productStockDTO));
        verify(productRepository, times(1)).save(any());

    }

    @Test
    public void testModifyProductStockWithIncorrectOperation() {

        ProductStockDTO productStockDTO = new ProductStockDTO();
        productStockDTO.setProductId(1L);
        productStockDTO.setModifyStock(10);
        productStockDTO.setOperation("multiply");
        when(productRepository.findByProductId(productStockDTO.getProductId()))
                .thenReturn(Optional.of(new Product()));
        Exception exception = assertThrows(Exception.class, () -> {
            productService.modifyProductStock(productStockDTO);
        });
        verify(productRepository, never()).save(any());
    }

}
