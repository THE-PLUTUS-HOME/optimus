package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.clients.cryptomus.CryptomusRestClient;
import com.theplutushome.optimus.clients.hubtel.HubtelRestClient;
import com.theplutushome.optimus.entity.PaymentOrder;
import com.theplutushome.optimus.entity.api.cryptomus.PayoutRequest;
import com.theplutushome.optimus.entity.api.cryptomus.PayoutResponse;
import com.theplutushome.optimus.entity.api.hubtel.*;
import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;
import com.theplutushome.optimus.service.OrdersService;
import com.theplutushome.optimus.util.JwtUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/optimus/v1/api/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final HubtelRestClient client;

    private final OrdersService ordersService;

    private final CryptomusRestClient cryptomusRestClient;

    private JwtUtil jwtUtil;

    @Autowired
    public PaymentController(HubtelRestClient client, OrdersService ordersService, JwtUtil jwtUtil, CryptomusRestClient cryptomusRestClient) {
        this.jwtUtil = jwtUtil;
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

    @GetMapping("/verify/{reference}")
    public ResponseEntity<?> verifyPayment(@PathVariable("reference") String reference, @RequestHeader("Authorization") String authHeader) {
        jwtUtil.verifyToken(authHeader);
        TransactionStatusCheckResponse response = client.checkTransaction(reference);
        if (Objects.equals(response.getResponseCode(), "0000") && response.getData().getStatus().equalsIgnoreCase("Paid")) {
            PaymentOrder order = ordersService.findOrderByClientReference(reference);
            order.setStatus(PaymentOrderStatus.PROCESSING);
            ordersService.updateOrder(order);

            PayoutRequest request = getPayoutRequest(order);
            PayoutResponse payoutResponse = cryptomusRestClient.getPayout(request);
            return ResponseEntity.ok(payoutResponse);
        }
        return ResponseEntity.badRequest().build();
    }

    private static @org.jetbrains.annotations.NotNull PayoutRequest getPayoutRequest(PaymentOrder order) {
        PayoutRequest request = new PayoutRequest();
        request.setAddress(order.getAddress());
        request.setAmount(String.valueOf(order.getCryptoAmount()));
        request.setNetwork(order.getCrypto().equalsIgnoreCase("USDT") ? "TRON" : order.getCrypto().toUpperCase());
        request.setCurrency(order.getCrypto().toUpperCase());
        request.setPriority("recommended");
        request.setFrom_currency("USDT");
        request.setIs_subtract("1");
        request.setOrder_id(order.getClientReference());
        request.setUrl_callback("https://optimus-backend-49b31c7c7d3a.herokuapp.com/optimus/v1/api/cryptomus/callback");
        return request;
    }
}