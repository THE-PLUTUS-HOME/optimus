package com.theplutushome.optimus.dto;

import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrdersDto {
    private String clientReference;
    private double amountGHS;
    private double fee;
    private PaymentOrderStatus status;
    private String crypto;
    private double cryptoAmount;
    private double rate;
    private String address;
    private String phoneNumber;
    private String transactionId;
    private String createdAt;
    private String updatedAt;
}
