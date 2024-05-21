package com.example.blissful.blissful.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.blissful.blissful.models.cart;
@Controller
public class CartController {

    @GetMapping("/cart")
    public String showForm(Model model) {
        model.addAttribute("cart", new cart());
        return "cart";
    }

    @PostMapping("/cart")
    public String submitForm(@ModelAttribute cart cart) {
        // Process the billing info (e.g., save to database)
        return "done";
    }
}