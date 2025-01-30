package com.theplutushome.optimus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theplutushome.optimus.entity.PaymentCallback;

public interface PaymentCallbackRepository extends JpaRepository<PaymentCallback, Integer> {

    Optional<PaymentCallback> findPaymentCallbackByClientReference(String clientReference);
}
