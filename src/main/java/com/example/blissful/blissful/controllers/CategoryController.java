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
}
