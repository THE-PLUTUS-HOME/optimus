package com.theplutushome.optimus.repository;

import com.theplutushome.optimus.dto.PaymentOrderDto;
import com.theplutushome.optimus.entity.PaymentOrder;
import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<PaymentOrder, String> {
    PaymentOrder findPaymentOrderByClientReference(String clientReference);
    
    PaymentOrder findPaymentOrderByPhoneNumberAndStatus(String phoneNumber, PaymentOrderStatus status);
    
    List<PaymentOrder> findPaymentOrdersByEmailAndStatus(String email, PaymentOrderStatus status);

    List<PaymentOrderDto> findAllByEmail(@NotNull String email);

    List<PaymentOrder> findPaymentOrdersByDeleted(boolean deleted);
}
