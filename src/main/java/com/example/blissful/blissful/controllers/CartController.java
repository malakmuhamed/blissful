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

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public ModelAndView viewCart(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return new ModelAndView("redirect:/user/login"); // Redirect to login if user is not logged in
        }

        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);
        Cart cart = cartService.getCartByUserId(userId); // Get the cart by userId
        ModelAndView modelAndView = new ModelAndView("AddtoCart"); // Assuming the template is named "AddtoCart.html"
        modelAndView.addObject("cartItems", cartItems);
        modelAndView.addObject("totalPrice", cart.getTotalPrice()); // Add total price to the model
        return modelAndView;
    }
    @GetMapping("/addToCart")
    public ModelAndView addToCart(@RequestParam(name = "productId") Integer productId, @RequestParam(name = "quantity", defaultValue = "1") int quantity, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            // Handle case where user is not logged in
            return new ModelAndView("redirect:/login");
        }
        try {
            cartService.addToCart(userId, productId, quantity);
            return new ModelAndView("redirect:/cart"); // Redirect to the cart view after adding to the cart
        }
}