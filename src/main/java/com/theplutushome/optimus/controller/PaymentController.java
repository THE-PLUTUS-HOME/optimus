package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.clients.hubtel.HubtelRestClient;
import com.theplutushome.optimus.entity.api.hubtel.SMSResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/optimus/api/v1/payment")
public class PaymentController {

    private final HubtelRestClient client;

    public PaymentController(HubtelRestClient client) {
        this.client = client;
    }

    @GetMapping("/sendOtp")
    public SMSResponse sendMessage(@RequestParam(value = "phone") String phoneNumber) {
        return null;
    }

    @GetMapping("/verifyOtp")
    public ResponseEntity<Void> verifyOtp(@RequestParam(value = "code") String code, @RequestParam(value = "phone") String phoneNumber){
        return null;
    }
}