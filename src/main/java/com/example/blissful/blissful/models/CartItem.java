package com.example.blissful.blissful.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private product product;

    @ManyToOne
    private Cart cart;

    private int quantity;
 // Constructors
 public CartItem() {
}

public CartItem(product product, Cart cart, int quantity) {
    this.product = product;
    this.cart = cart;
    this.quantity = quantity;
}

// Getters and Setters
public Integer getId() {
    return id;
}

public void setId(Integer id) {
    this.id = id;
}

public product getProduct() {
    return product;
}
    
public void setProduct(product product) {
    this.product = product;
}

public Cart getCart() {
    return cart;
}

public void setCart(Cart cart) {
    this.cart = cart;
}

public int getQuantity() {
    return quantity;
}

public void setQuantity(int quantity) {
    this.quantity = quantity;
}
}

