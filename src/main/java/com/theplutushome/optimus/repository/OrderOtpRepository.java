/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.optimus.repository;

import com.theplutushome.optimus.entity.OrderOtp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author MalickMoro-Samah
 */
public interface OrderOtpRepository extends JpaRepository<OrderOtp, Integer> {
    Optional<OrderOtp> findOrderOtpByClientReference(String clientReference);
}
