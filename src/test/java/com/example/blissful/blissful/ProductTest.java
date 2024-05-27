package com.example.blissful.blissful;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.blissful.blissful.controllers.productcontroller;
import com.example.blissful.blissful.models.product;
import com.example.blissful.blissful.repository.CategoryRepository;
import com.example.blissful.blissful.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductTest {

    @InjectMocks
    private productcontroller productController;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private Model model;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
//addprod
    @Test
    public void testValidation() {
        // Mock necessary objects
        product product = new product();
        product.setName(",");
        product.setPrice(-10); // negative price, should fail validation
        product.setOffer(1); // negative offer, should fail validation
        // product.setPhoto("ana.png");
        product.setQuantity(0);
        MultipartFile file = mock(MultipartFile.class);
        BindingResult bindingResult = new BeanPropertyBindingResult(product, "product");
        // Correctly initialize the BindingResult
        // BindingResult bindingResult = mock(BindingResult.class);
        Model model = mock(Model.class);

        // Call the method
        // String viewName = productController.saveprod(product, file, bindingResult,
        // model);
        String viewName = productController.saveprod(product, file, bindingResult, model);
        // Debug: Print out the contents of the bindingResult
        System.out.println("Errors detected: " + bindingResult.hasErrors());
        if (bindingResult.hasErrors()) {
            System.out.println("Error count: " + bindingResult.getErrorCount());
            for (ObjectError error : bindingResult.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
        }

        // Verify behavior
        assertEquals("addprod", viewName); // Assuming the controller returns "addprod" in case of errors
        assertTrue(bindingResult.hasErrors());
        // assertNotNull(bindingResult.getFieldError("price"));
    }

    @Test
    public void testSuccessfulValidation() {

        // Mock necessary objects
        product product = new product();
        product.setName("shahd");
        product.setPrice(10);
        product.setOffer(1);
        product.setQuantity(1);
        MultipartFile file = mock(MultipartFile.class);
        BindingResult bindingResult = new BeanPropertyBindingResult(product, "product");

        Model model = mock(Model.class);

        String viewName = productController.saveprod(product, file, bindingResult, model);
        // Debug: Print out the contents of the bindingResult

        // Verify behavior
        assertEquals("redirect:/", viewName); // Assuming the controller returns "addprod" in case of errors
        assertFalse(bindingResult.hasErrors());
        // assertNotNull(bindingResult.getFieldError("price"));
    }

    @Test
    public void testDeleteProduct() {
        // Mock necessary objects
        String productName = "TestProduct";
        product product1 = new product();
        product1.setName(productName);
        List<product> productList = new ArrayList<>();
        productList.add(product1);

        // Mock behavior of the productRepository
        when(productRepository.findAllByName(productName)).thenReturn(productList);

        // Call the method
        String result = productController.deleteprod(productName, model);

        // Verify behavior
        assertEquals("redirect:/", result); // Assuming the controller returns "redirect:/" after successful deletion
        verify(productRepository, times(1)).delete(product1);
    }

    @Test
    public void testDeleteProduct_ProductNotFound() {
        // Mock necessary objects
        String productName = "NonExistingProduct";

        // Mock behavior of the productRepository
        when(productRepository.findAllByName(productName)).thenReturn(new ArrayList<>());

        // Call the method
        String result = productController.deleteprod(productName, model);

        // Verify behavior
        assertEquals("deleteprod", result); // Assuming the controller returns "deleteprod" when the product is not found
        verify(productRepository, never()).delete(any());
        verify(model, times(1)).addAttribute("errorMessage", "Product not found");
    }

    @Test
    public void testDeleteProduct_Exception() {
        // Mock necessary objects
        String productName = "TestProduct";
        product product1 = new product();
        product1.setName(productName);
        List<product> productList = new ArrayList<>();
        productList.add(product1);

        // Mock behavior of the productRepository to throw an exception
        when(productRepository.findAllByName(productName)).thenThrow(new RuntimeException());

        // Call the method
        String result = productController.deleteprod(productName, model);

        // Verify behavior
        assertEquals("deleteprod", result); // Assuming the controller returns "deleteprod" when an exception occurs
        verify(productRepository, never()).delete(any());
        verify(model, times(1)).addAttribute("errorMessage", "An error occurred while deleting the product");
    }
}
