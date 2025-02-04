package com.theplutushome.optimus.dto;

public record CustomerDto(String phone, int purchase_count, double totalSpent, String first_purchase, String last_purchase) {
}
