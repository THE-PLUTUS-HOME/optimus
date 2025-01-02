package com.theplutushome.optimus.entity.api.hubtel;

import lombok.Data;

@Data
public class ReceiveMoneyRequest {
    private String customerName;
    private String customerMsisdn;
    private String customerEmail;
    private String channel;
    private Double amount;
    private String primaryCallbackUrl;
    private String description;
    private String clientReference;
}

