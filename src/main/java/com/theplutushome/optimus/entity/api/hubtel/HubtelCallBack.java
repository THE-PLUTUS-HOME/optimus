package com.theplutushome.optimus.entity.api.hubtel;

public class HubtelCallBack {

    private String Message;
    private String ResponseCode;
    private Data Data;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public Data getData() {
        return Data;
    }

    public void setData(Data data) {
        this.Data = data;
    }

    public static class Data {
        private String TransactionId;
        private String ExternalTransactionId;
        private String Description;
        private String ClientReference;
        private double Amount;
        private double Charges;
        private double AmountAfterCharges;
        private double AmountCharged;
        private String OrderId;
        private String PaymentDate;

        public String getTransactionId() {
            return TransactionId;
        }

        public void setTransactionId(String transactionId) {
            TransactionId = transactionId;
        }

        public String getExternalTransactionId() {
            return ExternalTransactionId;
        }

        public void setExternalTransactionId(String externalTransactionId) {
            ExternalTransactionId = externalTransactionId;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getClientReference() {
            return ClientReference;
        }

        public void setClientReference(String clientReference) {
            ClientReference = clientReference;
        }

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double amount) {
            Amount = amount;
        }

        public double getCharges() {
            return Charges;
        }

        public void setCharges(double charges) {
            Charges = charges;
        }

        public double getAmountAfterCharges() {
            return AmountAfterCharges;
        }

        public void setAmountAfterCharges(double amountAfterCharges) {
            AmountAfterCharges = amountAfterCharges;
        }

        public double getAmountCharged() {
            return AmountCharged;
        }

        public void setAmountCharged(double amountCharged) {
            AmountCharged = amountCharged;
        }

        public String getOrderId() {
            return OrderId;
        }

        public void setOrderId(String orderId) {
            OrderId = orderId;
        }

        public String getPaymentDate() {
            return PaymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            PaymentDate = paymentDate;
        }
    }
}