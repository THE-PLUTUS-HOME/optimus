package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.clients.cryptomus.CryptomusRestClient;
import com.theplutushome.optimus.clients.hubtel.HubtelRestClient;
import com.theplutushome.optimus.entity.PaymentOrder;
import com.theplutushome.optimus.entity.api.cryptomus.PayoutRequest;
import com.theplutushome.optimus.entity.api.cryptomus.PayoutResponse;
import com.theplutushome.optimus.entity.api.hubtel.HubtelCallBack;
import com.theplutushome.optimus.entity.api.hubtel.PaymentLinkRequest;
import com.theplutushome.optimus.entity.api.hubtel.PaymentLinkResponse;
import com.theplutushome.optimus.entity.api.hubtel.SMSResponse;
import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;
import com.theplutushome.optimus.service.OrdersService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/optimus/v1/api/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final HubtelRestClient client;

    private final OrdersService ordersService;

    private final CryptomusRestClient cryptomusRestClient;

    @Autowired
    public PaymentController(HubtelRestClient client, OrdersService ordersService, CryptomusRestClient cryptomusRestClient) {

        this.client = client;
        this.ordersService = ordersService;
        this.cryptomusRestClient = cryptomusRestClient;
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
    public PaymentLinkResponse generateLink(@RequestBody @Valid PaymentOrder request, @RequestHeader("Authorization") String authHeader) {
        System.out.println("The payment request: " + request.toString());
        ordersService.createOrder(request, authHeader);
        PaymentLinkRequest paymentLinkRequest = new PaymentLinkRequest();
        paymentLinkRequest.setCallbackUrl(request.getCallbackUrl());
        paymentLinkRequest.setClientReference(request.getClientReference());
        paymentLinkRequest.setDescription(request.getDescription());
        paymentLinkRequest.setCancellationUrl(request.getCancellationUrl());
        paymentLinkRequest.setReturnUrl(request.getReturnUrl());
        paymentLinkRequest.setTotalAmount(request.getAmountGHS());
        paymentLinkRequest.setMerchantAccountNumber(request.getMerchantAccountNumber());

        return client.getPaymentUrl(paymentLinkRequest);
    }

    @PostMapping("/callback")
    public ResponseEntity<PayoutResponse> paymentCallback(@RequestBody HubtelCallBack callBack) {
        log.info("Payment callback received: {}", callBack);
        if (callBack.getStatus() != null && callBack.getStatus().equalsIgnoreCase("Success")) {
            PaymentOrder order = ordersService.findOrderByClientReference(callBack.getData().getClientReference());
            order.setStatus(PaymentOrderStatus.PROCESSING);
            ordersService.updateOrder(order);

            PayoutRequest request = getPayoutRequest(order);
            PayoutResponse response = cryptomusRestClient.getPayout(request);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

    private static @org.jetbrains.annotations.NotNull PayoutRequest getPayoutRequest(PaymentOrder order) {
        PayoutRequest request = new PayoutRequest();
        request.setAddress(order.getAddress());
        request.setAmount(String.valueOf(order.getCryptoAmount()));
        request.setNetwork(order.getCrypto().equalsIgnoreCase("USDT") ? "TRON" : order.getCrypto().toUpperCase());
        request.setCurrency(order.getCrypto().toUpperCase());
        request.setPriority("1");
        request.setFrom_currency("USDT");
        request.setIs_subtract("1");
        request.setOrder_id(order.getClientReference());
        request.setUrl_callback("");
        return request;
    }
}