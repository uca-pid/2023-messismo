package com.messismo.bar.Services;


import com.messismo.bar.DTOs.ProductDTO;
import com.messismo.bar.Entities.Menu;
import com.messismo.bar.Entities.Product;
import com.messismo.bar.Exceptions.ProductNotFoundException;
import com.messismo.bar.Repositories.MenuRepository;
import com.messismo.bar.Repositories.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final MenuRepository menuRepository;

    public ProductService(ProductRepository productRepository,MenuRepository menuRepository){
        this.productRepository=productRepository;
        this.menuRepository=menuRepository;
    }

    public ResponseEntity<?> modifyProductPrice(Long productId, Double price){
        try{
            Product product = productRepository.findByProductId(productId).orElseThrow(() -> new ProductNotFoundException("ProductId DOES NOT match any productId"));;
            product.setUnitPrice(price);
            return ResponseEntity.status(HttpStatus.OK).body("Product price updated successfully");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product price CANNOT be updated");
        }
    }

    public ResponseEntity<?> deleteProduct(Long productId) {
        try{
            Product product = productRepository.findByProductId(productId).orElseThrow(() -> new ProductNotFoundException("ProductId DOES NOT match any productId"));
            productRepository.delete(product);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product CANNOT be deleted");
        }
    }

    public ResponseEntity<?> addProduct(ProductDTO productDTO) {
        try{
            Optional<Product> product = productRepository.findByName(productDTO.getName());
            if (product.isPresent()) { // PRODUCT ALREADY EXISTS
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The product already exists");
            }
            else {
                Product newProduct = new Product();
                newProduct.setName(productDTO.getName());
                newProduct.setUnitPrice(productDTO.getUnitPrice());
                newProduct.setCategory(productDTO.getCategory());
                newProduct.setDescription(productDTO.getDescription());
                Set<Menu> menu = new HashSet<>();
                menu.add(menuRepository.findByMenuId(1L).get());
                newProduct.setMenus(menu);
                productRepository.save(newProduct);
                return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product NOT created");

        }
    }
}
