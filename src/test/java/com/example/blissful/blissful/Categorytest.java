package com.example.blissful.blissful;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import com.example.blissful.blissful.controllers.CategoryController;
import com.example.blissful.blissful.models.Category;
import com.example.blissful.blissful.models.product;
import com.example.blissful.blissful.repository.CategoryRepository;
import com.example.blissful.blissful.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

public class Categorytest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCategories() {
        // Mock user session
        when(session.getAttribute("type")).thenReturn("admin");

        // Mock necessary objects
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        categories.add(new Category());

        // Mock behavior of the categoryRepository
        when(categoryRepository.findAll()).thenReturn(categories);

        // Call the method
        ModelAndView mav = categoryController.getCategories(session);

        // Verify behavior
        assertEquals("list-category.html", mav.getViewName());
        assertEquals(2, ((List<Category>) mav.getModel().get("categories")).size());
    }

    @Test
    public void testAddCategory() {
        // Mock user session
        when(session.getAttribute("type")).thenReturn("admin");

        // Call the method
        ModelAndView mav = categoryController.addCategory(session);

        // Verify behavior
        assertEquals("addCategory.html", mav.getViewName());
        assertEquals(Category.class, mav.getModel().get("category").getClass());
    }

    @Test
    public void testEditProduct() {
        // Mock user session
        when(session.getAttribute("type")).thenReturn("admin");

        // Mock necessary objects
        int productId = 1;
        product product = new product(); // Assuming Product is the correct class name
        product.setId(productId);
        product.setName("Test Product");
        Category category = new Category();
        category.setId(1); // Assuming Category has an ID field
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        // Mock behavior of the productRepository
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(categoryRepository.findAll()).thenReturn(categories);

        // Call the method
        ModelAndView mav = categoryController.editProduct(productId, session);

        // Verify behavior
        assertEquals("edit-product.html", mav.getViewName());
        assertEquals(product, mav.getModel().get("product"));
        assertEquals(categories, mav.getModel().get("categories"));
    }

    @Test
    public void testSaveCategory_withErrors() {
        // Mock necessary objects
        Category category = new Category();
        category.setName(""); // Set an invalid name to trigger validation error

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError())
                .thenReturn(new FieldError("category", "name", "Category name is required."));

        // Call the method
        String viewName = categoryController.saveCategory(category, bindingResult);

        // Verify behavior
        assertEquals("addCategory", viewName);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testSaveCategory_success() {
        // Mock necessary objects
        Category category = new Category();
        category.setName("ValidName");

        when(bindingResult.hasErrors()).thenReturn(false);

        // Call the method
        String viewName = categoryController.saveCategory(category, bindingResult);

        // Verify behavior
        assertEquals("redirect:/", viewName);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testUpdateProduct_withErrors() {
        // Mock necessary objects
        int productId = 1;
        product existingProduct = new product();
        existingProduct.setId(productId);
        product updatedProduct = new product();
        updatedProduct.setName(""); // Invalid name to trigger validation error

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(bindingResult.hasErrors()).thenReturn(true);

        // Call the method
        String viewName = categoryController.updateProduct(productId, updatedProduct, bindingResult, model);

        // Verify behavior
        assertEquals("edit-product", viewName);
        verify(productRepository, never()).save(any(product.class));
    }

    @Test
    public void testUpdateProduct_success() {
        // Mock necessary objects
        int productId = 1;
        product existingProduct = new product();
        existingProduct.setId(productId);

        Category category = new Category();
        category.setId(1);
        existingProduct.setCategory(category);

        product updatedProduct = new product();
        updatedProduct.setName("ValidName");
        updatedProduct.setPrice(100);
        updatedProduct.setOffer(10);
        updatedProduct.setQuantity(5);
        updatedProduct.setCategory(category); // Ensure the updated product also has a category

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(bindingResult.hasErrors()).thenReturn(false);

        // Call the method
        String viewName = categoryController.updateProduct(productId, updatedProduct, bindingResult, model);

        // Verify behavior
        assertEquals("redirect:/category/" + existingProduct.getCategory().getId(), viewName);
        verify(productRepository, times(1)).save(existingProduct);
    }
}
