package com.theplutushome.optimus.entity;

import com.theplutushome.optimus.entity.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ORDERS")
public class Order extends EntityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private double amountGHS;
    private double amountUSD;
    private double rate;
    private String address;

}
