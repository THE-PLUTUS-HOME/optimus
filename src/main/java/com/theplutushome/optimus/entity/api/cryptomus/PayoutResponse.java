package com.theplutushome.optimus.entity.api.cryptomus;

import java.util.List;
import java.util.Map;

public class PayoutResponse {
    private int state;
    private Result result;


    public PayoutResponse(int state, Result result) {
        this.state = state;
        this.result = result;
    }

    public static class Result {
        private String uuid;
        private String amount;
        private String currency;
        private String network;
        private String address;
        private String txid;
        private String status;
        private boolean isFinal;
        private double balance;
        private String payerCurrency;
        private String payerAmount;
        private String message;
        private Error errors;
        private String code;

        public Result(String uuid, String amount, String currency, String network, String address, String txid, String status, boolean isFinal, int balance, String payerCurrency, String payerAmount, String message, String code, Error errors) {
            this.uuid = uuid;
            this.amount = amount;
            this.currency = currency;
            this.network = network;
            this.address = address;
            this.txid = txid;
            this.status = status;
            this.isFinal = isFinal;
            this.balance = balance;
            this.payerCurrency = payerCurrency;
            this.payerAmount = payerAmount;
            this.message = message;
            this.code = code;
            this.errors = errors;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public boolean isFinal() {
            return isFinal;
        }

        public void setIsFinal(boolean isFinal) {
            this.isFinal = isFinal;
        }

        public String getPayerCurrency() {
            return payerCurrency;
        }

        public void setPayerCurrency(String payerCurrency) {
            this.payerCurrency = payerCurrency;
        }

        public String getPayerAmount() {
            return payerAmount;
        }

        public void setPayerAmount(String payerAmount) {
            this.payerAmount = payerAmount;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Error getErrors() {
            return errors;
        }

        public void setErrors(Error errors) {
            this.errors = errors;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class Error {
        private Map<String, List<String>> errors; // Adjusted to match the JSON structure

        public Error(Map<String, List<String>> errors) {
            this.errors = errors;
        }

        public Map<String, List<String>> getErrors() {
            return errors;
        }

        public void setErrors(Map<String, List<String>> errors) {
            this.errors = errors;
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}