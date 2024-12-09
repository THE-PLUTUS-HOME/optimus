package com.theplutushome.optimus.entity.api.cryptomus;

import lombok.Data;

@Data
public class Webhook {
    private String type;
    private String uuid;
    private String order_id;
    private String amount;
    private String merchant_amount;
    private String commission;
    private boolean is_final;
    private String status;
    private String txid;
    private String currency;
    private String network;
    private String payer_currency;
    private String payer_amount;
    private String sign;
}
