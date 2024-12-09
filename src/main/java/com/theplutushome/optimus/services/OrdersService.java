package com.theplutushome.optimus.services;

import com.theplutushome.optimus.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

    private OrderRepository orderRepository;

    public OrdersService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
