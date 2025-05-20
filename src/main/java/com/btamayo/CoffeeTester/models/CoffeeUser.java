package com.btamayo.CoffeeTester.models;

import jakarta.validation.constraints.NotBlank;

public class CoffeeUser {
    @NotBlank(message = "Username should not be blank")
    private String username;
    @NotBlank(message = "Password should not be blank")
    private String password;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}