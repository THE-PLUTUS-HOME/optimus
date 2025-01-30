package com.theplutushome.optimus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PAYMENT_CALLBACK")
@Getter
@Setter
@NoArgsConstructor
public class PaymentCallback extends EntityModel {

}
