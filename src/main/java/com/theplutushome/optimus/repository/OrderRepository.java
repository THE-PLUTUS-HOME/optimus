package com.theplutushome.optimus.repository;

import com.theplutushome.optimus.dto.PaymentOrderDto;
import com.theplutushome.optimus.entity.PaymentOrder;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<PaymentOrder, String> {
    PaymentOrder findPaymentOrderByClientReference(String clientReference);

    List<PaymentOrderDto> findAllByEmail(@NotNull String email);
}
