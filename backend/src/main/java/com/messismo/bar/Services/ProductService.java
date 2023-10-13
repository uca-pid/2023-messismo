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
        if (productDTO.getCategory() == null || productDTO.getName() == null || productDTO.getName().isEmpty() || productDTO.getUnitPrice() == null || productDTO.getDescription() == null || productDTO.getStock() == null || productDTO.getUnitCost() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a product");
        }
        if(productDTO.getUnitCost()<=0 || productDTO.getStock()<0 || productDTO.getUnitPrice()<0){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Some values cannot be less than zero. Please check.");
        }
        try {
            Optional<Product> product = productRepository.findByName(productDTO.getName());
            Category category = categoryRepository.findByName(productDTO.getCategory()).orElseThrow(() -> new CategoryNotFoundException("Provided category name DOES NOT match any category name"));
            if (product.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The product already exists");
            } else {
                Product newProduct = Product.builder().name(productDTO.getName()).unitPrice(productDTO.getUnitPrice()).category(category).description(productDTO.getDescription()).stock(productDTO.getStock()).unitCost(productDTO.getUnitCost()).build();
                productRepository.save(newProduct);
                return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product NOT created. ");
        }
    }

    public ResponseEntity<?> deleteProduct(Long productId) {
        try {
            Product product = productRepository.findByProductId(productId).orElseThrow(() -> new ProductNotFoundException("ProductId DOES NOT match any productId"));
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
        if(productPriceDTO.getUnitPrice()<=0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product price CANNOT be less than 0.");
        }
        try {
            Product product = productRepository.findByProductId(productPriceDTO.getProductId()).orElseThrow(() -> new ProductNotFoundException("ProductId DOES NOT match any productId"));
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

    public ResponseEntity<?> modifyProductStock(ProductStockDTO productStockDTO) {
        if (productStockDTO.getModifyStock() == null || productStockDTO.getProductId() == null || productStockDTO.getOperation() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing data to add product stock");
        }
        if (productStockDTO.getModifyStock() < 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Stock quantity cannot be less than 0");
        }
        try {
            Product product = productRepository.findByProductId(productStockDTO.getProductId()).orElseThrow(() -> new ProductNotFoundException("ProductId DOES NOT match any productId"));
            if (Objects.equals(productStockDTO.getOperation(), "add")) {
                product.setStock(product.getStock() + productStockDTO.getModifyStock());
            } else if (Objects.equals(productStockDTO.getOperation(), "substract")) {
                product.setStock(product.getStock() - productStockDTO.getModifyStock());
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Incorrect type of operation");
            }
            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.OK).body("Product stock updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product stock CANNOT be updated");
        }
    }

    public ResponseEntity<?> filterProducts(FilterProductDTO filterProductDTO) {
        try {
            List<Product> filteredProducts = new ArrayList<>();
            List<Product> allProducts = productRepository.findAll();

            filteredProducts = filterByName(allProducts, filterProductDTO.getProductName());
            if (filterProductDTO.getCategories() != null) {
                for(String aCategory : filterProductDTO.getCategories()){
                    Category category = categoryRepository.findByName(aCategory).orElseThrow(() -> new CategoryNotFoundException("Provided category name DOES NOT match any category name"));
                    filteredProducts = filterByCategory(filteredProducts, category);
                }
            }
            filteredProducts = filterByMinUnitPrice(filteredProducts, filterProductDTO.getMinUnitPrice());
            filteredProducts = filterByMaxUnitPrice(filteredProducts, filterProductDTO.getMaxUnitPrice());
            filteredProducts = filterByMinUnitCost(filteredProducts, filterProductDTO.getMinUnitCost());
            filteredProducts = filterByMaxUnitCost(filteredProducts, filterProductDTO.getMaxUnitCost());
            filteredProducts = filterByMinStock(filteredProducts, filterProductDTO.getMinStock());
            filteredProducts = filterByMaxStock(filteredProducts, filterProductDTO.getMaxStock());
            return ResponseEntity.status(HttpStatus.OK).body(filteredProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT filter at the moment.");
        }
    }

    private List<Product> filterByMaxUnitCost(List<Product> allProducts, Double maxUnitCost) {
        List<Product> response = new ArrayList<>();
        if (maxUnitCost == null || maxUnitCost == 0.00) {
            response.addAll(allProducts);
        } else {

            for (Product product : allProducts) {
                if (product.getUnitCost() < maxUnitCost) {
                    response.add(product);
                }
            }
        }
        return response;
    }

    private List<Product> filterByMinUnitCost(List<Product> allProducts, Double minUnitCost) {
        List<Product> response = new ArrayList<>();
        if (minUnitCost == null || minUnitCost == 0.00) {
            response.addAll(allProducts);
        } else {

            for (Product product : allProducts) {
                if (product.getUnitCost() > minUnitCost) {
                    response.add(product);
                }
            }
        }
        return response;
    }

    public List<Product> filterByMaxStock(List<Product> allProducts, Integer maxStock) {
        List<Product> response = new ArrayList<>();
        if (maxStock == null || maxStock == 0) {
            response.addAll(allProducts);
        } else {

            for (Product product : allProducts) {
                if (product.getStock() < maxStock) {
                    response.add(product);
                }
            }
        }
        return response;
    }

    public List<Product> filterByMinStock(List<Product> allProducts, Integer minStock) {
        List<Product> response = new ArrayList<>();
        if (minStock == null || minStock == 0) {
            response.addAll(allProducts);
        } else {
            for (Product product : allProducts) {
                if (product.getStock() > minStock) {
                    response.add(product);
                }
            }
        }
        return response;
    }

    public List<Product> filterByMaxUnitPrice(List<Product> allProducts, Double maxUnitPrice) {
        List<Product> response = new ArrayList<>();
        if (maxUnitPrice == null || maxUnitPrice == 0.00) {
            response.addAll(allProducts);
        } else {
            for (Product product : allProducts) {
                if (product.getUnitPrice() < maxUnitPrice) {
                    response.add(product);
                }
            }
        }
        return response;
    }

    public List<Product> filterByMinUnitPrice(List<Product> allProducts, Double minUnitPrice) {
        List<Product> response = new ArrayList<>();
        if (minUnitPrice == null || minUnitPrice == 0.00) {
            response.addAll(allProducts);
        } else {
            for (Product product : allProducts) {
                if (product.getUnitPrice() > minUnitPrice) {
                    response.add(product);
                }
            }
        }
        return response;
    }

    public List<Product> filterByCategory(List<Product> allProducts, Category category) {
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

    public List<Product> filterByName(List<Product> allProducts, String productName) {
        List<Product> response = new ArrayList<>();
        if (productName == null || productName.isEmpty()) {
            response.addAll(allProducts);
        } else {
            for (Product product : allProducts) {
                if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                    response.add(product);
                }
            }
        }
        return response;
    }
}
