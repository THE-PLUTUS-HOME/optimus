package com.theplutushome.optimus.entity;

import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "ORDERS")
public class PaymentOrder extends EntityModel{
    private String description;
    private String callbackUrl;
    private String returnUrl;
    private String merchantAccountNumber;
    private String cancellationUrl;
    private String clientReference;
    @NotNull
    private double amountGHS;
    @NotNull
    private double fee;
    @Enumerated(EnumType.STRING)
    private PaymentOrderStatus status;
    @NotNull
    private String crypto;
    @NotNull
    private double cryptoAmount;
    @NotNull
    private String email;
    @NotNull
    private double rate;
    @NotNull
    private String address;
    private String phoneNumber;
    private String transactionId;
    private double amountPaid;

    // No-argument constructor
    public PaymentOrder() { }

 

    public double getCryptoAmount() {
        return cryptoAmount;
    }

    public void setCryptoAmount(double cryptoAmount) {
        this.cryptoAmount = cryptoAmount;
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getMerchantAccountNumber() {
        return merchantAccountNumber;
    }

    public void setMerchantAccountNumber(String merchantAccountNumber) {
        this.merchantAccountNumber = merchantAccountNumber;
    }

    public String getCancellationUrl() {
        return cancellationUrl;
    }

    public void setCancellationUrl(String cancellationUrl) {
        this.cancellationUrl = cancellationUrl;
    }

    public String getClientReference() {
        return clientReference;
    }

    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    public double getAmountGHS() {
        return amountGHS;
    }

    public void setAmountGHS(double amountGHS) {
        this.amountGHS = amountGHS;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public PaymentOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentOrderStatus status) {
        this.status = status;
    }

    public String getCrypto() {
        return crypto;
    }

    public void setCrypto(String crypto) {
        this.crypto = crypto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }
    
    

    // toString method
    @Override
    public String toString() {
        return "PaymentOrder{" +
               ", description='" + description + '\'' +
               ", callbackUrl='" + callbackUrl + '\'' +
               ", returnUrl='" + returnUrl + '\'' +
               ", merchantAccountNumber='" + merchantAccountNumber + '\'' +
               ", cancellationUrl='" + cancellationUrl + '\'' +
               ", clientReference='" + clientReference + '\'' +
               ", amountGHS=" + amountGHS +
               ", fee=" + fee +
               ", status='" + status + '\'' +
               ", crypto='" + crypto + '\'' +
               ", email='" + email + '\'' +
               ", rate=" + rate +
               ", address='" + address + '\'' +
               ", transactionId='" + transactionId + '\'' +
               '}';
    }
}
