package com.theplutushome.optimus.dto;

import com.theplutushome.optimus.entity.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotNull(message = "name should not be null")
    private String name;
    @NotNull(message = "password should not be null")
    @Size(min = 6, max = 30, message = "minimum length of string should be 6 with a maximum of 30")
    private String password;
    @Email(message = "invalid email address")
    private String email;
    @NotNull(message = "username should not be null")
    private String username;
    @NotNull(message = "phone should not be null")
    @Size(min = 12, max = 12, message = "Phone number invalid, expected format {233012345678}")
    private String phone;
    private UserType userType;
    @NotNull(message = "secret phrase required")
    @Size(min = 7, max = 10)
    private String secretPhrase;

    public UserRequest() {
    }

    public UserRequest(String name, String password, String email, String username, UserType userType, String secretPhrase) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.username = username;
        this.userType = userType;
        this.secretPhrase = secretPhrase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getSecretPhrase() {
        return secretPhrase;
    }

    public void setSecretPhrase(String secretPhrase) {
        this.secretPhrase = secretPhrase;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
