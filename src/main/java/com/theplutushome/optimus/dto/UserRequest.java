package com.theplutushome.optimus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotNull(message = "password should not be null")
    @Size(min = 6, max = 30, message = "minimum length of string should be 6 with a maximum of 30")
    private String password;
    @Email(message = "invalid email address")
    private String email;
    @NotNull(message = "username should not be null")
    private String username;
    private String referralCode;


    public UserRequest(String password, String email, String username) {
        this.password = password;
        this.email = email;
        this.username = username;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
