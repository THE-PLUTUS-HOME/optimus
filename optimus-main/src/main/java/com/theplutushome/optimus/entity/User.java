package com.theplutushome.optimus.entity;

import com.theplutushome.optimus.entity.enums.UserAccountStatus;
import com.theplutushome.optimus.entity.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS_TBL")
@Cacheable(value = false)
public class User extends EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Enumerated(EnumType.STRING)
    private UserAccountStatus userAccountStatus;
    private String secretPhrase;

    public User(int id, String name, String password, String email, String username, UserType userType, UserAccountStatus userAccountStatus, String secretPhrase) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.username = username;
        this.userType = userType;
        this.userAccountStatus = userAccountStatus;
        this.secretPhrase = secretPhrase;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getsecretPhrase() {
        return secretPhrase;
    }

    public void setsecretPhrase(String secretPhrase) {
        this.secretPhrase = secretPhrase;
    }
}
