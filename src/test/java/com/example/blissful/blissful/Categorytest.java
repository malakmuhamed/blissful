package com.example.blissful.blissful;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
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
//getcategories 
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
    //addcategory
    @Test
    public void testSaveCategory_ValidCategory() {
        // Prepare mock data
        Category category = new Category();
        category.setName("NewCategory");

        BindingResult bindingResult = new BeanPropertyBindingResult(category, "category");

        // Mock repository behavior
        when(categoryRepository.findByName(category.getName())).thenReturn(null); // Simulating category not existing

        // Call the method
        String viewName = categoryController.saveCategory(category, bindingResult);

        // Verify behavior
        assertEquals("redirect:/", viewName);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testSaveCategory_EmptyName() {
        // Prepare mock data
        Category category = new Category();
        category.setName("");

        BindingResult bindingResult = new BeanPropertyBindingResult(category, "category");

        // Call the method
        String viewName = categoryController.saveCategory(category, bindingResult);

        // Verify behavior
        assertEquals("addCategory", viewName);
        assertTrue(bindingResult.hasErrors());
        assertEquals("Category name is required.", bindingResult.getFieldError("name").getDefaultMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testSaveCategory_DuplicateName() {
        // Prepare mock data
        Category category = new Category();
        category.setName("DuplicateCategory");

        BindingResult bindingResult = new BeanPropertyBindingResult(category, "category");

        // Mock repository behavior
        when(categoryRepository.findByName(category.getName())).thenReturn(new Category()); // Simulating category already exists

        // Call the method
        String viewName = categoryController.saveCategory(category, bindingResult);

        // Verify behavior
        assertEquals("addCategory", viewName);
        assertTrue(bindingResult.hasErrors());
        assertEquals("Category name already exists.", bindingResult.getFieldError("name").getDefaultMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }
    @Test
    public void testSaveCategory_withnonletters() {
        // Prepare mock data
        Category category = new Category();
        category.setName(",");

        BindingResult bindingResult = new BeanPropertyBindingResult(category, "category");

        // Mock repository behavior
        when(categoryRepository.findByName(category.getName())).thenReturn(new Category()); // Simulating category already exists

        // Call the method
        String viewName = categoryController.saveCategory(category, bindingResult);

        // Verify behavior
        assertEquals("addCategory", viewName);
        assertTrue(bindingResult.hasErrors());
        assertEquals("Category name must contain only letters.", bindingResult.getFieldError("name").getDefaultMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }
    
    //updateprod
    @Test
    public void testUpdateProduct_ValidationErrors() {
        // Prepare mock data
        product existingProduct = new product();
        existingProduct.setId(1);
        existingProduct.setName("TestProduct");
        existingProduct.setPrice(100);
        existingProduct.setOffer(10);
        existingProduct.setQuantity(5);
    
        product updatedProduct = new product();
        updatedProduct.setId(1); // Setting the same ID
        updatedProduct.setName(""); // Empty name to trigger validation error
        updatedProduct.setPrice(-50); // Invalid price to trigger validation error
        updatedProduct.setOffer(-5); // Invalid offer to trigger validation error
        updatedProduct.setQuantity(0); // Invalid quantity to trigger validation error
    
        // Mock repository behavior
        when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));
    
        BindingResult bindingResult = new BeanPropertyBindingResult(updatedProduct, "updatedProduct");
        Model model = mock(Model.class);
    
        // Call the method to be tested
        String viewName = categoryController.updateProduct(1, updatedProduct, bindingResult, model);
    
        // Verify behavior
        assertTrue(bindingResult.hasErrors()); // Check if there are binding errors
        assertEquals(4, bindingResult.getErrorCount()); // Ensure all fields have validation errors
       // verify(productRepository, never()).save(any(product.class)); // Ensure that save method was never called
    }
    @Test
public void testUpdateProduct_ValidData() {
    // Prepare existing product
    product existingProduct = new product();
    existingProduct.setId(1);
    existingProduct.setName("ExistingProduct");
    existingProduct.setPrice(100);
    existingProduct.setOffer(10);
    existingProduct.setQuantity(20);
    existingProduct.setCategory(new Category(1, "TestCategory"));

    // Mock repository behavior to return existing product for findById
    when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));

    // Prepare updated product data
    product updatedProduct = new product();
    updatedProduct.setId(1);
    updatedProduct.setName("UpdatedProduct");
    updatedProduct.setPrice(120);
    updatedProduct.setOffer(15);
    updatedProduct.setQuantity(25);
    updatedProduct.setCategory(new Category(1, "Test Category"));

    BindingResult bindingResult = new BeanPropertyBindingResult(updatedProduct, "updatedProduct");
    Model model = mock(Model.class);

    // Call the method to be tested
    String viewName = categoryController.updateProduct(1, updatedProduct, bindingResult, model);

    // Verify behavior
    assertEquals("redirect:/category/1", viewName); // Check if it redirects to category page
   // verify(productRepository).save(any(Product.class)); // Ensure that save method was called once
   // verify(model, never()).addAttribute(eq("errorMessages"), anyString()); // Verify no error message attribute added
}

    
   
    

}


