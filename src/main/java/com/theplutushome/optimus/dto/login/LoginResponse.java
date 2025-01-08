package com.theplutushome.optimus.dto.login;

public class LoginResponse {
    private String status;
    private String email;
    private String message;
    private String lastLoggedIn;
    private String token;

    public LoginResponse(String status, String message, String lastLoggedIn, String token, String email) {
        this.status = status;
        this.message = message;
        this.lastLoggedIn = lastLoggedIn;
        this.token = token;
        this.email = email;
    }

    // Getters and Setters
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
