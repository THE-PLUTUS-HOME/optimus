package com.theplutushome.optimus.dto;

import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;

import java.time.LocalDateTime;

public record PaymentOrderDto(
        double amountGHS,
        double fee,
        PaymentOrderStatus status,
        String crypto,
        double cryptoAmount,
        double rate,
        String address,
        String transactionId,
        LocalDateTime createdAt
) {}
