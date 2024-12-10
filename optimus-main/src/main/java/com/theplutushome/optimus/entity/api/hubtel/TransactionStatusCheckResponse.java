package com.theplutushome.optimus.entity.api.hubtel;

import lombok.Data;

@Data
public class TransactionStatusCheckResponse {
    private String message;
    private String responseCode;
    private TransactionStatusCheckData data;

    @Data
    public static class TransactionStatusCheckData {
        private String date;
        private String status;
        private String transactionId;
        private String externalTransactionId;
        private String paymentMethod;
        private String clientReference;
        private String currencyCode;
        private Double amount;
        private Double charges;
        private Double amountAfterCharges;
        private Boolean isFulfilled;
    }
}

