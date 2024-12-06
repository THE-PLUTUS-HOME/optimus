package com.theplutushome.optimus.entity;

import com.theplutushome.optimus.entity.enums.Currency;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ORDERS")
@Entity
public class Order extends EntityModel {

    @Enumerated(EnumType.STRING)
    private Currency currency;
    private Double amountGHS;
    private Double amountUSD;
    private Double rate;
    private String address;

}
