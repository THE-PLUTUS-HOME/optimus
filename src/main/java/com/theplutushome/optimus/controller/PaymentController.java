package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.advice.AmountNotFeasibleException;
import com.theplutushome.optimus.advice.OtpCodeInvalidException;
import com.theplutushome.optimus.clients.cryptomus.CryptomusRestClient;
import com.theplutushome.optimus.clients.hubtel.HubtelRestClient;
import com.theplutushome.optimus.dto.PaymentCheck;
import com.theplutushome.optimus.dto.PaymentOtpRequest;
import com.theplutushome.optimus.dto.PaymentOtpVerify;
import com.theplutushome.optimus.dto.SMSRequest;
import com.theplutushome.optimus.entity.OrderOtp;
import com.theplutushome.optimus.entity.PaymentOrder;
import com.theplutushome.optimus.entity.api.cryptomus.PayoutRequest;
import com.theplutushome.optimus.entity.api.cryptomus.PayoutResponse;
import com.theplutushome.optimus.entity.api.hubtel.*;
import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;
import com.theplutushome.optimus.repository.OrderOtpRepository;
import com.theplutushome.optimus.service.OrdersService;
import com.theplutushome.optimus.util.Function;
import com.theplutushome.optimus.util.JwtUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
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
    private OrderOtpRepository orderOtpRepository;

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
        jwtUtil.verifyToken(authHeader);
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
        ordersService.createOrder(request);
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
        PaymentOrder order = ordersService.findOrderByClientReference(callBack.getData().getClientReference());
        if (callBack.toString().equalsIgnoreCase("Failed")) {
            order.setStatus(PaymentOrderStatus.FAILED);
            ordersService.updateOrder(order);
            log.info("Invalid callback status: {}", callBack.toString());
            return ResponseEntity.ok().body("Order Failed");
        }

        if (callBack.getStatus() != null && callBack.getStatus().equalsIgnoreCase("Success")) {
            // Find the order by client reference

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

        return ResponseEntity.ok().body("Order Processed");
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

    private static @org.jetbrains.annotations.NotNull
    PayoutRequest getPayoutRequest(PaymentOrder order) {
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

//     USSDCallback{
//        responseCode=0000,
//        message=success,
//        data=Data{amount=5.0, 
//            charges=0.1, 
//            amountAfterCharges=5.0, 
//            description=,
//            clientReference=NHafa98374a50548369b773ca79b1f2281_233203212972_12221,
//            transactionId=194478791E66569, externalTransactionId=0000008531731704,
//            amountCharged=5.1, 
//            orderId=0565b79b942e420bab0dd35954f39724, 
//            paymentDate=2025-01-08T20:07:34.6254007+00:00
//     }}
    @PostMapping("/sms/callback")
    public ResponseEntity<?> ussdPaymentResponse(@RequestBody USSDCallback callback) {
        log.info(callback.toString());
        if (callback.getResponseCode().equals("0000") && callback.getMessage().equalsIgnoreCase("success")) {
            double amountPaid = callback.getData().getAmountAfterCharges();
            String paymentReference = callback.getData().getOrderId();
            String[] parts = callback.getData().getClientReference().split("_");
            String customerPhone = parts[1];

            PaymentOrder order = ordersService.findOrderByPhoneNumber(customerPhone);

            if (order.getPaymentReference() == null) {
                log.info(String.format("The amount paid is %s and the amount in the system is %s", amountPaid, order.getAmountGHS()));
                order.setPaymentReference(paymentReference);
                order.setAmountPaid(amountPaid);
            }

            if (!order.getPaymentReference().equals(paymentReference)) {
                log.info(String.format("The amount paid is %s and the amount in the system is %s", amountPaid, order.getAmountGHS()));
                order.setAmountPaid(order.getAmountPaid() + amountPaid);
                order.setPaymentReference(paymentReference);
            }

            ordersService.updateOrder(order);
        }
        return ResponseEntity.ok("DONE");
    }

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody @Valid PaymentOrder request, @RequestHeader("Authorization") String authHeader) {
        jwtUtil.verifyToken(authHeader);
        System.out.println("The payment request: " + request.toString());

        List<PaymentOrder> pendingOrders = ordersService.findPendingOrdersByEmail(request.getEmail());
        if (!pendingOrders.isEmpty()) {
            for (PaymentOrder order : pendingOrders) {
                if (order.getStatus() == PaymentOrderStatus.PENDING) {
                    order.setStatus(PaymentOrderStatus.ABANDONED);
                    order.setAmountPaid(0.0);
                    ordersService.updateOrder(order);
                }
            }
        }

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
        ordersService.createOrder(request);

        return ResponseEntity.ok(request.getClientReference());

    }

    @PostMapping("/sendCode")
    public ResponseEntity<?> sendOtpCode(@RequestBody @Valid PaymentOtpRequest otpRequest, @RequestHeader("Authorization") String authHeader) {
        jwtUtil.verifyToken(authHeader);
        PaymentOrder order = ordersService.findOrderByClientReference(otpRequest.getClientReference());
        order.setPhoneNumber(otpRequest.getPhoneNumber());

        OrderOtp orderOtp = orderOtpRepository.findOrderOtpByClientReferenceAndExpired(order.getClientReference(), false).orElse(null);
        if (orderOtp == null) {
            String otpCode = Function.generateFourDigitCode();
            String otpPrefix = Function.generateOtpPrefix();
            orderOtp = new OrderOtp(otpPrefix, otpCode, order.getClientReference());
            orderOtpRepository.save(orderOtp);
        }

        String otpMessage = String.format("Your payment verification code is %s-%s. Please enter this code to proceed with your transaction. This code will expire in 10 minutes. Thank you!", orderOtp.getSuffix(), orderOtp.getCode());
        SMSResponse smsResponse = client.sendSMS(otpRequest.getPhoneNumber(), otpMessage);
        if (smsResponse.getStatus() == 0) {

        } else {
            log.info(smsResponse.toString());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(orderOtp.getSuffix());
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<?> verifyOtpCode(@RequestBody @Valid PaymentOtpVerify otpVerify, @RequestHeader("Authorization") String authHeader) {
        jwtUtil.verifyToken(authHeader);
        OrderOtp orderOtp = orderOtpRepository.findOrderOtpByClientReferenceAndExpired(otpVerify.getClientReference(), false).orElse(null);
        if (orderOtp == null) {
            throw new RuntimeException("Order OTP Not Found");
        }

        if (!orderOtp.getCode().equals(otpVerify.getOtpCode())) {
            throw new OtpCodeInvalidException();
        }
        orderOtp.setExpired(true);
        orderOtpRepository.save(orderOtp);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/checkPayment/{reference}")
    public ResponseEntity<?> checkPayment(@RequestHeader("Authorization") String authHeader, @PathVariable(name = "reference") String clientReference) {
        PaymentOrder order = ordersService.findOrderByClientReference(clientReference);
        PaymentCheck payment = new PaymentCheck();
        if (order.getAmountPaid() < order.getAmountGHS()) {
            double amountRemaining = order.getAmountGHS() - order.getAmountPaid();
            amountRemaining = Math.round(amountRemaining * 100.0) / 100.0; // Round to 2 decimal places
            payment.setAmountRemaining(amountRemaining);
            payment.setMessage("Incomplete");

        } else {
            payment.setAmountRemaining(0.00);
            payment.setMessage("Complete");
            PayoutRequest request = getPayoutRequest(order);
            PayoutResponse payoutResponse = cryptomusRestClient.getPayout(request);

            // Update order status based on payout response
            if (payoutResponse.getState() == 0) {
                order.setStatus(PaymentOrderStatus.PROCESSING);

                String customerName = order.getEmail().substring(0, order.getEmail().indexOf('@'));
                String message = "Hi " + customerName + ", your payment was successful and your order is now being processed. Thank you for your purchase!.";

                SMSResponse smsResponse = client.sendSMS(order.getPhoneNumber(), message);
                if (smsResponse.getStatus() == 0) {
                    payment.setAmountRemaining(0.0);
                    payment.setMessage("Incomplete");
                } else {
                    payment.setAmountRemaining(0.0);
                    payment.setMessage("Sms Error");
                }
            }

            ordersService.updateOrder(order);

            // Return a success response
            log.info("Payment processed successfully for order: {}", order.getClientReference());
            return ResponseEntity.ok(payment);
        }

        return ResponseEntity.ok(payment);
    }

    @PostMapping("/cancel/{reference}")
    public ResponseEntity<?> cancelOrder(@RequestHeader("Authorization") String authHeader, @PathVariable("reference") String reference) {
        jwtUtil.verifyToken(authHeader);
        PaymentOrder order = ordersService.findOrderByClientReference(reference);
        order.setStatus(PaymentOrderStatus.CANCELED);
        ordersService.updateOrder(order);
        return ResponseEntity.ok().build();
    }

}
