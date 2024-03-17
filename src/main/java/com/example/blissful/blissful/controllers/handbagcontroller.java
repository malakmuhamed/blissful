package com.example.blissful.blissful.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.blissful.blissful.models.product;
import com.example.blissful.blissful.repository.ProductRepository;
@Controller
public class handbagcontroller {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/handbag")
    public ModelAndView showCrossbagsPage() {
        ModelAndView modelAndView = new ModelAndView("handbag"); // Return the name of the HTML file

        // Retrieve products belonging to the "crossbags" category
        List<product> handbagsProducts = productRepository.findAllByCategory_Name("Handbags");

        // Add the list of crossbags products to the model
        modelAndView.addObject("Handbags", handbagsProducts);

        return modelAndView;
    }
}