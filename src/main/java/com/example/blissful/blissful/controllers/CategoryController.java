package com.example.blissful.blissful.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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


@Controller 
@RequestMapping("/")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public ModelAndView getCategories() {
        ModelAndView mav = new ModelAndView("list-category.html");
        List<Category> categories = this.categoryRepository.findAll();
        mav.addObject("categories", categories);
        return mav;
    }
    

@GetMapping("/admindashboard")
    public ModelAndView getAdminDashboard() {
        ModelAndView mav = new ModelAndView("admindashboard.html");
       return mav;
    }
    @GetMapping("addCategory")
    public ModelAndView addCategory() {
        ModelAndView mav = new ModelAndView("addCategory.html");
        Category newCategory = new Category();
        mav.addObject("category", newCategory);
        return mav;
    }

    @PostMapping("addCategory")
    public String saveCategory(@ModelAttribute Category category) {
        this.categoryRepository.save(category);
        return "redirect:/"; // Redirect to the homepage after saving the category
    }

    

    @GetMapping("/category/{id}")
public ModelAndView getCategory(@PathVariable("id") int id) {
    ModelAndView mav = new ModelAndView("list-prod.html");
    List<product>prods=this.productRepository.findAllByCategoryId(id);
    mav.addObject("prods", prods);
    return mav;
}

@GetMapping("/editprod/{id}")
public ModelAndView editProduct(@PathVariable("id") int id) {
    ModelAndView mav = new ModelAndView("edit-product.html"); 
    product product = this.productRepository.findById(id).orElse(null); // Find the product by its ID
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
public String updateProduct(@PathVariable("id") int id, @ModelAttribute product updatedProduct) {
    product existingProduct = this.productRepository.findById(id).orElse(null); // Find the existing product by its ID
    if (existingProduct != null) {
        // Update the existing product with the new data
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setOffer(updatedProduct.getOffer());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setCategory(updatedProduct.getCategory());
        // Save the updated product
        this.productRepository.save(existingProduct);
    } else {
        // Handle case where product is not found
        return "redirect:/"; // Redirect to the homepage or an error page
    }
    return "redirect:/category/" + existingProduct.getCategory().getId(); // Redirect to the category page after successful update
}


}
