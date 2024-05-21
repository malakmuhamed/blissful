package com.example.blissful.blissful.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blissful.blissful.models.UserLog;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {
    
}