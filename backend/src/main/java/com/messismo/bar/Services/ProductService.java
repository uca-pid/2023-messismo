package com.messismo.bar.Services;

import com.messismo.bar.DTOs.FilterProductDTO;
import com.messismo.bar.DTOs.ProductDTO;
import com.messismo.bar.DTOs.ProductPriceDTO;
import com.messismo.bar.DTOs.ProductStockDTO;
import com.messismo.bar.Entities.Category;
import com.messismo.bar.Entities.Product;
import com.messismo.bar.Exceptions.CategoryNotFoundException;
import com.messismo.bar.Exceptions.ProductNotFoundException;
import com.messismo.bar.Repositories.CategoryRepository;
import com.messismo.bar.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ResponseEntity<?> addProduct(ProductDTO productDTO) {
        if (productDTO.getCategory() == null || productDTO.getName() == null || productDTO.getName().isEmpty()
                || productDTO.getUnitPrice() == null || productDTO.getDescription() == null
                || productDTO.getStock() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a product");
        }
        try {
            Optional<Product> product = productRepository.findByName(productDTO.getName());
            Category category = categoryRepository.findByName(productDTO.getCategory()).orElseThrow(
                    () -> new CategoryNotFoundException("Provided category name DOES NOT match any category name"));
            if (product.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The product already exists");
            } else {
                Product newProduct = Product.builder().name(productDTO.getName()).unitPrice(productDTO.getUnitPrice())
                        .category(category).description(productDTO.getDescription()).stock(productDTO.getStock())
                        .build();
                productRepository.save(newProduct);
                return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product NOT created. " + e);
        }
    }

    public ResponseEntity<?> deleteProduct(Long productId) {
        try {
            Product product = productRepository.findByProductId(productId)
                    .orElseThrow(() -> new ProductNotFoundException("ProductId DOES NOT match any productId"));
            productRepository.delete(product);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product CANNOT be deleted");
        }
    }

    public ResponseEntity<?> modifyProductPrice(ProductPriceDTO productPriceDTO) {
        if (productPriceDTO.getUnitPrice() == null || productPriceDTO.getProductId() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Missing data to modify product price");
        }
        try {
            Product product = productRepository.findByProductId(productPriceDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("ProductId DOES NOT match any productId"));
            product.setUnitPrice(productPriceDTO.getUnitPrice());
            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.OK).body("Product price updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product price CANNOT be updated");
        }
    }

    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    public ResponseEntity<?> addProductStock(ProductStockDTO productStockDTO) {
        if (productStockDTO.getAddStock() == null || productStockDTO.getProductId() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Missing data to add product stock");
        }
        try {
            Product product = productRepository.findByProductId(productStockDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("ProductId DOES NOT match any productId"));
            product.setStock(product.getStock() + productStockDTO.getAddStock());
            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.OK).body("Product price updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product stock CANNOT be updated");
        }
    }

    public ResponseEntity<?> filterProducts(FilterProductDTO filterProductDTO) {
        try {
            List<Product> filteredProducts = new ArrayList<>();
            List<Product> allProducts = productRepository.findAll();
            filteredProducts = filterByName(allProducts, filterProductDTO.getProductName());
            if (!(Objects.equals(filterProductDTO.getCategoryName(), "")) && filterProductDTO.getCategoryName() != null) {
                Category category = categoryRepository.findByName(filterProductDTO.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException("Provided category name DOES NOT match any category name"));
                filteredProducts = filterByCategory(filteredProducts, category);
            }
            filteredProducts = filterByMinUnitPrice(filteredProducts, filterProductDTO.getMinUnitPrice());
            filteredProducts = filterByMaxUnitPrice(filteredProducts, filterProductDTO.getMaxUnitPrice());
            filteredProducts = filterByMinStock(filteredProducts, filterProductDTO.getMinStock());
            filteredProducts = filterByMaxStock(filteredProducts, filterProductDTO.getMaxStock());
            return ResponseEntity.status(HttpStatus.OK).body(filteredProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT filter at the moment. " + e);
        }
    }

    private List<Product> filterByMaxStock(List<Product> allProducts, Integer maxStock) {
        List<Product> response = new ArrayList<>();
        if (maxStock == null || maxStock == 0){
            response.addAll(allProducts);
        }
        else {
            for (Product product : allProducts) {
                if (product.getStock() < maxStock) {
                    response.add(product);
                }
            }
        }
        return response;
    }

    private List<Product> filterByMinStock(List<Product> allProducts, Integer minStock) {
        List<Product> response = new ArrayList<>();
        if (minStock == null || minStock == 0){
            response.addAll(allProducts);
        }
        else {
            for (Product product : allProducts) {
                if (product.getStock() > minStock) {
                    response.add(product);
                }
            }
        }
        return response;
    }

    private List<Product> filterByMaxUnitPrice(List<Product> allProducts, Double maxUnitPrice) {
        List<Product> response = new ArrayList<>();
        if (maxUnitPrice == null || maxUnitPrice == 0.00){
            response.addAll(allProducts);
        }
        else {
            for (Product product : allProducts) {
                if (product.getUnitPrice() < maxUnitPrice) {
                    response.add(product);
                }
            }
        }
        return response;
    }

    private List<Product> filterByMinUnitPrice(List<Product> allProducts, Double minUnitPrice) {
        List<Product> response = new ArrayList<>();
        if (minUnitPrice == null || minUnitPrice == 0.00){
            response.addAll(allProducts);
        }
        else {
            for (Product product : allProducts) {
                if (product.getUnitPrice() > minUnitPrice) {
                    response.add(product);
                }
            }
        }
        return response;
    }

    private List<Product> filterByCategory(List<Product> allProducts, Category category) {
        List<Product> response = new ArrayList<>();
        if (category == null) {
            response.addAll(allProducts);
        } else {
            for (Product product : allProducts) {
                if (product.getCategory().equals(category)) {
                    response.add(product);
                }
            }
        }
        return response;
    }

    private List<Product> filterByName(List<Product> allProducts, String productName) {
        List<Product> response = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                response.add(product);
            }
        }
        return response;
    }
}
