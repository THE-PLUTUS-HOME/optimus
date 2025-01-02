package com.theplutushome.optimus.entity.api.cryptomus;

import java.util.List;

public class BalanceResponse {
    private int state;
    private List<BalanceResult> result;

    public static class BalanceResult {
        private Balance balance;

        public BalanceResult(Balance balance) {
            this.balance = balance;
        }

        public Balance getBalance() {
            return balance;
        }

        public void setBalance(Balance balance) {
            this.balance = balance;
        }
    }

    public static class Balance {
        private List<CurrencyBalance> merchant;
        private List<CurrencyBalance> user;

        public Balance(List<CurrencyBalance> merchant, List<CurrencyBalance> user) {
            this.merchant = merchant;
            this.user = user;
        }

        public List<CurrencyBalance> getMerchant() {
            return merchant;
        }

        public void setMerchant(List<CurrencyBalance> merchant) {
            this.merchant = merchant;
        }

        public List<CurrencyBalance> getUser() {
            return user;
        }

        public void setUser(List<CurrencyBalance> user) {
            this.user = user;
        }
    }

    public static class CurrencyBalance {
        private String uuid;
        private String balance;
        private String currency_code;

        public CurrencyBalance(String uuid, String balance, String currency_code) {
            this.uuid = uuid;
            this.balance = balance;
            this.currency_code = currency_code;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getCurrency_code() {
            return currency_code;
        }

        public void setCurrency_code(String currency_code) {
            this.currency_code = currency_code;
        }
    }

    public BalanceResponse(int state, List<BalanceResult> result) {
        this.state = state;
        this.result = result;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<BalanceResult> getResult() {
        return result;
    }

    public void setResult(List<BalanceResult> result) {
        this.result = result;
    }
}
