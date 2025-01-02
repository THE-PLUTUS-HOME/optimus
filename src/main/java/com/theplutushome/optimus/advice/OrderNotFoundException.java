package com.theplutushome.optimus.advice;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(){
        super("Order Not Found");
    }
}
