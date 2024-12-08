package com.theplutushome.optimus.entity.api.cryptomus;

import lombok.Data;

@Data
public class PayoutRequest {
    private String amount;
    private String currency;
    private String network;
    private String order_id;
    private String address;
    private String priority;
    private String url_callback;
    private String is_subtract;
    private String from_currency;
}
