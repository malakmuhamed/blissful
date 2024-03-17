package com.example.blissful.blissful.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blissful.blissful.models.product;

public interface ProductRepository extends JpaRepository<product,Integer>{
    
    List<product> findAllByCategoryId(int id);
    List<product>findAll();
    List<product>findAllByCategory_Name(String categoryName);
    List<product> findAllByOfferGreaterThan(int i);
    List<product> findAllByName(String productName);
   
}
