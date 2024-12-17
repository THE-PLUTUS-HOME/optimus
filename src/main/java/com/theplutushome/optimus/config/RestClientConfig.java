package com.theplutushome.optimus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean(name = "cryptomusClient")
    public RestClient cryptomusRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.cryptomus.com/")
                .build();
    }

    @Bean(name = "hubtelReceiveMoneyClient")
    public RestClient hubtelRestClient() {
        return RestClient.builder()
                .baseUrl("https://rmp.hubtel.com/merchantaccount/merchants/")
                .build();
    }

    @Bean(name = "hubtelVerifyTransactionClient")
    public RestClient hubtelVerifyTransactionRestClient() {
        return RestClient.builder()
                .baseUrl("https://api-txnstatus.hubtel.com/transactions")
                .build();
    }

    @Bean(name = "hubtelSMSClient")
    public RestClient hubtelSMSClient() {
        return RestClient.builder()
                .baseUrl("https://sms.hubtel.com/v1/messages")
                .build();
    }
}