package com.theplutushome.optimus.dto;

public class UserData {
    private String username;
    private double balance;
    private double accruedBalance;
    private int totalReferrals;
    private String referralCode;

    public UserData() {}
    public UserData(String username, double balance, int totalReferrals, String referralCode, double accruedBalance) {
        this.username = username;
        this.balance = balance;
        this.totalReferrals = totalReferrals;
        this.referralCode = referralCode;
        this.accruedBalance = accruedBalance;
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
