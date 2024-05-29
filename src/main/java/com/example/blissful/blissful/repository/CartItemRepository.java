package com.example.blissful.blissful.repository;

import java.util.List; // Correct import
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blissful.blissful.models.Cart;
import com.example.blissful.blissful.models.CartItem;
import com.example.blissful.blissful.models.product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndProduct(Cart cart, product product);
}