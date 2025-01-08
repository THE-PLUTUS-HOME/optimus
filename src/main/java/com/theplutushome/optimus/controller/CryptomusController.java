package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.clients.cryptomus.CryptomusRestClient;
import com.theplutushome.optimus.clients.hubtel.HubtelRestClient;
import com.theplutushome.optimus.entity.PaymentOrder;
import com.theplutushome.optimus.entity.api.cryptomus.*;
import com.theplutushome.optimus.entity.api.hubtel.SMSResponse;
import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;
import com.theplutushome.optimus.service.OrdersService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/optimus/v1/api/cryptomus")
public class CryptomusController {

    private static final Logger log = LoggerFactory.getLogger(CryptomusController.class);
    private final CryptomusRestClient client;
    private final OrdersService ordersService;
    private final HubtelRestClient hubtelRestClient;

    public CryptomusController(CryptomusRestClient client, OrdersService ordersService, HubtelRestClient hubtelRestClient) {
        this.client = client;
        this.ordersService = ordersService;
        this.hubtelRestClient = hubtelRestClient;
    }

    @GetMapping(value = "/exchange-rate/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ExchangeRateResponse getAllUsers(@PathVariable("currency") String currency, @RequestParam(value = "to", required = false) String to) {
        ExchangeRateResponse response = client.getExchangeRate(currency);
        response.getResult().removeIf(r -> !r.getTo().equals(to));
        response.getResult().get(0).setWithdrawalFee(client.getWithdrawalFee(currency));

        return response;
    }

//    @PostMapping("/balance")
    public BalanceResponse getBalance(@RequestParam(value = "currency_code", required = false) String currency) {
        BalanceResponse response = client.getBalance();

        // Check if currency filtering is required
        if (currency != null && !currency.isBlank()) {
            response.getResult().forEach(result -> {
                BalanceResponse.Balance balance = result.getBalance();

                // Filter merchant balances
                balance.getMerchant().removeIf(m -> !currency.equals(m.getCurrency_code()));

                // Filter user balances
                balance.getUser().removeIf(u -> !currency.equals(u.getCurrency_code()));
            });
        }

        return response;
    }


//    @PostMapping(value = "/payout/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public PayoutResponse getPayoutInfo(@RequestBody @Valid PayoutInfoRequest request) {
        return client.payOutInfoRequest(request);
    }

//    @PostMapping(value = "/payout/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public PayoutHistoryResponse getPayoutHistory(@RequestBody PayoutHistoryRequest request) {
        return client.getPayoutHistory(request);
    }

    @PostMapping(value = "/payout/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceList getPayoutServices(@RequestParam(value = "currency", required = true) String currency, @RequestParam(value = "network", required = true) String network) throws BadRequestException {
        if (currency.isBlank() || network.isBlank()) {
            throw new BadRequestException("Currency and network parameters are required");
        }
        ServiceList list = client.getServiceList();
        list.getResult().removeIf(l -> !l.getCurrency().equals(currency));
        list.getResult().removeIf(l -> !l.getNetwork().equals(network));

        return list;
    }

    @PostMapping(value = "/payout")
    public PayoutResponse payout(@RequestBody @Valid PayoutRequest request) {
        return client.getPayout(request);
    }

//    @PostMapping(value = "/convert")
    public ConvertResponse convert(@RequestBody @Valid ConvertRequest request) {
        return client.convertAsset(request);
    }

    @PostMapping("/callback")
    public ResponseEntity<?> getCallback(@RequestBody Webhook callback) {
        log.info("Incoming Webhook: {}", callback.toString());

        if (callback.isIs_final()) {
            PaymentOrder order = ordersService.findOrderByClientReference(callback.getOrder_id());
            if (order == null) {
                return ResponseEntity.badRequest().body("Order not found");
            }

            order.setStatus(PaymentOrderStatus.COMPLETED);
            order.setTransactionId(callback.getTxid());
            ordersService.updateOrder(order);

            if (order.getPhoneNumber() != null) {
                String customerName = order.getEmail().substring(0, order.getEmail().indexOf('@'));
                String message = "Hello " + customerName + ", your order has been finalized successfully. Please log in to the platform to view your hash. [www.theplutushome.com]";

                SMSResponse smsResponse = hubtelRestClient.sendSMS(order.getPhoneNumber(), message);
                if (smsResponse.getStatus() == 0) {
                    log.info("SMS received: {}", smsResponse);
                }
            }

            log.info("Order {} marked as completed", callback.getOrder_id());
            return ResponseEntity.ok().build();
        }

        // Log for non-final statuses
        log.info("Webhook received for non-final status: {}", callback);
        return ResponseEntity.ok("Non-final status received");
    }

}
