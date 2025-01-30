package com.theplutushome.optimus.entity;

import com.theplutushome.optimus.entity.enums.PaymentProvider;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PAYMENT_CALLBACK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCallback extends EntityModel {
    private PaymentProvider provider;
    private String responseCode;
    private String requestStatus;
    private String checkoutId;
    private String salesInvoiceId;
    private String clientReference;
    private String status;
    private double amount;
    private String customerPhoneNumber;
    private String mobileMoneyNumber;
    private String paymentType;
    private String channel;
    private String description;
    private String reason;
    private String clienttransid;
    private String telcotransid;
    private String transactionid;
    private String statusdate;
    private String paymentReference;
}
