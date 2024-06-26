package com.example.blissful.blissful.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blissful.blissful.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByName(String name);

}
