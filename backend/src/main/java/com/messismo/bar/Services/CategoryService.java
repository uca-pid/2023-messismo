package com.messismo.bar.Services;

import com.messismo.bar.DTOs.CategoryRequestDTO;
import com.messismo.bar.Entities.Category;
import com.messismo.bar.Entities.Product;
import com.messismo.bar.Exceptions.CategoryNotFoundException;
import com.messismo.bar.Repositories.CategoryRepository;
import com.messismo.bar.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    public ResponseEntity<?> addCategory(CategoryRequestDTO categoryRequestDTO) {
        if (categoryRequestDTO.getCategoryName() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a category");
        }
        try {
            Optional<Category> category = categoryRepository.findByName(categoryRequestDTO.getCategoryName());
            if (category.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The category already exists");
            } else {
                Category newCategory = Category.builder().name(categoryRequestDTO.getCategoryName()).build();
                categoryRepository.save(newCategory);
                return ResponseEntity.status(HttpStatus.CREATED).body("Category created successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Category NOT created.");
        }
    }

    public ResponseEntity<?> deleteCategory(CategoryRequestDTO categoryRequestDTO) {
        if (categoryRequestDTO.getCategoryName() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to delete a category");
        }
        try {
            Category category = categoryRepository.findByName(categoryRequestDTO.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException("Provided category name DOES NOT match any category name"));
            List<Product> productsByCategory = productRepository.findByCategory(category);
            if (productsByCategory.isEmpty()) {
                categoryRepository.delete(category);
                return ResponseEntity.status(HttpStatus.OK).body("Category deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The provided category has associated one or more products. Please delete them first");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Category NOT deleted.");
        }
    }

    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryRepository.findAll());
    }

    public Category getCategoryByName(String categoryName) throws Exception {
        return categoryRepository.findByName(categoryName).orElseThrow(() -> new Exception("CategoryName DOES NOT match any categoryName"));
    }
}
