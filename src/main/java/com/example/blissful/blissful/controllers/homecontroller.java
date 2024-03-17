package com.example.blissful.blissful.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.blissful.blissful.models.product;
import com.example.blissful.blissful.repository.ProductRepository;
@Controller
public class homecontroller {

    @Autowired
    private ProductRepository prodRepository;

    @GetMapping("/home")
    public ModelAndView showIndex() {
        ModelAndView modelAndView = new ModelAndView("index");
        // Retrieve products with offers
        List<product> prodsWithOffers = prodRepository.findAllByOfferGreaterThan(0);
        modelAndView.addObject("prods", prodsWithOffers);
        // Add the offer slider fragment to the model
       
        return modelAndView;
    
    }
}