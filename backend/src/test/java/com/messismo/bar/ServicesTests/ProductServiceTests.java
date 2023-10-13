package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.FilterProductDTO;
import com.messismo.bar.DTOs.ProductDTO;
import com.messismo.bar.DTOs.ProductPriceDTO;
import com.messismo.bar.DTOs.ProductStockDTO;
import com.messismo.bar.Entities.Category;
import com.messismo.bar.Entities.Product;
import com.messismo.bar.Repositories.CategoryRepository;
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

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();

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
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
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

        ProductDTO productDTO1 = ProductDTO.builder().category("Entrada").stock(40).description("Con aderezos").unitPrice(77.00).name("Papas Con Bacon").unitCost(5.00).newCategory(false).build();
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");

        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(1)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceAddProduct_WithSameName() {

        ProductDTO productDTO1 = ProductDTO.builder().name("Milanesa").category("Entrada").description("Milanesa con papas fritas").stock(50).unitPrice(70.00).unitCost(5.00).newCategory(false).build();
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("The product already exists");

        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(1)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceAddProduct_WithNullName() {

        ProductDTO productDTO1 = ProductDTO.builder().name(null).category("Entrada").description("Milanesa con papas fritas").stock(50).unitPrice(70.00).unitCost(5.00).newCategory(false).build();
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a product");

        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(0)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceAddProduct_WithEmptyName() {

        ProductDTO productDTO1 = ProductDTO.builder().name("").category("Entrada").description("Milanesa con papas fritas").stock(50).unitPrice(70.00).unitCost(5.00).newCategory(false).build();
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a product");

        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(0)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceAddProduct_WithNullUnitPrice() {

        ProductDTO productDTO1 = ProductDTO.builder().name("Pollito").category("Entrada").description("Milanesa con papas fritas").stock(50).unitPrice(null).unitCost(5.00).newCategory(false).build();
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a product");

        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(0)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceAddProduct_WithNullCategory() {

        ProductDTO productDTO1 = ProductDTO.builder().name("MilanesaTOP").category(null).description("Milanesa con papas fritas").stock(50).unitPrice(70.00).unitCost(5.00).newCategory(false).build();
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a product");

        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(0)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceAddProduct_WithNullDescription() {

        ProductDTO productDTO1 = ProductDTO.builder().name("MilanesaTOP").category("Entrada").description(null).stock(50).unitPrice(70.00).unitCost(5.00).newCategory(false).build();
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a product");

        assertEquals(response, productService.addProduct(productDTO1));
        verify(productRepository, times(0)).findByName(productDTO1.getName());
    }

    @Test
    public void testProductServiceCannotAddProduct() {

        ProductDTO productDTO1 = ProductDTO.builder().name("Pizza").category("Entrada").description("Pizza con papas fritas").stock(50).unitPrice(70.00).unitCost(5.00).newCategory(false).build();
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product NOT created. ");

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

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Missing data to modify product price");
        ProductPriceDTO productPriceDTO = new ProductPriceDTO(null, 50.00);

        assertEquals(response, productService.modifyProductPrice(productPriceDTO));
        verify(productRepository, times(0)).findByProductId(productPriceDTO.getProductId());
    }

    @Test
    public void testProductServiceModifyUnitPriceProduct_WithNullUnitPrice() {

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Missing data to modify product price");
        ProductPriceDTO productPriceDTO = new ProductPriceDTO(1L, null);

        assertEquals(response, productService.modifyProductPrice(productPriceDTO));
        verify(productRepository, times(0)).findByProductId(productPriceDTO.getProductId());
    }

    @Test
    public void testProductServiceAddProductStock() {

        ProductStockDTO productStockDTO = ProductStockDTO.builder().productId(1L).operation("add").modifyStock(50).build();
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).body("Product stock updated successfully");

        assertEquals(response, productService.modifyProductStock(productStockDTO));
    }

    @Test
    public void testProductServiceAddProductStock_WithANonExistantProductId() {

        ProductStockDTO productStockDTO = ProductStockDTO.builder().productId(10L).operation("add").modifyStock(50).build();
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product stock CANNOT be updated");

        assertEquals(response, productService.modifyProductStock(productStockDTO));
    }

    @Test
    public void testProductServiceAddProductStock_WithNullProductId() {

        ProductStockDTO productStockDTO = ProductStockDTO.builder().productId(null).operation("add").modifyStock(50).build();
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data to add product stock");

        assertEquals(response, productService.modifyProductStock(productStockDTO));
    }

    @Test
    public void testProductServiceAddProductStock_WithNullAddStock() {

        ProductStockDTO productStockDTO = ProductStockDTO.builder().productId(1L).operation("add").modifyStock(null).build();
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data to add product stock");

        assertEquals(response, productService.modifyProductStock(productStockDTO));
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
    public void testProductServiceFilterProductsByName() {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().productName("Pollo").build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product3);
        ResponseEntity<List<Product>> responseEntity = ResponseEntity.status(HttpStatus.OK).body(response);

        assertEquals(responseEntity, productService.filterProducts(filterProductDTO));
    }

    @Test
    public void testProductServiceFilterProductsByCategory() {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().categories(List.of("Entrada")).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product2);
        response.add(product3);
        ResponseEntity<List<Product>> responseEntity = ResponseEntity.status(HttpStatus.OK).body(response);

        assertEquals(responseEntity, productService.filterProducts(filterProductDTO));
    }

    @Test
    public void testProductServiceFilterProductsByMaxUnitPrice() {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().maxUnitPrice(20.00).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product3);
        ResponseEntity<List<Product>> responseEntity = ResponseEntity.status(HttpStatus.OK).body(response);

        assertEquals(responseEntity, productService.filterProducts(filterProductDTO));
    }

    @Test
    public void testProductServiceFilterProductsByMinUnitPrice() {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().minUnitPrice(20.00).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product2);
        ResponseEntity<List<Product>> responseEntity = ResponseEntity.status(HttpStatus.OK).body(response);

        assertEquals(responseEntity, productService.filterProducts(filterProductDTO));
    }

    @Test
    public void testProductServiceFilterProductsByMaxStock() {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().maxStock(20).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product3 = Product.builder().productId(3L).name("Pollo").unitPrice(15.99).category(category1).stock(5).description("Pollo con papas fritas").unitCost(5.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product3);
        ResponseEntity<List<Product>> responseEntity = ResponseEntity.status(HttpStatus.OK).body(response);

        assertEquals(responseEntity, productService.filterProducts(filterProductDTO));
    }

    @Test
    public void testProductServiceFilterProductsByMinStock() {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().minStock(20).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product1 = Product.builder().productId(1L).name("Milanesa").unitPrice(14.99).category(category1).stock(50).description("Milanesa con papas fritas").unitCost(5.00).build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product1);
        response.add(product2);
        ResponseEntity<List<Product>> responseEntity = ResponseEntity.status(HttpStatus.OK).body(response);

        assertEquals(responseEntity, productService.filterProducts(filterProductDTO));
    }

    @Test
    public void testProductServiceFilterProducts() {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().productName("Milanesa").categories(List.of("Entrada")).maxStock(30).minStock(0).maxUnitPrice(50.00).minUnitPrice(16.00).build();
        Category category1 = Category.builder().categoryId(1L).name("Entrada").build();
        Product product2 = Product.builder().productId(2L).name("Milanesas").unitPrice(44.99).category(category1).stock(25).description("Milanesas con papas fritas2").unitCost(5.00).build();
        List<Product> response = new ArrayList<>();
        response.add(product2);
        ResponseEntity<List<Product>> responseEntity = ResponseEntity.status(HttpStatus.OK).body(response);

        assertEquals(responseEntity, productService.filterProducts(filterProductDTO));
    }

    @Test
    public void testProductServiceFilterProducts_WithNonExistentCategory() {

        FilterProductDTO filterProductDTO = FilterProductDTO.builder().productName("Milanesa").categories(List.of("Plato")).maxStock(30).minStock(0).maxUnitPrice(50.00).minUnitPrice(16.00).build();
        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT filter at the moment.");

        assertEquals(responseEntity, productService.filterProducts(filterProductDTO));
    }

}
