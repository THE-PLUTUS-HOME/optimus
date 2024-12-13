package com.theplutushome.optimus.entity.api.cryptomus;

public class PayoutHistoryRequest {

    private String date_from;
    private String date_to;

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }
}
