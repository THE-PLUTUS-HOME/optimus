package com.theplutushome.optimus.entity.api.cryptomus;

import java.util.List;

public class PayoutHistoryResponse {
    private int state;
    private Result result;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        private List<Item> items;
        private Paginate paginate;

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public Paginate getPaginate() {
            return paginate;
        }

        public void setPaginate(Paginate paginate) {
            this.paginate = paginate;
        }
    }

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

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isIs_final() {
            return is_final;
        }

        public void setIs_final(boolean is_final) {
            this.is_final = is_final;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }

    public static class Paginate {
        private int count;
        private boolean hasPages;
        private String nextCursor;
        private String previousCursor;
        private int perPage;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isHasPages() {
            return hasPages;
        }

        public void setHasPages(boolean hasPages) {
            this.hasPages = hasPages;
        }

        public String getNextCursor() {
            return nextCursor;
        }

        public void setNextCursor(String nextCursor) {
            this.nextCursor = nextCursor;
        }

        public String getPreviousCursor() {
            return previousCursor;
        }

        public void setPreviousCursor(String previousCursor) {
            this.previousCursor = previousCursor;
        }

        public int getPerPage() {
            return perPage;
        }

        public void setPerPage(int perPage) {
            this.perPage = perPage;
        }
    }
}

