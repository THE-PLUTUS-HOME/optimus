package com.theplutushome.optimus.entity.api.cryptomus;

import lombok.Getter;
import lombok.Setter;

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

    @Getter
    @Setter
    private static class ConvertResult {
        private double from;
        private double to;
        private double approximate_rate;
        private double total_amount;
        private double commission;

        public String toString(){
            return "ConvertResult{" +
                    "from=" + getFrom() +
                    ", to=" + getTo() +
                    ", approximate_rate=" + getApproximate_rate() +
                    ", total_amount=" + getTotal_amount() +
                    ", commission=" + getCommission() +
                    '}';
        }
    }
}