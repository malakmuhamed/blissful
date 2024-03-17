package com.example.blissful.blissful.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.blissful.blissful.models.Category;
import com.example.blissful.blissful.models.product;
import com.example.blissful.blissful.repository.CategoryRepository;
import com.example.blissful.blissful.repository.ProductRepository;
import org.springframework.util.StringUtils;


@Controller
@RequestMapping("/")
public class productcontroller {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("addprod")
    public ModelAndView addprod() {
        ModelAndView mav = new ModelAndView("addprod.html");
        List<Category> allCategories = this.categoryRepository.findAll();
        mav.addObject("allCategories", allCategories);
        product newprod = new product();
        mav.addObject("prod", newprod);
        return mav;
    }

   @PostMapping("addprod")
    public String saveprod(@ModelAttribute product product, @RequestParam("file") MultipartFile file) {
        try {
            
            
            if (file != null && !file.isEmpty()) {
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                if (filename.contains("..")) {
                    System.out.println("Not a valid file");
                    // Handle invalid file name
                    return "error";
                }
                String uploadDir = "src/main/resources/static/images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try {
                    // Copy the file to the upload directory
                    Files.copy(file.getInputStream(), uploadPath.resolve(filename));
                    // Set the photo path in the Product object
                    product.setPhoto( filename);
                } catch (IOException e) {
                    System.out.println("Could not save the file: " + e.getMessage());
                    // Handle file save exception
                    return "error";
                }
            } else {
                // Handle case where no file is uploaded
                System.out.println("No file uploaded");
                return "error";
            }
// Calculate the price after applying the offer
double price = product.getPrice();
double offer = product.getOffer();
double discountedPrice = price - (price * (offer / 100.0));
product.setDiscountedPrice(discountedPrice);
            // Save the product object to the database
            productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle other exceptions
            return "error";
        }
        return "redirect:/"; // Redirect to home page after successful operation
    }
    @GetMapping("/deleteprod")
    public ModelAndView deleteprod() {
        ModelAndView mav = new ModelAndView("deleteprod.html");
        List<product> allProducts = productRepository.findAll();
        mav.addObject("allProducts", allProducts);
        return mav;
    }
    
   
    @PostMapping("/deleteprod")
    public String deleteprod(@RequestParam("productName") String productName) {
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
            } else {
                // Handle case where product(s) with given name do not exist
                System.out.println("Product(s) not found");
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle other exceptions
            return "error";
        }
        return "redirect:/"; // Redirect to home page after successful operation
    }
    
    
}

