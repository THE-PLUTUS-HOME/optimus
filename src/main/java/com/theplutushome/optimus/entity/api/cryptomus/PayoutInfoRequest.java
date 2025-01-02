package com.theplutushome.optimus.entity.api.cryptomus;

public class PayoutInfoRequest {
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public PayoutInfoRequest(String uuid) {
        this.uuid = uuid;
    }
}
