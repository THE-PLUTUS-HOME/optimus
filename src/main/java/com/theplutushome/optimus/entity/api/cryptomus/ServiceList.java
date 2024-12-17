package com.theplutushome.optimus.entity.api.cryptomus;

import java.util.List;

public class ServiceList {
    private int state;
    private List<CryptoInfo> result;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<CryptoInfo> getResult() {
        return result;
    }

    public void setResult(List<CryptoInfo> result) {
        this.result = result;
    }

    public static class CryptoInfo {
        private String network;
        private String currency;
        private boolean isAvailable;
        private Limit limit;
        private Commission commission;

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public boolean isAvailable() {
            return isAvailable;
        }

        public void setAvailable(boolean available) {
            isAvailable = available;
        }

        public Limit getLimit() {
            return limit;
        }

        public void setLimit(Limit limit) {
            this.limit = limit;
        }

        public Commission getCommission() {
            return commission;
        }

        public void setCommission(Commission commission) {
            this.commission = commission;
        }
    }

    public static class Limit {
        private String min_amount;
        private String max_amount;

        public String getMin_amount() {
            return min_amount;
        }

        public void setMin_amount(String min_amount) {
            this.min_amount = min_amount;
        }

        public String getMax_amount() {
            return max_amount;
        }

        public void setMax_amount(String max_amount) {
            this.max_amount = max_amount;
        }
    }

    public static class Commission {
        private String fee_amount;
        private String percent;

        public String getFee_amount() {
            return fee_amount;
        }

        public void setFee_amount(String fee_amount) {
            this.fee_amount = fee_amount;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }
    }
}

