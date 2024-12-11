package com.theplutushome.optimus.clients.cryptomus;

import com.theplutushome.optimus.entity.api.cryptomus.*;
import jakarta.validation.Valid;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class CryptomusRestClient {

    private final RestClient restClient;

    public CryptomusRestClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://api.cryptomus.com/")
                .build();
    }

    public List<ServiceList> getServiceList() {
        return restClient.post()
                .uri("/v1/payout/services")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public ExchangeRateResponse getExchangeRate(String currency) {
        try {
            return restClient.get()
                    .uri("/v1/exchange-rate/{currency}/list", currency)
                    .retrieve()
                    .body(ExchangeRateResponse.class);
        } catch (RestClientException e) {
            // Handle error
            throw new RestClientException("Failed to fetch exchange rate: " + e.getMessage());
        }
    }

    public BalanceResponse getBalance() {
        try {
            return restClient.post()
                    .uri("/v1/balance")
                    .retrieve()
                    .body(BalanceResponse.class);
        } catch (RestClientException e) {
            // Handle error
            throw new RestClientException("Failed to get balances: " + e.getMessage());
        }
    }

    public PayoutHistoryResponse getPayoutHistory(@RequestBody @Valid PayoutHistoryRequest payoutHistoryRequest) {
        try {
            return restClient.post()
                    .uri("/v1/payout/list")
                    .body(payoutHistoryRequest)
                    .retrieve()
                    .body(PayoutHistoryResponse.class);
        } catch (RestClientException e) {
            throw new RestClientException("Failed to get payout history: " + e.getMessage());
        }
    }

    public PayoutResponse getPayout(@RequestBody @Valid PayoutRequest payoutRequest) {
        try {
            return restClient.post()
                    .uri("/v1/payout")
                    .body(payoutRequest)
                    .retrieve()
                    .body(PayoutResponse.class);
        } catch (RestClientException e) {
            throw new RestClientException("Failed to make payout: " + e.getMessage());
        }
    }

    public PayoutResponse payOutInfoRequest(@RequestBody @Valid PayoutInfoRequest payoutInfoRequest) {
        try {
            return restClient.post()
                    .uri("/v1/payout/info")
                    .body(payoutInfoRequest)
                    .retrieve()
                    .body(PayoutResponse.class);
        } catch (RestClientException e) {
            throw new RestClientException("Failed to get payout info: " + e.getMessage());
        }
    }

}
