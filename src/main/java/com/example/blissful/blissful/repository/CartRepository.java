package com.example.blissful.blissful.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blissful.blissful.models.Cart;
import com.example.blissful.blissful.models.user;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser(user user);
}