package com.example.blissful.blissful.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.blissful.blissful.models.Category;
import com.example.blissful.blissful.models.product;
import com.example.blissful.blissful.repository.CategoryRepository;
import com.example.blissful.blissful.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Controller
@RequestMapping("/")
public class productcontroller {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("addprod")
    public ModelAndView addprod(HttpSession session) {
        String userType = (String) session.getAttribute("type");

        // Check if the user is not logged in or is not an admin
        if (userType == null || !userType.equals("admin")) {
            // Redirect to the login page if not logged in or not an admin
            return new ModelAndView("redirect:/user/login");
        }

        ModelAndView mav = new ModelAndView("addprod.html");
        List<Category> allCategories = this.categoryRepository.findAll();
        mav.addObject("allCategories", allCategories);
        product newprod = new product();
        mav.addObject("prod", newprod);
        return mav;
    }

    @PostMapping("addprod")
    public String saveprod(@ModelAttribute @Validated product product,
            @RequestParam("file") MultipartFile file,
            BindingResult bindingResult,
            Model model) {
        List<String> errorMessages = new ArrayList<>();

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        product.setPhoto(filename);

        // Validate product name
        if (product.getName() == null || product.getName().isEmpty()) {
            bindingResult.rejectValue("name", "error.name.required", "Product name is required.");
            errorMessages.add("Product name is required.");
        } else if (!product.getName().matches("[a-zA-Z]+")) {
            bindingResult.rejectValue("name", "error.name.invalidFormat", "Product name must contain only letters.");
            errorMessages.add("Product name must contain only letters.");
        }
        // Validate price
        // Validate price
        if (product.getPrice() <= 0) {
            bindingResult.rejectValue("price", "error.price.invalidFormat", "Price must be a positive number.");
            errorMessages.add("Price must be a positive number.");
        } else if (product.getPrice() < 0) {
            bindingResult.rejectValue("price", "error.price.invalidFormat", "Price cannot be negative.");
            errorMessages.add("Price cannot be negative.");
        }

        // Validate offer
        if (product.getOffer() < 0) {
            bindingResult.rejectValue("offer", "error.offer.invalidFormat", "Offer must be a non-negative integer.");
            errorMessages.add("Offer must be a non-negative integer.");
        }

        // Validate quantity
        if (product.getQuantity() <= 0) {
            bindingResult.rejectValue("quantity", "error.quantity.invalidFormat",
                    "Quantity must be a positive integer.");
            errorMessages.add("Quantity must be a positive integer.");
        }

        // Calculate the discounted price
        double price = product.getPrice();
        double offer = product.getOffer();
        double discountedPrice = price - (price * (offer / 100.0));
        product.setDiscountedPrice(discountedPrice);

        if (bindingResult.hasErrors()) {
            model.addAttribute("prod", product);
            model.addAttribute("allCategories", categoryRepository.findAll());
            model.addAttribute("errorMessages", errorMessages);
            return "addprod";
        }

        // Save the product object to the database
        try {
            productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle database save exception
            model.addAttribute("errorMessages", "An error occurred while saving the product.");
            model.addAttribute("prod", product);
            model.addAttribute("allCategories", categoryRepository.findAll());
            return "addprod";
        }

        // Redirect to the home page after successful product addition
        return "redirect:/";
    }

    @GetMapping("/deleteprod")
    public ModelAndView deleteprod(HttpSession session) {
        ModelAndView mav = new ModelAndView();

        // Check if the user is not logged in or is not an admin
        String userType = (String) session.getAttribute("type");
        if (userType == null || !userType.equals("admin")) {
            // Redirect to the login page if not logged in or not an admin
            mav.setViewName("redirect:/user/login");
            return mav;
        }

        // User is an admin, proceed with the original logic
        mav.setViewName("deleteprod.html");
        List<product> allProducts = productRepository.findAll(); // Ensure class name starts with an uppercase letter
        mav.addObject("allProducts", allProducts);
        return mav;
    }
    @PostMapping("/deleteprod")
    public String deleteprod(@RequestParam("productName") String productName, Model model) {
        try {
            // Retrieve the product(s) by its name
            List<product> productList = productRepository.findAllByName(productName);
    
            if (!productList.isEmpty()) {
                // Delete each product from the repository
                for (product prod : productList) {
                    productRepository.delete(prod);
                    // Delete the associated photo file (if exists)
                    String photoFilename = prod.getPhoto();
                    if (photoFilename != null && !photoFilename.isEmpty()) {
                        Path photoPath = Paths.get("src/main/resources/static/images/", photoFilename);
                        if (Files.exists(photoPath)) {
                            Files.delete(photoPath);
                        }
                    }
                }
                return "redirect:/"; // Redirect to home page after successful operation
            } else {
                // Handle case where product(s) with given name do not exist
                model.addAttribute("errorMessage", "Product not found");
                return "deleteprod"; // Return to the same page to show the error message
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle other exceptions
            model.addAttribute("errorMessage", "An error occurred while deleting the product");
            return "deleteprod"; // Return to the same page to show the error message
        }
    }

}
