package com.example.blissful.blissful.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer cartId;

    @OneToOne
    private user user;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> items;
// Default constructor
public Cart() {
    this.items = new ArrayList<>(); // Ensure items is initialized
}

// Constructor with user
public Cart(user user) {
    this.user = user;
    this.items = new ArrayList<>(); // Initialize items as an ArrayList
}

// Getters and Setters
public Integer getCartId() {
    return cartId;
}

public void setCartId(Integer cartId) {
    this.cartId = cartId;
}

public user getUser() {
    return user;
}

}
