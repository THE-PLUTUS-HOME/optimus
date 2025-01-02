package com.theplutushome.optimus.entity.api.cryptomus;

import lombok.Data;
import java.util.List;

@Data
public class ServiceList {
    private int state;
    private List<CryptoInfo> result;

    @Data
    public static class CryptoInfo {
        private String network;
        private String currency;
        private boolean isAvailable;
        private Limit limit;
        private Commission commission;
    }

    @Data
    public static class Limit {
        private String minAmount;
        private String maxAmount;
    }

    @Data
    public static class Commission {
        private String feeAmount;
        private String percent;
    }
}

