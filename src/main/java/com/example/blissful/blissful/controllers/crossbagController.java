package com.example.blissful.blissful.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.blissful.blissful.models.product;
import com.example.blissful.blissful.repository.ProductRepository;
@Controller
public class crossbagController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/crossbags")
    public ModelAndView showCrossbagsPage() {
        ModelAndView modelAndView = new ModelAndView("crossbags"); // Return the name of the HTML file

        // Retrieve products belonging to the "crossbags" category
        List<product> crossbagsProducts = productRepository.findAllByCategory_Name("crossbags");

        // Add the list of crossbags products to the model
        modelAndView.addObject("crossbagsProducts", crossbagsProducts);

        return modelAndView;
    }
}