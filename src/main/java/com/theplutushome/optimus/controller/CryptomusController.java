package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.clients.cryptomus.CryptomusHttpClient;
import com.theplutushome.optimus.entity.api.cryptomus.ExchangeRateResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/optimus/v1/api/cryptomus")
public class CryptomusController {

    private final CryptomusHttpClient client;

    public CryptomusController(CryptomusHttpClient client) {
        this.client = client;
    }

    @GetMapping("/exchange-rate/{currency}")
    public ExchangeRateResponse getAllUsers(@PathVariable("currency") String currency) {
        return client.getExchangeRate(currency);
    }

}
