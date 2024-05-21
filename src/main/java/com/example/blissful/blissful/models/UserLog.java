package com.example.blissful.blissful.models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class UserLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId; // Assuming userId is the email
    private Date loginTime;
    private String pageVisited;

    public UserLog() {
    }

    public UserLog(Long id, String userId, Date loginTime, String pageVisited) {
        this.id = id;
        this.userId = userId;
        this.loginTime = loginTime;
        this.pageVisited = pageVisited;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getPageVisited() {
        return this.pageVisited;
    }

    public void setPageVisited(String pageVisited) {
        this.pageVisited = pageVisited;
    }

    // Remaining code for constructors, equals, hashCode, and toString methods...
}
