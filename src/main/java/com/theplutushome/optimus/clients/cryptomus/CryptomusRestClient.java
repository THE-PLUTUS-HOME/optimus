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

import java.math.BigDecimal;
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

    @Override
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
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating signature: " + e.getMessage());
        }
    }

    @Override
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

    @Override
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
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating signature: " + e.getMessage());
        }
    }

    @Override
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

    @Override
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

    @Override
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

    public double convertCryptoAmountToUsd(String crypto, BigDecimal cryptoAmount) {
        // Get exchange rate response for the given crypto
        ExchangeRateResponse response = this.getExchangeRate(crypto);

        // Filter the results to only keep the ones with "USD" as the target currency
        response.getResult().removeIf(r -> !r.getTo().equals("USD"));

        if (response.getResult().isEmpty()) {
            throw new IllegalArgumentException("No USD exchange rate found for " + crypto);
        }

        double course = Double.parseDouble(response.getResult().get(0).getCourse());
        BigDecimal exchangeRate = BigDecimal.valueOf(course);
        BigDecimal result = cryptoAmount.multiply(exchangeRate);

        // Return the result as a double
        return result.doubleValue();
    }


    public double getWithdrawalFee(String crypto) {
        ServiceList list = this.getServiceList();
        list.getResult().removeIf(l -> !l.getCurrency().equalsIgnoreCase(crypto));
        list.getResult().removeIf(l -> !l.getNetwork().equalsIgnoreCase(crypto.equalsIgnoreCase("USDT") ? "TRON" : crypto));

        if (!list.getResult().isEmpty()) {
            double fee = Double.parseDouble(list.getResult().get(0).getCommission().getFee_amount());
            return this.convertCryptoAmountToUsd(crypto, BigDecimal.valueOf(fee));
        }
        return 0;
    }

    public double getMerchantBalance() {
        BalanceResponse cryptoBalance = this.getBalance();

        cryptoBalance.getResult().forEach(result -> {
            BalanceResponse.Balance balance = result.getBalance();

            // Filter merchant balances
            balance.getMerchant().removeIf(m -> !"USDT".equalsIgnoreCase(m.getCurrency_code()));

            // Filter user balances
            balance.getUser().removeIf(u -> !"USDT".equalsIgnoreCase(u.getCurrency_code()));
        });

        return Double.parseDouble(cryptoBalance.getResult().get(0).getBalance().getMerchant().get(0).getBalance());
    }
}
