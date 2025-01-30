package com.theplutushome.optimus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PAYOUT_CALLBACK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayoutCallback extends EntityModel {
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
}
