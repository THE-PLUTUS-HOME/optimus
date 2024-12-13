package com.theplutushome.optimus.clients.cryptomus;

import com.theplutushome.optimus.entity.api.cryptomus.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

public interface CryptomusHttpClient {
    @PostExchange("/service-list")
    public List<ServiceList> getServiceList();

    @PostExchange("/{currency}")
    public ExchangeRateResponse getExchangeRate(@PathVariable String currency);

    @GetExchange("/balance")
    public BalanceResponse getBalance();

    @PostExchange("/payout-history")
    public PayoutHistoryResponse getPayoutHistory(@RequestBody @Valid PayoutHistoryRequest payoutHistoryRequest);

    @PostExchange("/payout")
    public PayoutResponse getPayout(@RequestBody @Valid PayoutRequest payoutRequest);

    @PostExchange("/payout-info")
    public PayoutResponse payOutInfoRequest(@RequestBody @Valid PayoutInfoRequest payoutInfoRequest);
}
