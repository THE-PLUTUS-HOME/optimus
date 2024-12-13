package com.theplutushome.optimus.entity.api.cryptomus;

import lombok.Data;

@Data
public class PayoutResponse {
    private int state;
    private Result result;

    @Data
    public static class Result {
        private String uuid;
        private String amount;
        private String currency;
        private String address;
        private String txid;
        private String status;
        private boolean is_final;
        private int balance;
        private String payer_currency;
        private String payer_amount;
        private String message;
        private Error error;
        private String code;
    }
}