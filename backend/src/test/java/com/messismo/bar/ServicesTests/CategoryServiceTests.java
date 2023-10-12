package com.messismo.bar.ServicesTests;

import com.messismo.bar.DTOs.CategoryRequestDTO;
import com.messismo.bar.Entities.Product;
import com.messismo.bar.Repositories.CategoryRepository;
import com.messismo.bar.Entities.Category;
import com.messismo.bar.Repositories.ProductRepository;
import com.messismo.bar.Repositories.UserRepository;
import com.messismo.bar.Services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
        Assertions.assertEquals(categoryService.getAllCategories(), ResponseEntity.status(HttpStatus.OK).body(categories));
    }

    @Test
    public void testAddCategory() {

        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("New Category").build();

        when(categoryRepository.findByName(requestDTO.getCategoryName())).thenReturn(Optional.empty());
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.CREATED).body("Category created successfully"), categoryService.addCategory(requestDTO));
    }

    @Test
    public void testAddCategory_CategoryAlreadyExists() {

        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("Existing Category").build();

        when(categoryRepository.findByName(requestDTO.getCategoryName())).thenReturn(Optional.of(new Category()));
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.CONFLICT).body("The category already exists"), categoryService.addCategory(requestDTO));
    }

    @Test
    public void testAddCategory_MissingInformation() {

        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().build();

        Assertions.assertEquals(ResponseEntity.status(HttpStatus.CONFLICT).body("Missing information to create a category"), categoryService.addCategory(requestDTO));
    }

    @Test
    public void testDeleteCategory_Success() {

        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("CategoryToDelete").build();
        Category categoryToDelete = new Category();
        List<Product> emptyProductList = new ArrayList<>();

        when(categoryRepository.findByName(requestDTO.getCategoryName())).thenReturn(Optional.of(categoryToDelete));
        when(productRepository.findByCategory(categoryToDelete)).thenReturn(emptyProductList);
        Assertions.assertEquals(categoryService.deleteCategory(requestDTO), ResponseEntity.status(HttpStatus.OK).body("Category deleted successfully"));
    }

    @Test
    public void testDeleteCategory_CategoryNotFound() {

        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("NonExistentCategory").build();

        when(categoryRepository.findByName(requestDTO.getCategoryName())).thenReturn(Optional.empty());
        Assertions.assertEquals(categoryService.deleteCategory(requestDTO), ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Category NOT deleted."));
    }

    @Test
    public void testDeleteCategory_CategoryWithProducts() {
        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("CategoryWithProducts").build();
        Category categoryWithProducts = new Category();
        List<Product> productsWithCategory = new ArrayList<>();
        productsWithCategory.add(new Product());

        when(categoryRepository.findByName(requestDTO.getCategoryName())).thenReturn(Optional.of(categoryWithProducts));
        when(productRepository.findByCategory(categoryWithProducts)).thenReturn(productsWithCategory);
        Assertions.assertEquals(categoryService.deleteCategory(requestDTO), ResponseEntity.status(HttpStatus.CONFLICT).body("The provided category has associated one or more products. Please delete them first"));
    }

    @Test
    public void testDeleteCategory_MissingInformation() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        ResponseEntity<?> response = categoryService.deleteCategory(requestDTO);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("Missing information to delete a category", response.getBody());
    }

}
