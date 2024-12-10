package com.theplutushome.optimus.entity.api.hubtel;

import lombok.Data;

@Data
public class TransactionStatusCheckRequest {
    private String clientReference;
    private String hubtelTransactionId;
    private String networkTransactionId;
}
