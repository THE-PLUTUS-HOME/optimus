package com.theplutushome.optimus.entity.api.captcha;

// DTO for the client's reCAPTCHA token
public class CaptchaRequest {
    private String token;

    // Getter and Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}