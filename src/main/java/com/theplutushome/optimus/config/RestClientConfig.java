package com.theplutushome.optimus.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;

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



    @Bean("hubtelVerifyTransactionClient")
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean("hubtelPaymentUrlGenerationClient")
    public RestClient hubtelPaymentUrlGenerationRestClient() {
        return RestClient.builder()
                .baseUrl("https://payproxyapi.hubtel.com")
                .build();
    }

    @Bean(name = "hubtelSMSClient")
    public RestClient hubtelSMSClient() {
        return RestClient.builder()
                .baseUrl("https://sms.hubtel.com/v1/messages")
                .build();
    }
}