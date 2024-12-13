package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.clients.cryptomus.CryptomusRestClient;
import com.theplutushome.optimus.entity.api.cryptomus.*;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/optimus/v1/api/cryptomus")
public class CryptomusController {

    private final CryptomusRestClient client;

    public CryptomusController(CryptomusRestClient client) {
        this.client = client;
    }

    @GetMapping(value = "/exchange-rate/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ExchangeRateResponse getAllUsers(@PathVariable("currency") String currency, @RequestParam(value = "to", required = false) String to) {
        ExchangeRateResponse response = client.getExchangeRate(currency);
        response.getResult().removeIf(r -> !r.getTo().equals(to));
        return response;
    }

    @PostMapping("/balance")
    public BalanceResponse getBalance(@RequestParam(value = "currency_code", required = false) String currency) {
        BalanceResponse response = client.getBalance();

        // Check if currency filtering is required
        if (currency != null && !currency.isBlank()) {
            response.getResult().forEach(result -> {
                BalanceResponse.Balance balance = result.getBalance();

                // Filter merchant balances
                balance.getMerchant().removeIf(m -> !currency.equals(m.getCurrency_code()));

                // Filter user balances
                balance.getUser().removeIf(u -> !currency.equals(u.getCurrency_code()));
            });
        }

        return response;
    }


    @PostMapping(value = "/payout/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public PayoutResponse getPayoutInfo(@RequestBody @Valid PayoutInfoRequest request) {
        return client.payOutInfoRequest(request);
    }

    @PostMapping(value = "/payout/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public PayoutHistoryResponse getPayoutHistory(@RequestBody PayoutHistoryRequest request) {
        return client.getPayoutHistory(request);
    }


}
