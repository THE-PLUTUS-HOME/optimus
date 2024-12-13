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

    @Bean(name = "anotherRestClient")
    public RestClient anotherRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.another-service.com/")
                .build();
    }
}

