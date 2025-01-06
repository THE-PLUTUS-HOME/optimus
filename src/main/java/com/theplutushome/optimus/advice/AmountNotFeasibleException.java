package com.theplutushome.optimus.advice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AmountNotFeasibleException extends RuntimeException {
    public AmountNotFeasibleException(){
        super("Amount not feasible");
    }
}