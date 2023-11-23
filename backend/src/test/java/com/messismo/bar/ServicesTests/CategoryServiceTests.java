package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.CategoryRequestDTO;
import com.messismo.bar.Entities.Category;
import com.messismo.bar.Entities.Product;
import com.messismo.bar.Exceptions.CategoryHasAtLeastOneProductAssociated;
import com.messismo.bar.Exceptions.CategoryNotFoundException;
import com.messismo.bar.Exceptions.ExistingCategoryFoundException;
import com.messismo.bar.Repositories.CategoryRepository;
import com.messismo.bar.Repositories.ProductRepository;
import com.messismo.bar.Services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class CategoryServiceTests {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCategories() {

        Category category1 = Category.builder().categoryId(1L).name("Category1").build();
        Category category2 = Category.builder().categoryId(2L).name("Category2").build();
        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);
        Assertions.assertEquals(categoryService.getAllCategories(), categories);

    }

    @Test
    public void testAddCategory() throws Exception {

        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("New Category").build();

        when(categoryRepository.findByName(requestDTO.getCategoryName())).thenReturn(Optional.empty());
        Assertions.assertEquals("Category created successfully", categoryService.addCategory(requestDTO));

    }

    @Test
    public void testAddCategory_CategoryAlreadyExists() {

        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("Existing Category").build();
        when(categoryRepository.findByName(requestDTO.getCategoryName())).thenReturn(Optional.of(new Category()));

        ExistingCategoryFoundException exception = assertThrows(ExistingCategoryFoundException.class, () -> {
            categoryService.addCategory(requestDTO);
        });
        assertEquals("Provided category name ALREADY exists", exception.getMessage());

    }

    @Test
    public void testAddCategory_CategoryNotCreated() {

        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("New Category").build();

        when(categoryRepository.findByName(requestDTO.getCategoryName())).thenReturn(Optional.empty());
        doThrow(new DataIntegrityViolationException("Error saving")).when(categoryRepository).save(any(Category.class));
        Exception exception = assertThrows(Exception.class, () -> {
            categoryService.addCategory(requestDTO);
        });
        assertEquals("Category NOT created", exception.getMessage());

    }

    @Test
    public void testDeleteCategory_Success() throws Exception {

        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("CategoryToDelete").build();
        Category categoryToDelete = new Category();
        List<Product> emptyProductList = new ArrayList<>();

        when(categoryRepository.findByName(requestDTO.getCategoryName())).thenReturn(Optional.of(categoryToDelete));
        when(productRepository.findByCategory(categoryToDelete)).thenReturn(emptyProductList);
        Assertions.assertEquals(categoryService.deleteCategory(requestDTO), "Category deleted successfully");

    }

    @Test
    public void testDeleteCategory_CategoryNotFound() {

        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("NonExistentCategory").build();

        when(categoryRepository.findByName(requestDTO.getCategoryName())).thenReturn(Optional.empty());
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.deleteCategory(requestDTO);
        });
        assertEquals("Provided category DOES NOT match any category name", exception.getMessage());

    }

    @Test
    public void testDeleteCategory_CategoryWithProducts() {

        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("CategoryWithProducts").build();
        Category categoryWithProducts = new Category();
        List<Product> productsWithCategory = new ArrayList<>();
        productsWithCategory.add(new Product());

        when(categoryRepository.findByName(requestDTO.getCategoryName())).thenReturn(Optional.of(categoryWithProducts));
        when(productRepository.findByCategory(categoryWithProducts)).thenReturn(productsWithCategory);
        CategoryHasAtLeastOneProductAssociated exception = assertThrows(CategoryHasAtLeastOneProductAssociated.class, () -> {
            categoryService.deleteCategory(requestDTO);
        });
        assertEquals("The provided category has associated one or more products. Please delete them first", exception.getMessage());

    }

}
