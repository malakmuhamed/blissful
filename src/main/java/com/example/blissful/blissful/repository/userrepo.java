package com.example.blissful.blissful.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blissful.blissful.models.user;

public interface userrepo extends JpaRepository<user, Integer> {

    user findByEmail(String email);

    List<user> findByUsernameContaining(String username);

}
