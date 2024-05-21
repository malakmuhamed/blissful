package com.example.blissful.blissful.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.blissful.blissful.models.BillingInfo;

@Controller
public class CheckoutController {

    @GetMapping("/checkout")
    public String showForm(Model model) {
        model.addAttribute("billingInfo", new BillingInfo());
        return "checkout";
    }
    @PostMapping("/checkout")
    public String submitForm(@ModelAttribute BillingInfo billingInfo) {
        // Process the billing info (e.g., save to database)
        return "checkoutSuccess";
    }
   
}
