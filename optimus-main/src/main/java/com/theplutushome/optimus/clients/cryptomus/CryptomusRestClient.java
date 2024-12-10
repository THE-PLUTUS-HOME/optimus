package com.theplutushome.optimus.clients.cryptomus;

import com.theplutushome.optimus.entity.api.cryptomus.ServiceList;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

public class CryptomusRestClient {

    private final RestClient restClient;

    public CryptomusRestClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build();
    }

    List<ServiceList> getServiceList() {
        return restClient.get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

}
