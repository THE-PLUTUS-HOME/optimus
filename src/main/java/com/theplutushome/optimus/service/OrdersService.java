package com.theplutushome.optimus.service;

import com.theplutushome.optimus.advice.OrderNotFoundException;
import com.theplutushome.optimus.dto.PaymentOrderDto;
import com.theplutushome.optimus.entity.PaymentOrder;
import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;
import com.theplutushome.optimus.repository.OrderRepository;
import com.theplutushome.optimus.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    private OrderRepository orderRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public OrdersService(OrderRepository orderRepository, JwtUtil jwtUtil) {
        this.orderRepository = orderRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<PaymentOrderDto> getAllOrders(String email, String authHeader) {
        jwtUtil.verifyToken(authHeader);
        List<PaymentOrderDto> allOrders = orderRepository.findAllByEmail(email);
        if (!allOrders.isEmpty()) {
            return allOrders;
        }
        return null;
    }

    public PaymentOrder findOrder(int orderId, String authHeader) {
        return null;
    }

    public void createOrder(PaymentOrder order) {
        order.setStatus(PaymentOrderStatus.PENDING);
        order.setAmountPaid(0.0);
        orderRepository.save(order);
    }

    public void updateOrder(PaymentOrder order) {
        orderRepository.save(order);
    }

    public PaymentOrder findOrderByClientReference(String reference) {
        PaymentOrder order = orderRepository.findPaymentOrderByClientReference(reference);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        return order;
    }
    
     public PaymentOrder findOrderByPhoneNumber(String phoneNumber) {
        PaymentOrder order = orderRepository.findPaymentOrderByPhoneNumberAndStatus(phoneNumber, PaymentOrderStatus.PENDING);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        return order;
    }
     
     public  List<PaymentOrder> findPendingOrdersByEmail(String email){
         return orderRepository.findPaymentOrdersByEmailAndStatus(email, PaymentOrderStatus.PENDING);
     }
}
