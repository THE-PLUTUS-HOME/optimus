package com.theplutushome.optimus.entity.api.cryptomus;

import java.util.List;

public class ExchangeRateResponse {
    private int state;
    private List<ExchangeRate> result;

    public ExchangeRateResponse(int state, List<ExchangeRate> result) {
        this.state = state;
        this.result = result;
    }

    public static class ExchangeRate {
        private String from;
        private String to;
        private String course;

        public ExchangeRate(String from, String to, String course) {
            this.from = from;
            this.to = to;
            this.course = course;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<ExchangeRate> getResult() {
        return result;
    }

    public void setResult(List<ExchangeRate> result) {
        this.result = result;
    }
}
