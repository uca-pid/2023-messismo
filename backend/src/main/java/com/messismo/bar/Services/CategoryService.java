package com.messismo.bar.Services;

import com.messismo.bar.DTOs.CategoryRequestDTO;
import com.messismo.bar.Entities.Category;
import com.messismo.bar.Exceptions.*;
import com.messismo.bar.Repositories.CategoryRepository;
import com.messismo.bar.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    public String addCategory(CategoryRequestDTO categoryRequestDTO) throws Exception {
        try {
            Category category = categoryRepository.findByName(categoryRequestDTO.getCategoryName()).orElseThrow(() -> new ExistingCategoryFoundException("Provided category name ALREADY exists"));
            Category newCategory = Category.builder().name(categoryRequestDTO.getCategoryName()).build();
            categoryRepository.save(newCategory);
            return "Category created successfully";
        } catch (ExistingCategoryFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Category NOT created");
        }
    }

    public String deleteCategory(CategoryRequestDTO categoryRequestDTO) throws Exception {
        try {
            Category category = categoryRepository.findByName(categoryRequestDTO.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException("Provided category DOES NOT match any category name"));
            if (productRepository.findByCategory(category).isEmpty()) {
                categoryRepository.delete(category);
                return "Category deleted successfully";
            } else {
                throw new CategoryHasAtLeastOneProductAssociated("The provided category has associated one or more products. Please delete them first");
            }
        } catch (CategoryNotFoundException | CategoryHasAtLeastOneProductAssociated e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Category NOT deleted");
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryByName(String categoryName) throws Exception {
        return categoryRepository.findByName(categoryName).orElseThrow(() -> new CategoryNotFoundException("CategoryName DOES NOT match any categoryName"));
    }
}
