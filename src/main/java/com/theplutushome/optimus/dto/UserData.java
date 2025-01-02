package com.theplutushome.optimus.dto;

public class UserData {
    private String username;
    private String email;
    private double balance;
    private double accruedBalance;
    private int totalReferrals;
    private String referralCode;

    public UserData() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getTotalReferrals() {
        return totalReferrals;
    }

    public void setTotalReferrals(int totalReferrals) {
        this.totalReferrals = totalReferrals;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public double getAccruedBalance() {
        return accruedBalance;
    }

    public void setAccruedBalance(double accruedBalance) {
        this.accruedBalance = accruedBalance;
    }
}
