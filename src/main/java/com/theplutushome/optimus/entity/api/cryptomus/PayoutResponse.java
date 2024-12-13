package com.theplutushome.optimus.entity.api.cryptomus;

import lombok.Data;

import java.util.List;
import java.util.Map;

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
        private Error errors;
        private String code;
    }

    @Data
    public static class Error {
        private Map<String, List<String>> errors; // Adjusted to match the JSON structure
    }
}