package com.theplutushome.optimus.clients.cryptomus;

import com.theplutushome.optimus.entity.api.cryptomus.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.theplutushome.optimus.util.Function.generateSign;


@PropertySource("classpath:application.properties")
@Component
public class CryptomusRestClient implements CryptomusHttpClient {

    private final RestClient cryptomusClient;
    private final String merchant_id;
    private final String userId;
    private final String payoutKey;
    private final String paymentKey;
    private final String userApiKey;

    @Autowired
    public CryptomusRestClient(@Qualifier("cryptomusClient") RestClient cryptomusClient, Environment env) {
        this.cryptomusClient = cryptomusClient;
        this.merchant_id = env.getProperty("merchant_id");
        this.userId = env.getProperty("user_id");
        this.payoutKey = env.getProperty("apiPayoutKey");
        this.paymentKey = env.getProperty("apiPaymentKey");
        this.userApiKey = env.getProperty("userApiKey");
    }

    public ServiceList getServiceList() {
        try {
            String sign = generateSign(null, payoutKey);
            return cryptomusClient.post()
                    .uri("/v1/payout/services")
                    .header("merchant", merchant_id)
                    .header("sign", sign)
                    .header("Content-Type", "application/json")
                    .retrieve()
                    .body(ServiceList.class);
        } catch (RestClientException e) {
            // Handle error
            throw new RestClientException("Failed to get balances: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error generating signature: " + e.getMessage());
        }
    }

    public ExchangeRateResponse getExchangeRate(String currency) {
        try {
            return cryptomusClient.get()
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
            String sign = generateSign(Map.of(), paymentKey);
            // Send the request with headers
            return cryptomusClient.post()
                    .uri("/v1/balance")
                    .header("merchant", merchant_id)
                    .header("sign", sign) // Add the signature header
                    .header("Content-Type", "application/json")
                    .body(new HashMap<>())
                    .retrieve()
                    .body(BalanceResponse.class);
        } catch (RestClientException e) {
            // Handle error
            throw new RestClientException("Failed to get balances: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error generating signature: " + e.getMessage());
        }
    }


    public PayoutHistoryResponse getPayoutHistory(@RequestBody @Valid PayoutHistoryRequest payoutHistoryRequest) {
        try {
            String sign = generateSign(payoutHistoryRequest, payoutKey);

            return cryptomusClient.post()
                    .uri("/v1/payout/list")
                    .header("merchant", merchant_id)
                    .header("sign", sign)
                    .header("Content-Type", "application/json")
                    .body(payoutHistoryRequest)
                    .retrieve()
                    .body(PayoutHistoryResponse.class);
        } catch (RestClientException e) {
            throw new RestClientException("Failed to get payout history: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating signature: " + e.getMessage());
        }
    }

    public PayoutResponse getPayout(@RequestBody @Valid PayoutRequest payoutRequest) {
        try {
            String sign = generateSign(payoutRequest, payoutKey);
            return cryptomusClient.post()
                    .uri("/v1/payout")
                    .header("merchant", merchant_id)
                    .header("sign", sign)
                    .header("Content-Type", "application/json")
                    .body(payoutRequest)
                    .retrieve()
                    .body(PayoutResponse.class);
        } catch (RestClientException e) {
            return new PayoutResponse(1, null);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public PayoutResponse payOutInfoRequest(@RequestBody @Valid PayoutInfoRequest payoutInfoRequest) {
        try {
            String sign = generateSign(payoutInfoRequest, payoutKey);
            return cryptomusClient.post()
                    .uri("/v1/payout/info")
                    .header("merchant", merchant_id)
                    .header("sign", sign)
                    .header("Content-Type", "application/json")
                    .body(payoutInfoRequest)
                    .retrieve()
                    .body(PayoutResponse.class);
        } catch (RestClientException e) {
            throw new RestClientException("Failed to get payout info: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating signature: " + e.getMessage());
        }
    }

    @Override
    public ConvertResponse convertAsset(ConvertRequest convertRequest) {
        try {
            String sign = generateSign(convertRequest, userApiKey);
            return cryptomusClient.post()
                    .uri("/v2/user-api/convert/calculate")
                    .header("userId", userId)
                    .header("sign", sign)
                    .header("Content-Type", "application/json")
                    .body(convertRequest)
                    .retrieve()
                    .body(ConvertResponse.class);
        } catch (RestClientException e) {
            throw new RestClientException("Failed to perform conversion: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating signature: " + e.getMessage());
        }
    }
    public double convertCryptoAmountToUsd(String crypto, double cryptoAmount) {
        ExchangeRateResponse response = this.getExchangeRate(crypto);
        response.getResult().removeIf(r -> !r.getTo().equals("USD"));
        return Double.parseDouble(response.getResult().get(0).getCourse()) * cryptoAmount;
    }

}
