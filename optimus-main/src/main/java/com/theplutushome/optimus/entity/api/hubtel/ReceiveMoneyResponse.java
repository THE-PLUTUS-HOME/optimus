package com.theplutushome.optimus.entity.api.hubtel;

import lombok.Data;

@Data
public class ReceiveMoneyResponse {
    private String message;
    private String responseCode;
    private ReceiveMoneyData data;

    @Data
    public static class ReceiveMoneyData {
        private String transactionId;
        private String description;
        private String clientReference;
        private Double amount;
        private Double charges;
        private Double amountAfterCharges;
        private Double amountCharged;
        private Double deliveryFee;
    }
}

