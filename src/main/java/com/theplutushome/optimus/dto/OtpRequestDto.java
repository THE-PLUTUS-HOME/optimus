package com.theplutushome.optimus.dto;

import jakarta.validation.constraints.NotNull;

public record OtpRequestDto(@NotNull String email, String otpCode) {
}
