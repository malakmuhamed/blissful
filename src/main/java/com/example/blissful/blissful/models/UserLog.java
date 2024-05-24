package com.example.blissful.blissful.models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Objects;

@Entity
public class UserLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // name of the column in UserLog table
    private user user;
    private String email;

    private Date loginTime;
    private String pageVisited;

    public UserLog() {
    }

    public UserLog(Long id, user user, String email, Date loginTime, String pageVisited) {
        this.id = id;
        this.user = user;
        this.email = email;
        this.loginTime = loginTime;
        this.pageVisited = pageVisited;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public user getUser() {
        return this.user;
    }

    public void setUser(user user) {
        this.user = user;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UserLog id(Long id) {
        setId(id);
        return this;
    }

    public UserLog user(user user) {
        setUser(user);
        return this;
    }

    public UserLog email(String email) {
        setEmail(email);
        return this;
    }

    public UserLog loginTime(Date loginTime) {
        setLoginTime(loginTime);
        return this;
    }

    public UserLog pageVisited(String pageVisited) {
        setPageVisited(pageVisited);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserLog)) {
            return false;
        }
        UserLog userLog = (UserLog) o;
        return Objects.equals(id, userLog.id) && Objects.equals(user, userLog.user)
                && Objects.equals(email, userLog.email) && Objects.equals(loginTime, userLog.loginTime)
                && Objects.equals(pageVisited, userLog.pageVisited);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, email, loginTime, pageVisited);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", user='" + getUser() + "'" +
                ", email='" + getEmail() + "'" +
                ", loginTime='" + getLoginTime() + "'" +
                ", pageVisited='" + getPageVisited() + "'" +
                "}";
    }

}
