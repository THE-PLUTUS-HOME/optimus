package com.theplutushome.optimus.entity;

import com.theplutushome.optimus.entity.enums.UserAccountStatus;
import com.theplutushome.optimus.entity.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "USERS_TBL")
@Cacheable(value = false)
@AllArgsConstructor
@Builder
public class User extends EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String username;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Enumerated(EnumType.STRING)
    private UserAccountStatus userAccountStatus;
    private String secretPhrase;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<User> referredUsers;
    private String referralCode;
    private double balance;
    private LocalDateTime lastLoggedIn;


    public User() {
    }

    public User(int id, String password, String phone, String email, String username, UserType userType, UserAccountStatus userAccountStatus, String secretPhrase, String referralCode, double balance) {
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.userType = userType;
        this.userAccountStatus = userAccountStatus;
        this.secretPhrase = secretPhrase;
        this.referralCode = referralCode;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserAccountStatus getUserAccountStatus() {
        return userAccountStatus;
    }

    public void setUserAccountStatus(UserAccountStatus userAccountStatus) {
        this.userAccountStatus = userAccountStatus;
    }

    public String getSecretPhrase() {
        return secretPhrase;
    }

    public void setSecretPhrase(String secretPhrase) {
        this.secretPhrase = secretPhrase;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getLastLoggedIn() {
        return lastLoggedIn;
    }

    public void setLastLoggedIn(LocalDateTime lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public List<User> getReferredUsers() {
        return referredUsers;
    }

    public void setReferredUsers(List<User> referredUsers) {
        this.referredUsers = referredUsers;
    }
}
