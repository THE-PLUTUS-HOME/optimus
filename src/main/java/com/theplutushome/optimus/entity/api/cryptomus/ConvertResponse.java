package com.theplutushome.optimus.entity.api.cryptomus;

public class ConvertResponse {
    private int state;
    private ConvertResult result;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ConvertResult getResult() {
        return result;
    }

    public void setResult(ConvertResult result) {
        this.result = result;
    }

    private static class ConvertResult {
        private double from;
        private double to;
        private double approximate_rate;
        private double total_amount;
        private double commission;

        public double getFrom() {
            return from;
        }

        public void setFrom(double from) {
            this.from = from;
        }

        public double getTo() {
            return to;
        }

        public void setTo(double to) {
            this.to = to;
        }

        public double getApproximate_rate() {
            return approximate_rate;
        }

        public void setApproximate_rate(double approximate_rate) {
            this.approximate_rate = approximate_rate;
        }

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public double getCommission() {
            return commission;
        }

        public void setCommission(double commission) {
            this.commission = commission;
        }
    }
}
