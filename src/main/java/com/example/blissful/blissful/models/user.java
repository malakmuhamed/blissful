package com.example.blissful.blissful.models;

import java.util.Date;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String type;
    private String username;
    private String email;
    private String password;
   // private String dob;
     @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    public user() {
    }

    public user(int id, String type, String username, String email, String password, Date dob) {
        this.id = id;
        this.type = type;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dob = dob;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDob() {
        return this.dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public user id(int id) {
        setId(id);
        return this;
    }

    public user type(String type) {
        setType(type);
        return this;
    }

    public user username(String username) {
        setUsername(username);
        return this;
    }

    public user email(String email) {
        setEmail(email);
        return this;
    }

    public user password(String password) {
        setPassword(password);
        return this;
    }

    public user dob(Date dob) {
        setDob(dob);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof user)) {
            return false;
        }
        user user = (user) o;
        return id == user.id && Objects.equals(type, user.type) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(dob, user.dob);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, username, email, password, dob);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", type='" + getType() + "'" +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", dob='" + getDob() + "'" +
            "}";
    }

}