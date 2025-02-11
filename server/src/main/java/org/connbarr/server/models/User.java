package org.connbarr.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "users")
public class User extends AbstractIdentifiableModel {
    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    public User() {
        //empty constructor. So alone.
    }

    public User(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        setPassword(password);
    }

    public String getUserName() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public static BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    public boolean isPasswordEmpty() {
        return password == null || password.isEmpty();
    }

    public boolean isMatchingPassword(String password) {
        return getEncoder().matches(password, this.password);
    }


}

//Class purpose: whenever a user object is created, it will receive its own ID tied to the specific user that is created.
//Moves sequentially (1, 2, 3, 4... etc..)
//Allows you to get user ID from data base and set properties to them.
//Allows character classes on top of user ID's.
//Abstract class allows anything we want to have an ID through extending the AbstractModel.
//ID will serve as anchor to find the fields that we edit from here forward, this will allow user-specific information