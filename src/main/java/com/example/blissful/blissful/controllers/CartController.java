package com.example.blissful.blissful.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.blissful.blissful.models.Cart;
import com.example.blissful.blissful.models.CartItem;
import com.example.blissful.blissful.service.CartService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

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