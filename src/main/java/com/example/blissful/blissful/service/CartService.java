package com.example.blissful.blissful.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blissful.blissful.models.Cart;
import com.example.blissful.blissful.models.CartItem;
import com.example.blissful.blissful.models.product;
import com.example.blissful.blissful.models.user;
import com.example.blissful.blissful.repository.CartItemRepository;
import com.example.blissful.blissful.repository.CartRepository;
import com.example.blissful.blissful.repository.ProductRepository;
import com.example.blissful.blissful.repository.userrepo;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private userrepo userRepo;
// Method to get cart by userId
public Cart getCartByUserId(Integer userId) {
    user user = userRepo.findById(userId).orElse(null);
    if (user == null) {
        throw new RuntimeException("User not found");
    }

    Optional<Cart> optionalCart = cartRepo.findByUser(user);
    return optionalCart.orElseGet(() -> new Cart(user));
}

// Method to get cart items by userId
public List<CartItem> getCartItemsByUserId(Integer userId) {
    user user = userRepo.findById(userId).orElse(null);
    if (user == null) {
        throw new RuntimeException("User not found");
    }

    Optional<Cart> optionalCart = cartRepo.findByUser(user);
    if (optionalCart.isPresent()) {
        Cart cart = optionalCart.get();
        return cartItemRepo.findByCart(cart);
    } else {
        return List.of(); // Return an empty list if no cart exists
    }
}
}
