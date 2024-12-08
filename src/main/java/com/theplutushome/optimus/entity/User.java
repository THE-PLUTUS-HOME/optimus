package com.theplutushome.optimus.entity;

import com.theplutushome.optimus.entity.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User extends EntityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "Name required")
    private String name;
    @NotNull(message = "Password required")
    private String password;
    private String email;
    @NotNull(message = "Username required")
    private String username;
    @Enumerated(EnumType.STRING)
    private UserType userType;

}
