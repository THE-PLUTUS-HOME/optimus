package com.theplutushome.optimus.service;

import com.theplutushome.optimus.entity.Order;
import com.theplutushome.optimus.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    private OrderRepository orderRepository;

    @Autowired
    public OrdersService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders(int userId, String authHeader) {
        return null;
    }

    public Order findOrder(int orderId, String authHeader) {
        return null;
    }

    public void createOrder(Order order, String authHeader) {
        return;
    }
}
