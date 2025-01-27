package com.theplutushome.optimus.dto.login;

public class LoginResponse {
    private String status;
    private String message;
    private String lastLoggedIn;
    private String email;
    private String username;

    public LoginResponse(String status, String message, String lastLoggedIn, String email, String username) {
        this.status = status;
        this.message = message;
        this.lastLoggedIn = lastLoggedIn;
        this.email = email;
        this.username = username;
    }

    // Getters and Setters


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLastLoggedIn() {
        return lastLoggedIn;
    }

    public void setLastLoggedIn(String lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
