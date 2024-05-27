package com.example.blissful.blissful.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.example.blissful.blissful.models.Category;
import com.example.blissful.blissful.models.product;
import com.example.blissful.blissful.repository.CategoryRepository;
import com.example.blissful.blissful.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public ModelAndView getCategories(HttpSession session) {
        ModelAndView mav = new ModelAndView();

        // Check if the user is not logged in or is not an admin
        String userType = (String) session.getAttribute("type");
        if (userType == null || !userType.equals("admin")) {
            // Redirect to the login page if not logged in or not an admin
            mav.setViewName("redirect:/user/login");
            return mav;
        }

        // User is an admin, proceed with the original logic
        mav.setViewName("list-category.html");
        List<Category> categories = this.categoryRepository.findAll();
        mav.addObject("categories", categories);
        return mav;
    }

    @GetMapping("addCategory")
    public ModelAndView addCategory(HttpSession session) {
        ModelAndView mav = new ModelAndView();

        // Check if the user is not logged in or is not an admin
        String userType = (String) session.getAttribute("type");
        if (userType == null || !userType.equals("admin")) {
            // Redirect to the login page if not logged in or not an admin
            mav.setViewName("redirect:/user/login");
            return mav;
        }

        // User is an admin, proceed with the original logic
        mav.setViewName("addCategory.html");
        Category newCategory = new Category();
        mav.addObject("category", newCategory);
        return mav;
    }

    @PostMapping("addCategory")
    public String saveCategory(@ModelAttribute("category") @Validated Category category,
            BindingResult bindingResult) {
        // Validate category name is not empty
        if (category.getName() == null || category.getName().isEmpty()) {
            bindingResult.rejectValue("name", "error.category", "Category name is required.");
        } else if (!category.getName().matches("[a-zA-Z]+")) {
            bindingResult.rejectValue("name", "error.category", "Category name must contain only letters.");
        }

        // Check if category name already exists
        Category existingCategory = categoryRepository.findByName(category.getName());
        if (existingCategory != null) {
            bindingResult.rejectValue("name", "error.category", "Category name already exists.");
        }

        if (bindingResult.hasErrors()) {
            return "addCategory"; // Return to the form with errors
        }

        this.categoryRepository.save(category);
        return "redirect:/"; // Redirect to the homepage after saving the category
    }

    @GetMapping("/category/{id}")
    public ModelAndView getCategory(@PathVariable("id") int id, HttpSession session) {
        ModelAndView mav = new ModelAndView();

        // Check if the user is not logged in or is not an admin
        String userType = (String) session.getAttribute("type");
        if (userType == null || !userType.equals("admin")) {
            // Redirect to the login page if not logged in or not an admin
            mav.setViewName("redirect:/user/login");
            return mav;
        }

        // User is an admin, proceed with the original logic
        mav.setViewName("list-prod.html");
        List<product> prods = this.productRepository.findAllByCategoryId(id); // Ensure class name starts with an
                                                                              // uppercase letter
        mav.addObject("prods", prods);
        return mav;
    }

    @GetMapping("/editprod/{id}")
    public ModelAndView editProduct(@PathVariable("id") int id, HttpSession session) {
        ModelAndView mav = new ModelAndView();

        // Check if the user is not logged in or is not an admin
        String userType = (String) session.getAttribute("type");
        if (userType == null || !userType.equals("admin")) {
            // Redirect to the login page if not logged in or not an admin
            mav.setViewName("redirect:/user/login");
            return mav;
        }

        // User is an admin, proceed with the original logic
        mav.setViewName("edit-product.html");
        product product = this.productRepository.findById(id).orElse(null); // Ensure class name starts with an
                                                                            // uppercase letter
        if (product != null) {
            mav.addObject("product", product); // Add the product to the model
            List<Category> categories = this.categoryRepository.findAll(); // Fetch all categories for the select box
            mav.addObject("categories", categories); // Add categories to the model
        } else {
            // Handle case where product is not found
            mav.addObject("errorMessage", "Product not found");
        }
        return mav;
    }

    @PostMapping("/editprod/{id}")
    public String updateProduct(@PathVariable("id") int id,
            @ModelAttribute @Validated product updatedProduct,
            BindingResult bindingResult,
            Model model) {
        product existingProduct = this.productRepository.findById(id).orElse(null); // Find the existing product by its
                                                                                    // ID

        if (existingProduct == null) {
            // Handle case where product is not found
            return "redirect:/"; // Redirect to the homepage or an error page
        }

        List<String> errorMessages = new ArrayList<>();

        // Validate product name
        if (updatedProduct.getName() == null || updatedProduct.getName().isEmpty()) {
            bindingResult.rejectValue("name", "error.name.required", "Product name is required.");
            errorMessages.add("Product name is required.");
        } else if (!updatedProduct.getName().matches("[a-zA-Z]+")) {
            bindingResult.rejectValue("name", "error.name.invalidFormat", "Product name must contain only letters.");
            errorMessages.add("Product name must contain only letters.");
        }

        // Validate price
        if (updatedProduct.getPrice() <= 0) {
            bindingResult.rejectValue("price", "error.price.invalidFormat", "Price must be a positive number.");
            errorMessages.add("Price must be a positive number.");
        } else if (updatedProduct.getPrice() < 0) {
            bindingResult.rejectValue("price", "error.price.invalidFormat", "Price cannot be negative.");
            errorMessages.add("Price cannot be negative.");
        }

        // Validate offer
        if (updatedProduct.getOffer() < 0) {
            bindingResult.rejectValue("offer", "error.offer.invalidFormat", "Offer must be a non-negative integer.");
            errorMessages.add("Offer must be a non-negative integer.");
        }

        // Validate quantity
        if (updatedProduct.getQuantity() <= 0) {
            bindingResult.rejectValue("quantity", "error.quantity.invalidFormat",
                    "Quantity must be a positive integer.");
            errorMessages.add("Quantity must be a positive integer.");
        }

        // Calculate the discounted price
        double price = updatedProduct.getPrice();
        double offer = updatedProduct.getOffer();
        double discountedPrice = price - (price * (offer / 100.0));
        updatedProduct.setDiscountedPrice(discountedPrice);

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", updatedProduct);
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("errorMessages", errorMessages);
            return "edit-product"; // Return to the edit-product page
        }

        // Update the existing product with the new data
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setOffer(updatedProduct.getOffer());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setCategory(updatedProduct.getCategory());

        // Save the updated product
        try {
            this.productRepository.save(existingProduct);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle database save exception
            model.addAttribute("errorMessages", "An error occurred while updating the product.");
            model.addAttribute("product", updatedProduct);
            model.addAttribute("categories", categoryRepository.findAll());
            return "edit-product"; // Return to the edit-product page
        }

        return "redirect:/category/" + existingProduct.getCategory().getId(); // Redirect to the category page after
                                                                              // successful update
    }
    

}