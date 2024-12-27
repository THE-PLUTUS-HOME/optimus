package com.theplutushome.optimus.clients.hubtel;

import com.theplutushome.optimus.entity.api.hubtel.*;
import com.theplutushome.optimus.util.Function;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@PropertySource("classpath:application.properties")
@Component
public class HubtelRestClient implements HubtelHttpClient {

    private static final Logger log =  LoggerFactory.getLogger(HubtelRestClient.class);
    
    private final RestClient hubtelReceiveMoneyClient;
    private final RestClient hubtelVerifyTransactionClient;
    private final RestClient hubtelSMSClient;
    private final RestClient hubtelPaymentUrlGenerationClient;
    private final String POS_Sales_ID;
    private final String clientId;
    private final String smsClientId;
    private final String smsClientSecret;
    private final String hubtelSecretKey;

    private final String clientSecret;


    @Autowired
    public HubtelRestClient(@Qualifier("hubtelReceiveMoneyClient") RestClient hubtelReceiveMoneyClient,
                            @Qualifier("hubtelVerifyTransactionClient") RestClient hubtelVerifyTransactionClient,
                            @Qualifier("hubtelSMSClient") RestClient hubtelSMSClient,
                            @Qualifier("hubtelPaymentUrlGenerationClient") RestClient hubtelPaymentUrlClient,
                            Environment env) {
        this.hubtelReceiveMoneyClient = hubtelReceiveMoneyClient;
        this.hubtelVerifyTransactionClient = hubtelVerifyTransactionClient;
        this.hubtelSMSClient = hubtelSMSClient;
        this.hubtelPaymentUrlGenerationClient = hubtelPaymentUrlClient;
        this.POS_Sales_ID = env.getProperty("pos_sales_id");
        this.clientId = env.getProperty("client_id");
        this.clientSecret = env.getProperty("client_secret");
        this.smsClientId = env.getProperty("sms_client_id");
        this.smsClientSecret = env.getProperty("sms_client_secret");
        this.hubtelSecretKey = env.getProperty("hubtel_secrety_key");
    }

    public PaymentResponse initiatePayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        try {
            String bearer = Function.generateAuthorizationKey(clientId, clientSecret);
            return hubtelReceiveMoneyClient.post()
                    .uri("/{POS_Sales_ID}/receive/mobilemoney", POS_Sales_ID)
                    .header("Content-Type", "application/json")
                    .header("Authorization", bearer)
                    .retrieve()
                    .body(PaymentResponse.class);
        } catch (RestClientException e) {
            throw new RestClientException("Failed to initiate payment: " + e.getMessage());
        }
    }

    public TransactionStatusCheckResponse checkTransaction(@RequestParam(value = "clientReference", required = true) String clientReference) {
        try {
            String bearer = Function.generateAuthorizationKey(clientId, clientSecret);
            return hubtelVerifyTransactionClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{POS_Sales_ID}/status")
                            .queryParam("clientReference", clientReference)
                            .build(POS_Sales_ID))
                    .header("Authorization", bearer)
                    .retrieve()
                    .body(TransactionStatusCheckResponse.class);
        } catch (RestClientException e) {
            // Handle error
            throw new RestClientException("Failed to verify transaction: " + e.getMessage());
        }
    }

    public SMSResponse sendSMS(@RequestParam(value = "to") String to, @RequestParam(value = "content") String content) {

        try {
            return hubtelSMSClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{POS_Sales_ID}/status")
                            .queryParam("clientid", smsClientId)
                            .queryParam("clientsecret", smsClientSecret)
                            .queryParam("from", "Plutus Home")
                            .queryParam("to", to)
                            .queryParam("content", content)
                            .build(POS_Sales_ID))
                    .retrieve()
                    .body(SMSResponse.class);
        } catch (RestClientException e) {
            throw new RestClientException("Failed to send SMS: " + e.getMessage());
        }
    }

    public PaymentLinkResponse getPaymentUrl(@RequestBody PaymentLinkRequest paymentLinkRequest) {
        try {
            log.info("Initiating payment link generation with request: {}", paymentLinkRequest.toString());
            PaymentLinkResponse response = hubtelPaymentUrlGenerationClient.post()
                    .uri("/items/initiate")
                    .header("Content-Type", "application/json")
                    .header("Authorization", hubtelSecretKey)
                    .body(paymentLinkRequest)
                    .retrieve()
                    .body(PaymentLinkResponse.class);
            log.info("Payment link generated successfully: {}", response);
            return response;
        } catch (RestClientException e) {
            log.error("Failed to generate link: {}", e.getMessage());
            throw new RestClientException("Failed to generate link: " + e.getMessage());
        }
    }
}