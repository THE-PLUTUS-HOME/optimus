package com.theplutushome.optimus.dto;

import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrdersDto {
    private int id;
    private String clientReference;
    private double amountGHS;
    private double fee;
    private PaymentOrderStatus status;
    private String crypto;
    private BigDecimal cryptoAmount;
    private double rate;
    private String address;
    private String phoneNumber;
    private String transactionId;
    private String createdAt;
    private String updatedAt;
}
