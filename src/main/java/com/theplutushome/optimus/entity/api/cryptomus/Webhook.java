package com.theplutushome.optimus.entity.api.cryptomus;

public class Webhook {
    private String type;
    private String uuid;
    private String order_id;
    private String amount;
    private String merchant_amount;
    private String commission;
    private boolean is_final;
    private String status;
    private String txid;
    private String currency;
    private String network;
    private String payer_currency;
    private String payer_amount;
    private String sign;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMerchant_amount() {
        return merchant_amount;
    }

    public void setMerchant_amount(String merchant_amount) {
        this.merchant_amount = merchant_amount;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public boolean isIs_final() {
        return is_final;
    }

    public void setIs_final(boolean is_final) {
        this.is_final = is_final;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
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

    public String getPayer_currency() {
        return payer_currency;
    }

    public void setPayer_currency(String payer_currency) {
        this.payer_currency = payer_currency;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPayer_amount() {
        return payer_amount;
    }

    public void setPayer_amount(String payer_amount) {
        this.payer_amount = payer_amount;
    }

    @Override
    public String toString() {
        return "Webhook{" +
                "type='" + type + '\'' +
                ", uuid='" + uuid + '\'' +
                ", order_id='" + order_id + '\'' +
                ", amount='" + amount + '\'' +
                ", merchant_amount='" + merchant_amount + '\'' +
                ", commission='" + commission + '\'' +
                ", is_final=" + is_final +
                ", status='" + status + '\'' +
                ", txid='" + txid + '\'' +
                ", currency='" + currency + '\'' +
                ", network='" + network + '\'' +
                ", payer_currency='" + payer_currency + '\'' +
                ", payer_amount='" + payer_amount + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
