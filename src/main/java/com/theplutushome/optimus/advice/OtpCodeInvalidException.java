/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.optimus.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author MalickMoro-Samah
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OtpCodeInvalidException extends RuntimeException {

    public OtpCodeInvalidException() {
        super("Otp Code is Invalid");
    }
}
