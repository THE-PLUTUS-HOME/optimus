package com.theplutushome.optimus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "OTP_REQUEST")
public class OTPRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String email;
    private String otpCode;
    private boolean expired;
    @Column(columnDefinition = "TIMESTAMP(6) WITHOUT TIME ZONE")
    private LocalDateTime createdAt;

    public OTPRequest() {

    }

    @PrePersist
    public void setCreatedAt(){
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OTPRequest(String email, String otpCode) {
        this.email = email;
        this.otpCode = otpCode;
        this.expired = false;
    }
}
