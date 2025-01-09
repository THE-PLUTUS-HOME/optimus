/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.optimus.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 *
 * @author MalickMoro-Samah
 */
@Table(name = "ORDER_OTP")
@Entity
@Cacheable(value = false)
public class OrderOtp extends EntityModel {
    
    private String suffix;
    private String code;
    private String clientReference;

    public OrderOtp(String suffix, String code, String clientReference) {
        this.suffix = suffix;
        this.code = code;
        this.clientReference = clientReference;
    }

    
    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClientReference() {
        return clientReference;
    }

    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    @Override
    public String toString() {
        return "OrderOtp{" + "suffix=" + suffix + ", code=" + code + ", clientReference=" + clientReference + '}';
    }
    
    
    
}
