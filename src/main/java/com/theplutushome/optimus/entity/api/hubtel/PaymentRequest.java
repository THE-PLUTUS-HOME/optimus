package com.theplutushome.optimus.entity.api.hubtel;

import com.theplutushome.optimus.entity.api.hubtel.enums.PaymentChannel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PaymentRequest {

    private String CustomerName;
    private String CustomerEmail;
    @NotNull
    @Size(min = 12, max = 12, message = "Format expected [233201112223]")
    private String CustomerMsisdn;
    @NotNull
    private PaymentChannel Channel;
    @NotNull
    private double Amount;
    @NotNull
    private String PrimaryCallbackUrl;
    @NotNull
    private String Description;
    @NotNull
    private String ClientReference;

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }

    public String getCustomerMsisdn() {
        return CustomerMsisdn;
    }

    public void setCustomerMsisdn(String customerMsisdn) {
        CustomerMsisdn = customerMsisdn;
    }

    public PaymentChannel getChannel() {
        return Channel;
    }

    public void setChannel(PaymentChannel channel) {
        Channel = channel;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getPrimaryCallbackUrl() {
        return PrimaryCallbackUrl;
    }

    public void setPrimaryCallbackUrl(String primaryCallbackUrl) {
        PrimaryCallbackUrl = primaryCallbackUrl;
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
}
