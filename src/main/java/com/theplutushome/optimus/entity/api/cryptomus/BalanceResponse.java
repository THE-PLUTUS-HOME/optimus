package com.theplutushome.optimus.entity.api.cryptomus;

import lombok.Data;
import java.util.List;

@Data
public class BalanceResponse {
    private int state;
    private List<BalanceResult> result;

    @Data
    public static class BalanceResult {
        private Balance balance;
    }

    @Data
    public static class Balance {
        private List<CurrencyBalance> merchant;
        private List<CurrencyBalance> user;
    }

    @Data
    public static class CurrencyBalance {
        private String uuid;
        private String balance;
        private String currencyCode;
    }
}
