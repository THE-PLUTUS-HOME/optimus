package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.advice.AmountNotFeasibleException;
import com.theplutushome.optimus.clients.cryptomus.CryptomusRestClient;
import com.theplutushome.optimus.clients.hubtel.HubtelRestClient;
import com.theplutushome.optimus.dto.SMSRequest;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@PropertySource("classpath:application.properties")
@RestController
@RequestMapping("/optimus/v1/api/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final HubtelRestClient client;

    private final OrdersService ordersService;

    private final CryptomusRestClient cryptomusRestClient;

    private final JwtUtil jwtUtil;
    
    private final String merchantAccountNumber;

    @Autowired
    public PaymentController(HubtelRestClient client, OrdersService ordersService, JwtUtil jwtUtil, CryptomusRestClient cryptomusRestClient, Environment env) {
        this.jwtUtil = jwtUtil;
        this.client = client;
        this.ordersService = ordersService;
        this.cryptomusRestClient = cryptomusRestClient;
        this.merchantAccountNumber = env.getProperty("pos_sales_id");
    }

    @PostMapping("/sendMessage")
    public SMSResponse sendMessage(@RequestBody SMSRequest request) {
        return client.sendSMS(request.getPhone(), request.getMessage());
    }

    //    @GetMapping("/verifyOtp")
    public ResponseEntity<Void> verifyOtp(@RequestParam(value = "code") String code, @RequestParam(value = "phone") String phoneNumber) {
        return null;
    }

    @Transactional
    @PostMapping("/generate")
    public PaymentLinkResponse generateLink(@RequestBody @Valid PaymentOrder request, @RequestHeader("Authorization") String authHeader) {
        System.out.println("The payment request: " + request.toString());
        double merchantBalance = cryptomusRestClient.getMerchantBalance();
        double purchaseAmount = cryptomusRestClient.convertCryptoAmountToUsd(request.getCrypto(), request.getCryptoAmount());
        double withdrawalFee = cryptomusRestClient.getWithdrawalFee(request.getCrypto());

        if (purchaseAmount + withdrawalFee > merchantBalance) {
            // Send text message to admin
            String username = request.getEmail().substring(0, request.getEmail().indexOf('@')); // "kingmartin"
            String message = "Almighty King Plutus, " + username + " is trying to purchase an amount of " + String.format("%.2f", purchaseAmount) + " USD" + " but your balance is " + String.format("%.2f", merchantBalance) + " USD. Kindly top up to keep your kingdom at peace. Thank you!";
            SMSResponse smsResponse = client.sendSMS("233555075023", message);
            SMSResponse smsResponse1 = client.sendSMS("233599542518", message);
            log.info(smsResponse.toString());
            log.info(smsResponse1.toString());
            throw new AmountNotFeasibleException();
        }

        request.setDescription("Item Purchase");
        request.setCallbackUrl("https://optimus-backend-49b31c7c7d3a.herokuapp.com/optimus/v1/api/payment/callback");
        request.setReturnUrl("https://theplutushome.com/payment/success");
        request.setMerchantAccountNumber(merchantAccountNumber);
        request.setCancellationUrl("https://theplutushome.com/payment/failed");
        ordersService.createOrder(request, authHeader);
        PaymentLinkRequest paymentLinkRequest = getPaymentLinkRequest(request);

        return client.getPaymentUrl(paymentLinkRequest);
    }

    private static PaymentLinkRequest getPaymentLinkRequest(PaymentOrder request) {
        PaymentLinkRequest paymentLinkRequest = new PaymentLinkRequest();
        paymentLinkRequest.setCallbackUrl(request.getCallbackUrl());
        paymentLinkRequest.setClientReference(request.getClientReference());
        paymentLinkRequest.setDescription(request.getDescription());
        paymentLinkRequest.setCancellationUrl(request.getCancellationUrl());
        paymentLinkRequest.setReturnUrl(request.getReturnUrl());
        paymentLinkRequest.setTotalAmount(request.getAmountGHS());
        paymentLinkRequest.setMerchantAccountNumber(request.getMerchantAccountNumber());
        return paymentLinkRequest;
    }


    @PostMapping("/callback")
    public ResponseEntity<?> paymentCallback(@RequestBody HubtelCallBack callBack) {
        log.info("Payment callback received: {}", callBack.toString());

        if (callBack.getStatus() != null && callBack.getStatus().equalsIgnoreCase("Success")) {
            // Find the order by client reference
            PaymentOrder order = ordersService.findOrderByClientReference(callBack.getData().getClientReference());

            if (order == null) {
                log.error("Order not found for client reference: {}", callBack.getData().getClientReference());
                return ResponseEntity.badRequest().body("Order not found");
            }

            // Process the payout request
            PayoutRequest request = getPayoutRequest(order);
            PayoutResponse payoutResponse = cryptomusRestClient.getPayout(request);

            // Update order status based on payout response
            if (payoutResponse.getState() == 0) {
                order.setStatus(PaymentOrderStatus.PROCESSING);

                if (callBack.getData().getCustomerPhoneNumber() != null) {
                    order.setPhoneNumber(callBack.getData().getCustomerPhoneNumber());

                    String customerName = order.getEmail().substring(0, order.getEmail().indexOf('@'));
                    String message = "Hi " + customerName + ", your payment was successful and your order is now being processed. Thank you for your purchase!.";

                    SMSResponse smsResponse = client.sendSMS(callBack.getData().getCustomerPhoneNumber(), message);
                    if (smsResponse.getStatus() == 0) {
                        log.info("SMS received: {}", smsResponse);
                    } else {
                        log.error("SMS received: {}", smsResponse);
                    }
                }
            } else {
                order.setStatus(PaymentOrderStatus.FAILED);
            }

            ordersService.updateOrder(order);

            // Return a success response
            log.info("Payment processed successfully for order: {}", order.getClientReference());
            return ResponseEntity.ok("Payment processed successfully");
        }

        // Return bad request for invalid callback status
        log.error("Invalid callback status: {}", callBack.getStatus());
        return ResponseEntity.badRequest().body("Invalid callback status");
    }


    @GetMapping("/verify/{reference}")
    public ResponseEntity<?> verifyPayment(@PathVariable("reference") String reference, @RequestHeader("Authorization") String authHeader) {
        jwtUtil.verifyToken(authHeader);
        TransactionStatusCheckResponse response = client.checkTransaction(reference);
        if (Objects.equals(response.getResponseCode(), "0000") && response.getData().getStatus().equalsIgnoreCase("Paid")) {
            PaymentOrder order = ordersService.findOrderByClientReference(response.getData().getClientReference());

            PayoutRequest request = getPayoutRequest(order);
            PayoutResponse payoutResponse = cryptomusRestClient.getPayout(request);
            if (payoutResponse.getState() == 0) {
                order.setStatus(PaymentOrderStatus.PROCESSING);
            } else {
                order.setStatus(PaymentOrderStatus.FAILED);
            }
            ordersService.updateOrder(order);
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

    @PostMapping("/direct-debit")
    public ResponseEntity<?> directDebit(@RequestBody PaymentRequest request, @RequestHeader("Authorization") String authHeader) {
        jwtUtil.verifyToken(authHeader);
        PaymentResponse response = client.initiatePayment(request);
        return ResponseEntity.ok(response);

    }
}