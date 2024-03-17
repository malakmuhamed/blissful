package com.example.blissful.blissful.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.ManyToOne;


@Entity
public class product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int price;
    private double offer;
    private String photo; // Added photo field
    private int quantity;
    double discountedPrice;
    

    @ManyToOne
    private Category category;

    public product() {
    }

    public product(int id, String name, int price, double offer, String photo, int quantity, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.offer = offer;
        this.photo = photo;
        this.quantity = quantity;
        this.category = category;
    }
    
    
    public double getDiscountedPrice() {
        return discountedPrice;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getOffer() {
        return offer;
    }

    public void setOffer(double offer) {
        this.offer = offer;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
   
       
        public void setDiscountedPrice(double discountedPrice) {
            // Calculate the discounted price based on the original price and offer
            this.discountedPrice = price - (price * (offer / 100.0));
        }
       
        
}