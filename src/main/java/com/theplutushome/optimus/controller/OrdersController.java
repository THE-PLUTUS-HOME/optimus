package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.dto.PaymentOrderDto;
import com.theplutushome.optimus.entity.PaymentOrder;
import com.theplutushome.optimus.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/optimus/v1/api/orders")
public class OrdersController {

    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/list/{email}")
    public List<PaymentOrderDto> getOrders(@PathVariable(value = "email") String email, @RequestHeader("Authorization") String authHeader) {
        List<PaymentOrderDto> orders = ordersService.getAllOrders(email, authHeader);
        if (orders != null) {
            return orders.stream()
                    .sorted((o1, o2) -> o2.createdAt().compareTo(o1.createdAt())) // Sort by createdAt in descending order
                    .collect(Collectors.toList());
        }
        return null;
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentOrder> getOrder(@RequestHeader("Authorization") String authHeader, @PathVariable int orderId) {
        PaymentOrder order = ordersService.findOrder(orderId, authHeader);
        return ResponseEntity.ok(order);
    }

}
