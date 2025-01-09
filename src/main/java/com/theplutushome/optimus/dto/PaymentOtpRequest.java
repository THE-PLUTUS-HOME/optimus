/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.optimus.dto;

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author MalickMoro-Samah
 */
public class PaymentOtpRequest {
    
    @NotNull
    private String phoneNumber;
    @NotNull
    private String clientReference;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getClientReference() {
        return clientReference;
    }

    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }
    
    
    
}
