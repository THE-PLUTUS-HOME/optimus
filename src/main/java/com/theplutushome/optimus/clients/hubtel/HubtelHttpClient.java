package com.theplutushome.optimus.clients.hubtel;

import com.theplutushome.optimus.entity.api.hubtel.PaymentRequest;
import com.theplutushome.optimus.entity.api.hubtel.PaymentResponse;
import com.theplutushome.optimus.entity.api.hubtel.SMSResponse;
import com.theplutushome.optimus.entity.api.hubtel.TransactionStatusCheckResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface HubtelHttpClient {
    @PostExchange("")
    public PaymentResponse initiatePayment(@RequestBody @Valid PaymentRequest paymentRequest);

    @GetExchange("")
    public TransactionStatusCheckResponse checkTransaction(@RequestParam(value = "clientReference", required = true) String clientReference);

    @GetExchange("")
    public SMSResponse sendSMS(@RequestParam(value = "to") String to, @RequestParam(value = "content") String content);

}
