package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.clients.cryptomus.CryptomusRestClient;
import com.theplutushome.optimus.entity.PaymentOrder;
import com.theplutushome.optimus.entity.api.cryptomus.*;
import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;
import com.theplutushome.optimus.service.OrdersService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/optimus/v1/api/cryptomus")
public class CryptomusController {

    private final CryptomusRestClient client;
    private final OrdersService ordersService;

    public CryptomusController(CryptomusRestClient client, OrdersService ordersService) {
        this.client = client;
        this.ordersService = ordersService;
    }

    @GetMapping(value = "/exchange-rate/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ExchangeRateResponse getAllUsers(@PathVariable("currency") String currency, @RequestParam(value = "to", required = false) String to) {
        ExchangeRateResponse response = client.getExchangeRate(currency);
        response.getResult().removeIf(r -> !r.getTo().equals(to));
        return response;
    }

    @PostMapping("/balance")
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


    @PostMapping(value = "/payout/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public PayoutResponse getPayoutInfo(@RequestBody @Valid PayoutInfoRequest request) {
        return client.payOutInfoRequest(request);
    }

    @PostMapping(value = "/payout/history", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "/convert")
    public ConvertResponse convert(@RequestBody @Valid ConvertRequest request) {
        return client.convertAsset(request);
    }

    @PostMapping("/callback")
    public ResponseEntity<?> getCallback(Webhook callback){
        if(callback.isIs_final()){
            PaymentOrder order = ordersService.findOrderByClientReference(callback.getOrder_id());
            order.setStatus(PaymentOrderStatus.COMPLETED);
            order.setTransactionId(callback.getTxid());
            ordersService.updateOrder(order);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }
}
