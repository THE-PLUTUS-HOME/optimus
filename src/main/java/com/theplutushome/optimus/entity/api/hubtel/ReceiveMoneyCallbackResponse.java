package com.theplutushome.optimus.entity.api.hubtel;

import lombok.Data;

@Data
public class ReceiveMoneyCallbackResponse {
    private String responseCode;
    private String message;
    private ReceiveMoneyCallbackData data;

    @Data
    public static class ReceiveMoneyCallbackData {
        private Double amount;
        private Double charges;
        private Double amountAfterCharges;
        private String description;
        private String clientReference;
        private String transactionId;
        private String externalTransactionId;
        private String orderId;
        private String paymentDate;
    }
}
