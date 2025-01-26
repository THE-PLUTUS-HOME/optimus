package com.theplutushome.optimus.dto;

import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;

import java.time.LocalDateTime;

public record PaymentOrderDto(
        int id,
        double amountGHS,
        double fee,
        PaymentOrderStatus status,
        String crypto,
        double cryptoAmount,
        double rate,
        String clientReference,
        String address,
        String phoneNumber,
        String transactionId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
