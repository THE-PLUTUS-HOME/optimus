package com.theplutushome.optimus.entity.api.cryptomus;

import jakarta.validation.constraints.NotNull;

public class ConvertRequest {
    @NotNull
    private String from;
    @NotNull
    private String to;
    private String from_amount;
    private String to_amount;

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

    public String getFrom_amount() {
        return from_amount;
    }

    public void setFrom_amount(String from_amount) {
        this.from_amount = from_amount;
    }

    public String getTo_amount() {
        return to_amount;
    }

    public void setTo_amount(String to_amount) {
        this.to_amount = to_amount;
    }
}
