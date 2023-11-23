package com.messismo.bar.Services;

import com.messismo.bar.DTOs.*;
import com.messismo.bar.Entities.Category;
import com.messismo.bar.Entities.Product;
import com.messismo.bar.Exceptions.CategoryNotFoundException;
import com.messismo.bar.Exceptions.ExistingCategoryFoundException;
import com.messismo.bar.Exceptions.ExistingProductFoundException;
import com.messismo.bar.Exceptions.ProductNotFoundException;
import com.messismo.bar.Repositories.CategoryRepository;
import com.messismo.bar.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final CategoryService categoryService;

    public String addProduct(ProductDTO productDTO) throws Exception {
        try {
            Optional<Product> existingProduct = productRepository.findByName(productDTO.getName());
            if (existingProduct.isPresent()) {
                throw new ExistingProductFoundException("The product already exists");
            }
            if (productDTO.getNewCategory() == Boolean.TRUE) {
                CategoryRequestDTO categoryRequestDTO = CategoryRequestDTO.builder().categoryName(productDTO.getCategory()).build();
                categoryService.addCategory(categoryRequestDTO);
            }
            Category category = categoryRepository.findByName(productDTO.getCategory()).orElseThrow(() -> new CategoryNotFoundException("Provided category name DOES NOT match any category name"));
            Product newProduct = new Product(productDTO.getName(), productDTO.getUnitPrice(), productDTO.getUnitCost(), productDTO.getDescription(), productDTO.getStock(), category);
            productRepository.save(newProduct);
            return "Product created successfully";

        } catch (CategoryNotFoundException | ExistingProductFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Product NOT created");
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public String deleteProduct(Long productId) throws Exception {
        try {
            Product product = productRepository.findByProductId(productId).orElseThrow(() -> new ProductNotFoundException("ProductId DOES NOT match any productId"));
            productRepository.delete(product);
            return "Product deleted successfully";
        } catch (ProductNotFoundException e) {
            throw e;
        }
        catch(Exception e){
            throw new Exception("Product CANNOT be deleted");
        }
    }

    public String modifyProductPrice(ProductPriceDTO productPriceDTO) throws Exception {
        try {
            Product product = productRepository.findByProductId(productPriceDTO.getProductId()).orElseThrow(() -> new ProductNotFoundException("ProductId DOES NOT match any productId"));
            product.updateUnitPrice(productPriceDTO.getUnitPrice());
            productRepository.save(product);
            return "Product price updated successfully";
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("");
        }
    }

    public String modifyProductCost(ProductPriceDTO productPriceDTO) throws Exception {
        try {
            Product product = productRepository.findByProductId(productPriceDTO.getProductId()).orElseThrow(() -> new ProductNotFoundException("ProductId DOES NOT match any productId"));
            product.updateUnitCost(productPriceDTO.getUnitPrice());
            productRepository.save(product);
            return "Product cost updated successfully";
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("");
        }
    }


    public String modifyProductStock(ProductStockDTO productStockDTO) throws Exception {
        try {
            Product product = productRepository.findByProductId(productStockDTO.getProductId()).orElseThrow(() -> new ProductNotFoundException("ProductId DOES NOT match any productId"));
            product.updateStock(productStockDTO.getOperation(), productStockDTO.getModifyStock());
            productRepository.save(product);
            return "Product stock updated successfully";
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public List<Product> filterProducts(FilterProductDTO filterProductDTO) throws Exception {
        try {
            List<Product> filteredProducts = new ArrayList<>();
            List<Product> allProducts = productRepository.findAll();
            filteredProducts = filterByName(allProducts, filterProductDTO.getProductName());
            List<Product> filteredProductsByCategory = new ArrayList<>();
            if (filterProductDTO.getCategories() != null) {
                for (String aCategory : filterProductDTO.getCategories()) {
                    Category category = categoryRepository.findByName(aCategory).orElseThrow(() -> new CategoryNotFoundException("Provided category name DOES NOT match any category name"));
                    filteredProductsByCategory.addAll(filterByCategory(filteredProducts, category));
                }
                filteredProducts = filteredProductsByCategory;
            }
            filteredProducts = filterByMinUnitPrice(filteredProducts, filterProductDTO.getMinUnitPrice());
            filteredProducts = filterByMaxUnitPrice(filteredProducts, filterProductDTO.getMaxUnitPrice());
            filteredProducts = filterByMinUnitCost(filteredProducts, filterProductDTO.getMinUnitCost());
            filteredProducts = filterByMaxUnitCost(filteredProducts, filterProductDTO.getMaxUnitCost());
            filteredProducts = filterByMinStock(filteredProducts, filterProductDTO.getMinStock());
            filteredProducts = filterByMaxStock(filteredProducts, filterProductDTO.getMaxStock());
            return filteredProducts;
        } catch (CategoryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("CANNOT filter at the moment");
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

    public Product getProductByName(String productName) throws Exception {
        return productRepository.findByName(productName).orElseThrow(() -> new Exception("ProductName DOES NOT match any productName"));
    }
}
