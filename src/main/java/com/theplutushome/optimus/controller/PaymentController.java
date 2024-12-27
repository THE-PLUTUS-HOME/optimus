package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.clients.hubtel.HubtelRestClient;
import com.theplutushome.optimus.entity.PaymentOrder;
import com.theplutushome.optimus.entity.api.hubtel.HubtelCallBack;
import com.theplutushome.optimus.entity.api.hubtel.PaymentLinkRequest;
import com.theplutushome.optimus.entity.api.hubtel.PaymentLinkResponse;
import com.theplutushome.optimus.entity.api.hubtel.SMSResponse;
import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;
import com.theplutushome.optimus.service.OrdersService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/optimus/v1/api/payment")
public class PaymentController {
    
    private static final Logger log =  LoggerFactory.getLogger(PaymentController.class);

    private final HubtelRestClient client;

    private OrdersService ordersService;

    @Autowired
    public PaymentController(HubtelRestClient client, OrdersService ordersService) {

        this.client = client;
        this.ordersService = ordersService;
    }

    @GetMapping("/sendOtp")
    public SMSResponse sendMessage(@RequestParam(value = "phone") String phoneNumber) {
        return null;
    }

    @GetMapping("/verifyOtp")
    public ResponseEntity<Void> verifyOtp(@RequestParam(value = "code") String code, @RequestParam(value = "phone") String phoneNumber) {
        return null;
    }

    @Transactional
    @PostMapping("/generate")
    public PaymentLinkResponse generateLink(@RequestBody @Valid PaymentOrder request,  @RequestHeader("Authorization") String authHeader) {
        ordersService.createOrder(request, authHeader);
        PaymentLinkRequest paymentLinkRequest = new PaymentLinkRequest();
        paymentLinkRequest.setCallbackUrl(request.getCallbackUrl());
        paymentLinkRequest.setClientReference(request.getClientReference());
        paymentLinkRequest.setDescription(request.getDescription());
        paymentLinkRequest.setCancellationUrl(request.getCancellationUrl());
        paymentLinkRequest.setReturnUrl(request.getReturnUrl());
        paymentLinkRequest.setTotalAmount(request.getAmountGHS());
        paymentLinkRequest.setMerchantAccountNumber(request.getMerchantAccountNumber());

        PaymentLinkResponse response = client.getPaymentUrl(paymentLinkRequest);
        return response;
    }

    @PostMapping("/callback")
    public ResponseEntity<HubtelCallBack> paymentCallback(@RequestBody HubtelCallBack callBack) {
        log.info("Payment callback received: {}", callBack);
        if(callBack.getStatus() != null && callBack.getStatus().equalsIgnoreCase("Success")) {
            PaymentOrder order = ordersService.findOrderByClientReference(callBack.getData().getClientReference());
            order.setStatus(PaymentOrderStatus.PROCESSING);
            ordersService.updateOrder(order);
        }
        return ResponseEntity.ok(callBack);
    }
}