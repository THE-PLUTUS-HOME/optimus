package com.theplutushome.optimus.entity.api.cryptomus;

import lombok.Data;
import java.util.List;

@Data
public class PayoutHistoryResponse {
    private int state;
    private Result result;

    @Data
    public static class Result {
        private List<Item> items;
        private Paginate paginate;
    }

    @Data
    public static class Item {
        private String uuid;
        private String amount;
        private String currency;
        private String network;
        private String address;
        private String txid;
        private String order_id;
        private String payment_status;
        private String status;
        private boolean is_final;
        private String balance;
        private String created_at;
        private String updated_at;
    }

    @Data
    public static class Paginate {
        private int count;
        private boolean hasPages;
        private String nextCursor;
        private String previousCursor;
        private int perPage;
    }
}

