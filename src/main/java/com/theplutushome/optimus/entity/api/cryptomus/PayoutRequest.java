package com.theplutushome.optimus.entity.api.cryptomus;

import jakarta.validation.constraints.NotNull;

public class PayoutRequest {
    @NotNull
    private String amount;
    @NotNull
    private String currency;
    @NotNull
    private String network;
    @NotNull
    private String order_id;
    @NotNull
    private String address;
    private String priority;
    private String url_callback;
    @NotNull
    private String is_subtract;
    private String from_currency;

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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl_callback() {
        return url_callback;
    }

    public void setUrl_callback(String url_callback) {
        this.url_callback = url_callback;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getIs_subtract() {
        return is_subtract;
    }

    public void setIs_subtract(String is_subtract) {
        this.is_subtract = is_subtract;
    }

    public String getFrom_currency() {
        return from_currency;
    }

    public void setFrom_currency(String from_currency) {
        this.from_currency = from_currency;
    }
}
