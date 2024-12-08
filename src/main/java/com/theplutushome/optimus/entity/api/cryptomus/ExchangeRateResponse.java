package com.theplutushome.optimus.entity.api.cryptomus;

import lombok.Data;
import java.util.List;

@Data
public class ExchangeRateResponse {
    private int state;
    private List<ExchangeRate> result;

    @Data
    public static class ExchangeRate {
        private String from;
        private String to;
        private String course;
    }
}
