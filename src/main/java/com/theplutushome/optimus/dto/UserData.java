package com.theplutushome.optimus.dto;

public class UserData {
    private String username;
    private double balance;
    private int totalReferrals;
    private String referralCode;

    public UserData() {}
    public UserData(String username, double balance, int totalReferrals, String referralCode) {
        this.username = username;
        this.balance = balance;
        this.totalReferrals = totalReferrals;
        this.referralCode = referralCode;
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
}
